package com.miaoshaProject.service.impl;

import com.miaoshaProject.dao.ShopCarDOMapper;
import com.miaoshaProject.dataobject.ShopCarDO;
import com.miaoshaProject.error.BusinessException;
import com.miaoshaProject.service.ItemService;
import com.miaoshaProject.service.ShopCarService;
import com.miaoshaProject.service.ValidateService;
import com.miaoshaProject.service.model.ItemModel;
import com.miaoshaProject.service.model.ShopCarModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ShopCarServiceImp implements ShopCarService {

    @Autowired
    private ValidateService validateService;

    @Autowired
    private ShopCarDOMapper shopCarDOMapper;

    @Autowired
    private ItemService itemService;

    @Override
    public ShopCarModel addProductToCar(Integer userId, Integer itemId, Integer promoId, Integer amount) throws BusinessException {
        //验证商品、活动等内容
        validateService.userAndItemValidate(userId,itemId,promoId,amount);
        //判断该用户购物车里是否有商品
        ShopCarModel shopCarModel = convertShopCarModelFromShopCarDO(shopCarDOMapper.selectByItemIdUserId(userId, itemId));
        //如果为空，则插入购物车
        if(shopCarModel == null){
            //从商品列表中获取该商品
            ItemModel itemModel = itemService.getItemById(itemId);
            //新建一个商品类
            shopCarModel = new ShopCarModel();
            shopCarModel.setItemId(itemId);
            shopCarModel.setUserId(userId);
            shopCarModel.setAmount(amount);
            //判断promoId是否为空，若为空则设置为0代表无活动
            if(promoId == null){
                shopCarModel.setPromoId(0);
            }else {
                shopCarModel.setPromoId(promoId);
            }
            //判断是否有活动，如果有活动  且 活动正在进行中 则把价格设置成活动价
            if(itemModel.getPromoModel() != null && itemModel.getPromoModel().getStatus() == 2){
                shopCarModel.setItemPrice(itemModel.getPromoModel().getPromoItemPrice());
            }else {
                shopCarModel.setItemPrice(itemModel.getPrice());
            }
            //将Model转化为DO并插入数据库
            ShopCarDO shopCarDO = this.convertShopCarDOFromShopCarModel(shopCarModel);
            shopCarDOMapper.insertSelective(shopCarDO);
        }else {
            //如果不为空，则该商品的数量增加
            shopCarModel.setAmount(shopCarModel.getAmount() + amount);
            System.out.println(shopCarModel.getShopcarId());
            //数据库中商品数量增加
            increaseAmount(shopCarModel.getShopcarId(),amount);

        }
        return shopCarModel;
    }

    private ShopCarDO convertShopCarDOFromShopCarModel(ShopCarModel shopCarModel){
        if(shopCarModel == null){
            return null;
        }
        ShopCarDO shopCarDO = new ShopCarDO();
        BeanUtils.copyProperties(shopCarModel,shopCarDO);
        shopCarDO.setPrice(shopCarModel.getItemPrice().doubleValue());
        return shopCarDO;
    }

    private ShopCarModel convertShopCarModelFromShopCarDO(ShopCarDO shopCarDO){
        if(shopCarDO == null){
            return null;
        }
        ShopCarModel shopCarModel = new ShopCarModel();
        BeanUtils.copyProperties(shopCarDO,shopCarModel);//和DO类的元素名称必须一样
        shopCarModel.setItemPrice(new BigDecimal(shopCarDO.getPrice()));
        return shopCarModel;
    }

    @Transactional
    @Override
    public void increaseAmount(Integer id,Integer amount){
        shopCarDOMapper.increaseAmount(id,amount);
    }


}
