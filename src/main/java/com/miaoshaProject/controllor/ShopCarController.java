package com.miaoshaProject.controllor;

import com.miaoshaProject.error.BusinessException;
import com.miaoshaProject.reponse.CommonReturnType;
import com.miaoshaProject.service.ShopCarService;
import com.miaoshaProject.service.model.ShopCarModel;
import com.miaoshaProject.service.model.UserModel;
import com.miaoshaProject.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller("shopCar")
@RequestMapping("/shopCar")
@CrossOrigin(origins = {"*"},allowCredentials = "true")
public class ShopCarController extends BaseController{

    @Autowired
    private ShopCarService shopCarService;

    @Autowired
    private RedisUtils redisUtils;


    //下单请求
    @RequestMapping(value = "/addShopCar",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createOrder(@RequestParam(name = "itemId")Integer itemId,
                                        @RequestParam(name = "promoId",required = false)Integer promoId,
                                        @RequestParam(name = "amount")Integer amount) throws BusinessException {

        //判断用户是否登录
        UserModel userModel = validateUserLogin();
        //下单
        ShopCarModel shopCarModel = shopCarService.addProductToCar(userModel.getId(),itemId,promoId,amount);
        redisUtils.set(String.valueOf(shopCarModel.getShopcarId()),shopCarModel);

        return CommonReturnType.create(null);
    }
}
