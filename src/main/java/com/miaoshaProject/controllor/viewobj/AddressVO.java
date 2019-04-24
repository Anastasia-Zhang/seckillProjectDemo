package com.miaoshaProject.controllor.viewobj;

import javax.validation.constraints.NotBlank;

public class AddressVO {
    private Integer addressId;
    private Integer userId;

    private String addressDetail;

    private String zipCode;

    private String rcvdName;

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
