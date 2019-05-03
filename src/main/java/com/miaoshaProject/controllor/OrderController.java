package com.miaoshaProject.controllor;


import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miaoshaProject.controllor.viewobj.OrderVO;
import com.miaoshaProject.controllor.viewobj.ShopCarProductVO;
import com.miaoshaProject.error.BusinessException;
import com.miaoshaProject.error.EmBusinessError;
import com.miaoshaProject.reponse.CommonReturnType;
import com.miaoshaProject.service.OrderService;
import com.miaoshaProject.service.model.OrderModel;
import com.miaoshaProject.service.model.ShopCarModel;
import com.miaoshaProject.service.model.UserModel;
import org.apache.ibatis.annotations.Param;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller("order")
@RequestMapping("/order")
@CrossOrigin(origins = {"*"},allowCredentials = "true")
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    //用户直接从下单请求
    @RequestMapping(value = "/createorder",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createOrder(@RequestParam(name = "itemId")Integer itemId,
                                        @RequestParam(name = "promoId",required = false)Integer promoId,
                                        @RequestParam(name = "amount")Integer amount) throws BusinessException {

        //判断用户是否登录
        UserModel userModel = validateUserLogin();
        //下单
        OrderModel orderModel = orderService.createOrder(userModel.getId(),itemId,promoId,amount);

        return CommonReturnType.create(null);
    }

    //用户从购物车下单请求
    @RequestMapping(value = "/createshopCarorder",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType shopCarOrder(@RequestParam(name = "orderItemList")String  orderItemList) throws BusinessException, IOException {
        if(orderItemList == null ||  orderItemList.equals("")) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"请选择商品或无下单商品");
        }
        UserModel userModel = validateUserLogin();
        //jackson对象
        ObjectMapper mapper = new ObjectMapper();
        //使用jackson将json转为List
        JavaType jt = mapper.getTypeFactory().constructParametricType(List.class, ShopCarProductVO.class);
        List<ShopCarProductVO> VOList =  mapper.readValue(orderItemList, jt);

        //将VO转成Model
        List<ShopCarModel> shopCarModelList = VOList.stream().map(shopCarProductVO -> {
            ShopCarModel shopCarModel = this.convertModelFromVO(shopCarProductVO);
            return shopCarModel;
        }).collect(Collectors.toList());

        //下单
        List<OrderModel> orderModelList = orderService.createOrderShopCar(userModel.getId(),shopCarModelList);
        return CommonReturnType.create(orderModelList);

    }

    @RequestMapping(value = "confirmOrder",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType confirmOrder(@RequestParam(name = "orderIdList") String orderIdList) throws BusinessException {
        UserModel userModel = validateUserLogin();
        if(orderIdList == null || orderIdList.equals("")){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        List<OrderModel> orderModelList = orderService.showOrderForConfirm(userModel.getId(),orderIdList);
        List<OrderVO> orderVOList = orderModelList.stream().map(orderModel -> {
            OrderVO orderVO = this.convertOrderVOFrmoMoedel(orderModel);
            return orderVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(orderVOList);
    }

    @RequestMapping(value = "orderAddress",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType orderAddress(@RequestParam(name = "addressId") Integer addressId,
                                         @RequestParam(name = "orderIdList")String orderList) throws BusinessException {
        UserModel userModel = validateUserLogin();
        if(addressId == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        orderService.addOrderAddress(userModel.getId(),addressId,orderList);
        return CommonReturnType.create(null);
    }

    @RequestMapping(value = "showOrder",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType showOrder() throws BusinessException {
        UserModel userModel = validateUserLogin();
        List<OrderModel> orderModelList = orderService.getOrderByUserId(userModel.getId());
        return CommonReturnType.create(orderModelList);
    }

    @RequestMapping(value = "delOrder",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType delOrder(@RequestParam(name = "orderId") String orderId) throws BusinessException {
        UserModel userModel = validateUserLogin();
        orderService.delOrder(orderId);
        return CommonReturnType.create(null);
    }


    private ShopCarModel convertModelFromVO(ShopCarProductVO shopCarProductVO){
        if(shopCarProductVO == null){
            return null;
        }
        ShopCarModel shopCarModel = new ShopCarModel();
        BeanUtils.copyProperties(shopCarProductVO,shopCarModel);
        return shopCarModel;
    }

    private OrderVO convertOrderVOFrmoMoedel(OrderModel orderModel){
        if(orderModel == null){
            return null;
        }
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orderModel,orderVO);
        orderVO.setOrderTime(orderModel.getOrderTime().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
        return orderVO;
    }

}
