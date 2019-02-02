package com.miaoshaProject.validator;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

@Component//容器、bean
public class ValidatorImpl implements InitializingBean {

    private Validator validator;

    //实现校验方法并返回校验结果
    public ValidationResult validate(Object bean)
    {
        final ValidationResult result = new ValidationResult();
        Set<ConstraintViolation<Object>> constraintViolationSet = validator.validate(bean);//将bean的校验错误值返回set集合里
        if(constraintViolationSet.size()>0){
            //有错误
            result.setHasErrors(true);
            //遍历set获取错误信息,遍历每一个元素ConstraintViolation的message
            constraintViolationSet.forEach(constraintViolation->{//java8 forEach lamdabiao表达式
                String errMsg = constraintViolation.getMessage();//错误信息
                String propertyName = constraintViolation.getPropertyPath().toString();//字段错误名称
                result.getErrorMsgMap().put(propertyName,errMsg);
            });
        }

        return result;
    }

    //springbean初始化完成之后会回调validatorImpl的afterPropertiesSet方法，实例化validator
    @Override
    public void afterPropertiesSet() throws Exception {
        //将hibernate validator 通过工厂化的方式使其实例化
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }
}
