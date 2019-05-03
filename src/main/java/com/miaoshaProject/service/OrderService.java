package com.miaoshaProject.service;

import com.miaoshaProject.error.BusinessException;
import com.miaoshaProject.service.model.AddressModel;
import com.miaoshaProject.service.model.OrderModel;
import com.miaoshaProject.service.model.ShopCarModel;

import java.util.List;

public interface OrderService {
    //1，通过前端Url上传过来秒杀活动id，然后下单接口内校验id是否属于对应商品且活动已经开始
    //2，直接再下单的接口内判断是否存在秒杀活动，若存在进行中的则以秒杀价格下单
    //一个商品可能存在多个秒杀活动，若平价商品，采取第二种方案的话接口进入则需要再判断一次是都有秒杀活动损耗性能，因此选择第一种方案
    OrderModel createOrder(Integer userId,Integer itemId,Integer promoId,Integer amount) throws BusinessException;
    //从购物车下订单
    List<OrderModel> createOrderShopCar(Integer userId,List<ShopCarModel> shopCarModelList) throws BusinessException;
    //展示未确定地址订单
    List<OrderModel> showOrderForConfirm(Integer userId,String orderIdList) throws BusinessException;
    //添加订单地址
    void addOrderAddress(Integer userId,Integer addressId,String orderIdList) throws BusinessException;
    //查看用户所有订单
    List<OrderModel> getOrderByUserId(Integer userId);
    //删除订单
    void delOrder(String orderId);

}
