package com.miaoshaProject.error;

public enum EmBusinessError implements CommonError {
    //10001 通用错误类型
    PARAMETER_VALIDATION_ERROR(10001,"参数不合法"),
    //00002 未知错误
    UNKNOWN_ERROR(10002,"未知错误"),
    //20000开头为用户信息相关定义
    USER_NOT_EXIST(20001,"用户不存在"),


    ;

    private EmBusinessError(int errCode,String errMsg)
    {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    private int errCode;
    private String errMsg;
    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    //有一个通用错误类型，但是在不同场景下通用错误类型不同，如用户名没有输入、邮箱格式不合法等等都属于参数类型不合法此时需要定义一个接口接受不同实现（多态）
    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;//返回当前对象引用
    }


}
