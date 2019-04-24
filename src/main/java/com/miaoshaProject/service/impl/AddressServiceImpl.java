package com.miaoshaProject.service.impl;

import com.miaoshaProject.dao.UserAddressDOMapper;
import com.miaoshaProject.dataobject.UserAddressDO;
import com.miaoshaProject.error.BusinessException;
import com.miaoshaProject.error.EmBusinessError;
import com.miaoshaProject.service.AddressService;
import com.miaoshaProject.service.ValidateService;
import com.miaoshaProject.service.model.AddressModel;
import com.miaoshaProject.validator.ValidationResult;
import com.miaoshaProject.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private ValidatorImpl validator;

    @Autowired
    private UserAddressDOMapper userAddressDOMapper;

    @Autowired
    private ValidateService validateService;

    @Override
    @Transactional
    public void addAddress(AddressModel addressModel) throws BusinessException {
        if(addressModel == null){
            throw  new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"添加地址出错请重新添加");
        }
        ValidationResult result = validator.validate(addressModel);
        if(result.isHasErrors()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrorMsg());
        }
        UserAddressDO  userAddressDO = this.convertFromModel(addressModel);
        userAddressDOMapper.insertSelective(userAddressDO);

    }

    @Override
    public AddressModel getAddressInfoById(Integer id) {
       UserAddressDO userAddressDO = userAddressDOMapper.selectByPrimaryKey(id);
       AddressModel addressModel = this.convertFromDO(userAddressDO);
       return addressModel;
    }

    @Override
    public List<AddressModel> listAddress(Integer userId) throws BusinessException {
        validateService.userValidate(userId);
        List<UserAddressDO> addressDOList = userAddressDOMapper.selectByUserId(userId);
        List<AddressModel> addressModelList = addressDOList.stream().map(userAddressDO -> {
            AddressModel addressModel = this.convertFromDO(userAddressDO);
            return addressModel;
        }).collect(Collectors.toList());
        return addressModelList;
    }

    @Override
    public void delAddress(Integer userId,Integer addressId) throws BusinessException {
        validateService.userValidate(userId);
        AddressModel addressModel = this.getAddressInfoById(addressId);
        if (addressModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"地址不存在");
        }
        userAddressDOMapper.deleteByPrimaryKey(addressId);
    }

    @Override
    @Transactional
    public void updateAddress(Integer userId, AddressModel addressModel) throws BusinessException {
        validateService.userValidate(userId);
        if(addressModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"地址信息参数错误");
        }
        UserAddressDO userAddressDO = convertFromModel(addressModel);
        userAddressDOMapper.updateByPrimaryKeySelective(userAddressDO);

    }


    private UserAddressDO convertFromModel(AddressModel addressModel){
        if(addressModel == null){
            return null;
        }
        UserAddressDO userAddressDO = new UserAddressDO();
        BeanUtils.copyProperties(addressModel,userAddressDO);
        return userAddressDO;
    }

    private AddressModel convertFromDO(UserAddressDO userAddressDO){
        if(userAddressDO == null){
            return  null;
        }
        AddressModel addressModel = new AddressModel();
        BeanUtils.copyProperties(userAddressDO,addressModel);
        return addressModel;
    }
}
