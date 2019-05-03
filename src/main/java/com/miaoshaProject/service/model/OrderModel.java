package com.miaoshaProject.service.model;

import org.joda.time.DateTime;

import java.math.BigDecimal;

//用户下单交易模型
public class OrderModel {
    //交易号
    private String id;

    private Integer userId;
    //下单时间
    private DateTime orderTime;

    private  Integer itemId;

    private String imgUrl;

    private String itemName;

    private Integer promoId;//若非空，则以商品秒杀方式下单

    //购买商品的单价，若promoId非空则表示秒杀商品价格
    private BigDecimal itemPrice;
    //购买数量
    private Integer amount;
    //总金额若promoId非空则表示秒杀商品价格
    private BigDecimal orderPrice;

    //订单物流状态表
    //订单地址
    private Integer addressId;
    //订单状态,0代表未支付,1代表已经支付，2代表运输，3代表订单完成
    private Integer orderStatus;

    private AddressModel addressModel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public DateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(DateTime orderTime) {
        this.orderTime = orderTime;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public AddressModel getAddressModel() {
        return addressModel;
    }

    public void setAddressModel(AddressModel addressModel) {
        this.addressModel = addressModel;
    }
}
