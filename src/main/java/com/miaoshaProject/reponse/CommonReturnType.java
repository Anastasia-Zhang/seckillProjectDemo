package com.miaoshaProject.reponse;
//处理返回信息，返回jason的序列化对象，供前端解析使用
public class CommonReturnType {
    //http statueserror 500 404 200
    //表明对应请求的返回处理结果 success或fail
    private String status;
    //若status=success 则data内返回前端需要的json数据
    //若status=fail 则data使用通用的错误码格式，让前端返回有意义的错误信息
    private Object data;

    //定义一个通用的创建方法
    //函数重载，若不带有任何的status，只有返回结果result，则默认为success
    public static CommonReturnType create(Object result)
    {
        return CommonReturnType.create(result,"success");
    }
    //处理带有返回结果的消息，创建通用返回类型对象并初始化，返回该对象
    public static CommonReturnType create(Object result,String status){
        CommonReturnType type = new CommonReturnType();
        type.setStatus(status);
        type.setData(result);
        return type;

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
