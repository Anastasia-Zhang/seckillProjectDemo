package com.miaoshaProject.service;

import com.miaoshaProject.error.BusinessException;
import com.miaoshaProject.service.model.AddressModel;

import java.util.List;


public interface AddressService {
    void addAddress(AddressModel AddressModel) throws BusinessException;
    AddressModel getAddressInfoById(Integer id);
    List<AddressModel> listAddress(Integer userId) throws BusinessException;
    void delAddress(Integer userId,Integer addressId) throws BusinessException;
    void updateAddress(Integer userId,AddressModel addressModel) throws BusinessException;
}
