package com.miaoshaProject.service.impl;

import com.miaoshaProject.dao.OrderDOMapper;
import com.miaoshaProject.dao.SequenceDOMapper;
import com.miaoshaProject.dataobject.OrderDO;
import com.miaoshaProject.dataobject.SequenceDO;
import com.miaoshaProject.error.BusinessException;
import com.miaoshaProject.error.EmBusinessError;
import com.miaoshaProject.service.ItemService;
import com.miaoshaProject.service.OrderService;
import com.miaoshaProject.service.UserService;
import com.miaoshaProject.service.model.ItemModel;
import com.miaoshaProject.service.model.OrderModel;
import com.miaoshaProject.service.model.UserModel;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.plaf.basic.BasicButtonUI;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class OrderServiceImp implements OrderService {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderDOMapper orderDOMapper;

    @Autowired
    private SequenceDOMapper sequenceDOMapper;

    @Override
    @Transactional
    public OrderModel createOreder(Integer userId, Integer itemId, Integer amount) throws BusinessException {
        //校验下单状态，下单产品是否存在，用户是否合法，购买数量是否正确
        ItemModel itemModel = itemService.getItemById(itemId);
        if (itemModel == null){
            throw  new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"商品不存在");
        }
        UserModel userModel = userService.getUserById(userId);
        if(userModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"用户不想存在");
        }
        if (amount <= 0 || amount >99){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"数量信息不正确");
        }

        //落单减库存
        boolean result = itemService.decreaseStock(itemId,amount);
        if(!result){
            throw new BusinessException(EmBusinessError.STOCK_NOT_ENOUGH);
        }
        //订单入库
        OrderModel orderModel = new OrderModel();
        orderModel.setUserId(userId);
        orderModel.setItemId(itemId);
        orderModel.setAmount(amount);
        orderModel.setItemPrice(itemModel.getPrice());
        orderModel.setOrderAmount(itemModel.getPrice().multiply(new BigDecimal(amount)));

        //生成及交易流水号
        orderModel.setId(generateOrderNo());

        OrderDO orderDO = convertFromOrderModel(orderModel);
        orderDOMapper.insertSelective(orderDO);

        //销量增加
        itemService.increaseSales(itemId,amount);

        //返回前端
        return orderModel;
    }

    private OrderDO convertFromOrderModel(OrderModel orderModel){
        if(orderModel == null){
            return null;
        }
        OrderDO orderDO = new OrderDO();
        BeanUtils.copyProperties(orderModel,orderDO);
        orderDO.setItemPrice(orderModel.getItemPrice().doubleValue());
        orderDO.setOrderPrice(orderModel.getOrderPrice().doubleValue());
        return orderDO;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)//重新开启事务
    private String generateOrderNo(){
        //16位
        StringBuilder stringBuilder = new StringBuilder();
        //前8位为时间信息，年月日
        LocalDateTime now = LocalDateTime.now();
        String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-","");
        stringBuilder.append(nowDate);
        //中间六位为自增序列
        //获取当前seqence
        //有可能存在以下问题：1.没有设置最大值，解决办法：在表里设置初始值和最大值，当前值超过最大值的时候再初始化
        //调取该方法的方法内事务回滚，会生成相同的订单号失去全局唯一性
        int sequence = 0;
        SequenceDO sequenceDO = sequenceDOMapper.getSequenceByName("order_info");
        sequence = sequenceDO.getCurrentValue();
        //得到数据之后更新表，现在加步长
        sequenceDO.setCurrentValue(sequenceDO.getCurrentValue() + sequenceDO.getStep());
        sequenceDOMapper.updateByPrimaryKeySelective(sequenceDO);
        String sequenceStr = String.valueOf(sequence);
        //凑够六位.用0填充
        for (int i = 0;i< 6-sequenceStr.length();i++ ){
            stringBuilder.append(0);
        }
        stringBuilder.append(sequenceStr);
        //后两位为分库分表位
        stringBuilder.append("00");

        return stringBuilder.toString();
    }
}
