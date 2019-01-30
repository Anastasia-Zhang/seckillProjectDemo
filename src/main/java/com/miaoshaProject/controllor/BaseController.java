package com.miaoshaProject.controllor;

import com.miaoshaProject.error.BusinessException;
import com.miaoshaProject.error.EmBusinessError;
import com.miaoshaProject.reponse.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class BaseController {
    //定义exceptionhandler解决未被controller层吸收的异常
    // controller层是链接view层最后的一层，若发生在该层的的异常没有没处理，那么对于用户体验并不是很好
    //因此定义一种处理方式，使发生在controller层的异常得到处理，转化为用户可以接受且看的懂得界面，优化用户响应方式
    @ExceptionHandler(Exception.class)//捕获异常
    @ResponseStatus(HttpStatus.OK)//200 并非是业务逻辑异常，因此返回status ok
    @ResponseBody//注解的作用是将controller的方法返回的对象通过适当的转换器转换为指定的格式之后，写入到response对象的body区，通常用来返回JSON数据或者是XML
    //数据，需要注意的呢，在使用此注解之后不会再走试图处理器，而是直接将数据写入到输入流中，他的效果等同于通过response对象输出指定格式的数据。
    public Object handlerException(HttpServletRequest request, Exception ex){
        Map<String,Object> responseData = new HashMap<>();//解析异常数据,异常错误码和异常信息
        if(ex instanceof BusinessException){
            BusinessException businessException = (BusinessException)ex;//强行向上转型


            responseData.put("errCode",businessException.getErrCode());//获取传入的CommonError类型的错误码：100001
            responseData.put("errMsg",businessException.getErrMsg());//获取传入的CommonError类型的错信息：用户对象不存在

        }else{

            responseData.put("errCode", EmBusinessError.UNKNOWN_ERROR.getErrCode());//获取传入的CommonError类型的错误码：100001
            responseData.put("errMsg",EmBusinessError.UNKNOWN_ERROR.getErrMsg());//获取传入的CommonError类型的错信息：用户对象不存在

        }
        return CommonReturnType.create(responseData,"fail");//若直接把枚举传递过来，responseBody所默认的json数据序列化方式直接变成UNKNOWN_ERROR，得不到errCode和errMsg方式


        /*CommonReturnType commonReturnType = new CommonReturnType();
        commonReturnType.setStatus("fail");//定义返回状态值
        commonReturnType.setData(responseData);
        return commonReturnType;*/
    }
}
