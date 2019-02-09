package com.miaoshaProject.service;

import com.miaoshaProject.error.BusinessException;
import com.miaoshaProject.service.model.OrderModel;

public interface OrderService {
    //1，通过前端Url上传过来秒杀活动id，然后下单接口内校验id是否属于对应商品且活动已经开始
    //2，直接再下单的接口内判断是否存在秒杀活动，若存在进行中的则以秒杀价格下单
    //一个商品可能存在多个秒杀活动，若平价商品，采取第二种方案的话接口进入则需要再判断一次是都有秒杀活动损耗性能，因此选择第一种方案
    OrderModel createOreder(Integer userId,Integer itemId,Integer promoId,Integer amount) throws BusinessException;

}
