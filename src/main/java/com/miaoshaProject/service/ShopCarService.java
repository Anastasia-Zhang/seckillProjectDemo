package com.miaoshaProject.service;

import com.miaoshaProject.error.BusinessException;

import com.miaoshaProject.service.model.ShopCarModel;


public interface ShopCarService {
    ShopCarModel addProductToCar(Integer userId, Integer itemId, Integer promoId, Integer amount) throws BusinessException;
    //购物车数量增加
    void increaseAmount(Integer id,Integer amout);
}
