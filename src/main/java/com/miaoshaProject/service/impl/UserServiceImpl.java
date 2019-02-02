package com.miaoshaProject.service.impl;

import com.miaoshaProject.dao.UserDOMapper;
import com.miaoshaProject.dao.UserPasswordDOMapper;
import com.miaoshaProject.dataobject.UserDO;
import com.miaoshaProject.dataobject.UserPasswordDO;
import com.miaoshaProject.error.BusinessException;
import com.miaoshaProject.error.EmBusinessError;
import com.miaoshaProject.service.UserService;
import com.miaoshaProject.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;

@Service
public class UserServiceImpl implements UserService {

   @Autowired
   private UserDOMapper userDOMapper;

    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;

    @Override
    public UserModel getUserById(Integer id) {
        //调用userdomapper获得到对用的用户dataobject
       //不能返回给前端UserDO，UserDO是最简单的类数据库有什么字段该类就有那些成员变量
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);
        if(userDO == null){
            return  null;
        }
        //通过用户id获取对应的用户加密密码信息
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(id);//新加方法

        return convertFromDataObject(userDO,userPasswordDO);
    }


    //通过userdo和userpasswordDO组装成Usermodel对象
    private UserModel convertFromDataObject(UserDO userDO, UserPasswordDO userPasswordDO)
    {
        if(userDO==null){
            return  null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO,userModel);//将userdo的属性值copy到usermodel内，字段名一致并且类型名一致
        if(userPasswordDO != null)
        {
            userModel.setEncrptpassword(userPasswordDO.getEncrptPassword());//因为两个类的id属性重合，将userpasswordDO的属性赋值给userModel

        }

        return  userModel;
    }

    @Override
    @Transactional//必须完成所有字段的插入
    public void register(UserModel userModel) throws BusinessException{
        //判空处理，判断是否为空
        if(userModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //判断字段是否为空
        if(!StringUtils.isNoneEmpty(userModel.getName())
                || userModel.getGender() == null
                ||userModel.getAge()== null
                ||StringUtils.isEmpty(userModel.getTelphone()) ){
            //return cs == null || cs.length() == 0;
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //注册
        //实现modol->dataobject 方法
        UserDO userDO = convertFromModel(userModel);
        //捕捉唯一索引重复异常
//        userDOMapper.insertSelective(userDO);
        try{
            userDOMapper.insertSelective(userDO);
        }catch(DuplicateKeyException ex){
            System.out.println("异常已经捕获");
            BusinessException bex = new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"手机号重复");
            System.out.println(bex.getErrCode());
            throw bex;
        }

        System.out.println("user_info is inserted");
        //存入数据库,insertSelective判空处理若是不为空则直接跳过此字段，此方法完全依赖于数据库的默认值
        //在数据库设计中要避免使用null：1.java在处理空指针非常脆弱 2.null值没有意义

        //密码
        //获取数据库的id值作为user_password的外键插入
        userModel.setId(userDO.getId());
        UserPasswordDO userPasswordDO = convertPasswordFromModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);//将密码存入数据库的密码表
        System.out.println("password information is inserted");

        return;

    }

    @Override
    public UserModel validatelogin(String telphone, String encrptPassword) throws BusinessException {
        //通过用户的手机获取用户信息
        UserDO userDO = userDOMapper.selectByTelphone(telphone);
        //用户登录错误：密码或用户名错误
        if(userDO == null){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAILD);
        }
        //根据用户id获得相关密码信息
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        //将二者拼装成UserModel
        UserModel userModel = convertFromDataObject(userDO,userPasswordDO);

        //比对用户信息内加密的密码是否和传输进来的密码相匹配

        if(!StringUtils.equals(encrptPassword,userModel.getEncrptpassword())){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAILD);
        }
        return userModel;
    }

    private UserDO convertFromModel(UserModel userModel){
        if(userModel == null){
            return  null;
        }
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel,userDO);
        return userDO;

    }

    private UserPasswordDO convertPasswordFromModel(UserModel userModel){
        if(userModel == null)
        {
            return null;
        }
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncrptPassword(userModel.getEncrptpassword());
        userPasswordDO.setUserId(userModel.getId());
        return  userPasswordDO;

    }
}
