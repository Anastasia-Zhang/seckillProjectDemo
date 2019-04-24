package com.miaoshaProject.service;

import com.miaoshaProject.error.BusinessException;

import com.miaoshaProject.service.model.ShopCarModel;

import java.util.List;


public interface ShopCarService {
    //数据库持久化加入购物车
    ShopCarModel addProductToCar(Integer userId, Integer itemId, Integer promoId, Integer amount) throws BusinessException;
    ShopCarModel addShopCar(Integer userId, Integer itemId, Integer promoId, Integer amount) throws BusinessException;
    //商品数量增加
    void updateAmount(Integer userId,Integer ItemId,Integer amout) throws BusinessException;
    //购物车指定用户购物车浏览
    List<ShopCarModel> getShopCar(Integer userId) throws BusinessException;
    //删除商品
    void delItem(Integer userId,Integer itemId) throws BusinessException;
}
