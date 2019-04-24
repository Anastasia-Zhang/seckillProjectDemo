package com.miaoshaProject.service.model;

import javax.validation.constraints.NotBlank;

public class AddressModel {
    private Integer addressId;
    private Integer userId;
    @NotBlank(message = "详细地址必填")
    private String addressDetail;
    @NotBlank(message = "邮政编码必填")
    private String zipCode;
    @NotBlank(message = "收件人必填")
    private String rcvdName;
    @NotBlank(message = "手机号不能为空")
    private String rcvdTel;

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getRcvdName() {
        return rcvdName;
    }

    public void setRcvdName(String rcvdName) {
        this.rcvdName = rcvdName;
    }

    public String getRcvdTel() {
        return rcvdTel;
    }

    public void setRcvdTel(String rcvdTel) {
        this.rcvdTel = rcvdTel;
    }
}
