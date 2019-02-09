package com.miaoshaProject.service;

import com.miaoshaProject.error.BusinessException;
import com.miaoshaProject.service.model.OrderModel;

public interface OrderService {
    OrderModel createOreder(Integer userId,Integer itemId,Integer amount) throws BusinessException;

}
