package com.miaoshaProject.validator;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class ValidationResult {
    //检验结果
    private boolean hasErrors = false;
    //校验结果
    private Map<String,String> errorMsgMap = new HashMap<>();


    public boolean isHasErrors() {
        return hasErrors;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

    public Map<String, String> getErrorMsgMap() {
        return errorMsgMap;
    }

    public void setErrorMsgMap(Map<String, String> errorMsgMap) {
        this.errorMsgMap = errorMsgMap;
    }

    //实现公用的格式化字符串信息获取错误结果的msg方法
    public String getErrorMsg()
    {
       return StringUtils.join(errorMsgMap.values().toArray(),",");//将所有字段的错误信息连接起来并返回
    }
}
