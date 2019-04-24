package com.miaoshaProject.service;

import com.miaoshaProject.error.BusinessException;

public interface ValidateService {
   void userAndItemValidate(Integer userId, Integer itemId, Integer promoId, Integer amount) throws BusinessException ;
   void userValidate(Integer userId) throws  BusinessException ;
   void itemValidate(Integer itemId) throws  BusinessException ;

}

