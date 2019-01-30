package com.miaoshaProject.error;
//处理错误信息。统一管理错误码
public interface CommonError {
    public int getErrCode();
    public String getErrMsg();
    public CommonError setErrMsg(String errMsg);
}
