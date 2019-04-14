package com.miaoshaProject.controllor;

import com.aliyuncs.exceptions.ClientException;
import com.miaoshaProject.controllor.viewobj.UserVO;
import com.miaoshaProject.error.BusinessException;
import com.miaoshaProject.error.EmBusinessError;
import com.miaoshaProject.reponse.CommonReturnType;
import com.miaoshaProject.service.UserService;
import com.miaoshaProject.service.model.UserModel;
import com.miaoshaProject.util.SendSms;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;


@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")//实现ajex跨域请求,但是没有办法做到session共享
public class UserController extends BaseController{
    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;//获取httpSession，其对应当前用户对应的请求


    //用户登录
    @RequestMapping(value = "/login",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType login(@RequestParam(name = "telphone") String telphone,
                                  @RequestParam(name = "password") String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //入参校验
        if(org.apache.commons.lang3.StringUtils.isEmpty(telphone)||
                StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //用户登录服务，校验用户登录是否合法
        UserModel userModel = userService.validatelogin(telphone, this.EncodeByMD5(password));//加密后的密码

        //将登录凭证加入到用户登录成功的session内
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userModel);

        return CommonReturnType.create(null);
    }

    //用户注册接口
    @RequestMapping(value = "/register",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name="telphone") String telphone,
                                     @RequestParam(name="otpCode")String otpCode,
                                     @RequestParam(name="name")String name,
                                     @RequestParam(name="gender")Integer gender,
                                     @RequestParam(name="age")Integer age,
                                     @RequestParam(name="password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //验证手机号和otp相符合
        //获取该手机号对应的验证码

        //System.out.println("registerURL: "+ httpServletRequest.getRequestURL());
        //System.out.println("此session的id为: "+httpServletRequest.getSession().getId());
        String inSessionOtpCode = (String) httpServletRequest.getSession().getAttribute(telphone);
        System.out.println(inSessionOtpCode);
        //看二者是否相等
        //System.out.println(inSessionOtpCode);
        boolean b = com.alibaba.druid.util.StringUtils.equals(otpCode,inSessionOtpCode);
        if(!b){//该类库里有判空处理
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"短信验证码不符合");
        }
        System.out.println("ok run");
        //用户注册流程
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setGender(new Byte(String.valueOf(gender.intValue())));
        userModel.setAge(age);
        userModel.setTelphone(telphone);
        userModel.setRegisterMode("byphone");
        userModel.setEncrptpassword(this.EncodeByMD5(password));//将密码进行加密
        userService.register(userModel);
        return CommonReturnType.create(null);
    }

    public String EncodeByMD5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //确定一个计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        //加密字符串
        String newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;

    }


    //用户获取otp短信接口
    @RequestMapping(value = "/getotp",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name="telphone") String telphone) throws ClientException {

        StringBuffer stringBuffer=new StringBuffer();
        for (int x=0;x<=5;x++) {
            int random = (int) (Math.random() * (10 - 1));
            stringBuffer.append(random);
        }
        String string = stringBuffer.toString();
        int otpCode = Integer.parseInt(string);
        //SendSms sendSms = new SendSms();
        SendSms.getMessage(telphone,otpCode);
        //此处暂时用httpSession绑定
        httpServletRequest.getSession().setAttribute(telphone,otpCode);
       // System.out.println("成功设置session,id为: "+httpServletRequest.getSession().getId());
        // System.out.println("设置session的value值为: "+(String)httpServletRequest.getSession().getAttribute(telphone));

        //将otp验证码通过短信通道发送给用户，省略
        System.out.println("telphone = " + telphone + " & otpCode = "+otpCode);//打印到控制台上，方便测试时使用
        return CommonReturnType.create(null);
    }

    @RequestMapping("/get")
    @ResponseBody//表示该方法的返回结果直接写入 HTTP response body 中
    public CommonReturnType getUser(@RequestParam(name="id") Integer id) throws BusinessException {
        //调用service服务获取对应的id的用户对象并返回给前端
        UserModel userModel = userService.getUserById(id);

        //若获取的用户信息不存在
        if(userModel == null)
        {
            //userModel.setEncrptpassword("1234");//测试，抛出空指针异常,测试未知错误码
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);//将枚举类型的错误信息(错误码：100001，错误信息:用户不存在)
        }

        //将核心领域模型用户对象转化为可供UI使用的viewobject
        UserVO userVO = convertFromModel(userModel);
        //返回通用对象
        return CommonReturnType.create(userVO);
    }

    private UserVO convertFromModel(UserModel userModel)
    {
        if(userModel == null){
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel,userVO);
        return userVO;
    }



}