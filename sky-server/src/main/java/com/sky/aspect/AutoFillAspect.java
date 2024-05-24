package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 切面类=切入点+通知
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    /**
     * 定义切入点表达式，用来匹配目标方法
     */
    @Pointcut("@annotation(com.sky.annotation.AutoFill)")  //自定义注解
    public void opt(){}

    /**
     * 定义通知
     * @param joinPoint
     */
    @Before("opt()")
    public void autoFill(JoinPoint joinPoint){
        log.info("开始进行公共字段进行自动填充...");
        //获取当前被拦截的方法上数据库的操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature(); //方法签名对象
        log.info("被拦截到的方法：{}",signature.getMethod().getName());
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//获取该方法上面的注解对象
        OperationType operationType = autoFill.value();  // 获取注解中数据库操作类型
        Object[] args = joinPoint.getArgs();  //获取当前被拦截的方法的参数-实体对象
        if(args==null || args.length==0){
            return;
        }
        Object entity=args[0];
        //准备赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();
        //根据不同的数据库操作类型，为不同的字段进行赋值
        if(operationType==OperationType.INSERT){
            //如果是插入操作，通过反射为四个字段进行赋值
            try {
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                //通过反射为对象属性进行赋值
                setUpdateTime.invoke(entity,now);
                setCreateUser.invoke(entity,currentId);
                setUpdateUser.invoke(entity,currentId);
                setCreateTime.invoke(entity,now);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }else if(operationType==OperationType.UPDATE){
            try {
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                //通过反射为对象属性进行赋值
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }
}
