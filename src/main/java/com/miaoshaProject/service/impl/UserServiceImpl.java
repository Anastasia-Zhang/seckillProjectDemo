package com.miaoshaProject.service.impl;

import com.miaoshaProject.dao.UserDOMapper;
import com.miaoshaProject.dao.UserPasswordDOMapper;
import com.miaoshaProject.dataobject.UserDO;
import com.miaoshaProject.dataobject.UserPasswordDO;
import com.miaoshaProject.service.UserService;
import com.miaoshaProject.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(id);

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
}
