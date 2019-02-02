package com.miaoshaProject.error;
//包装器业务异常实现
//自定义异常，发成错误抛出改异常，并由springboot handler处理
public class BusinessException  extends  Exception implements CommonError{

    private CommonError commonError;

    //直接接受EmBusinessError的传参用于构造业务异常
    public BusinessException(CommonError commonError){
        super();//调用父类的构造函数，父类的构造函数带有一些自己的初始化
        this.commonError = commonError;
    }
    //接收自定义的errMsg的方式构造业务异常
    public BusinessException(CommonError commonError,String errMesg)
    {
        super();
        this.commonError = commonError;
        this.commonError.setErrMsg(errMesg);
    }
    @Override
    public int getErrCode() {
        return this.commonError.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.commonError.setErrMsg(errMsg);
        return this;
    }
}
