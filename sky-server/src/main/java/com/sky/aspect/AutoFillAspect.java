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
 * 自定义切面，实现自动填充功能
 * @author SXBai
 * @create 2026-04-12-1:47
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)" )
    public void autoFillPointCut(){}

    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){
        log.info("开始进行公共字段自动填充...");

        //获取当前被拦截的方法上的数据库操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill autofill = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autofill.value();

        //获取到当前被拦截到的方法的参数--实体对象
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0){
            return;
        }

        Object enity=args[0];

        //准备赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long currendID= BaseContext.getCurrentId();


        //通过当前不同的操作类型，使用反射为对象对应的属性进行赋值
        if(OperationType.INSERT.equals(operationType)){
            //插入方法，为四个公共字段赋值
            try {
                Method setCreatTime=enity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setUpdateTime=enity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setCrearUser=enity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateUser=enity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                //通过反射为对象赋值
                setCreatTime.invoke(enity,now);
                setUpdateTime.invoke(enity,now);
                setCrearUser.invoke(enity,currendID);
                setUpdateUser.invoke(enity,currendID);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else if(OperationType.UPDATE.equals(operationType)){
            try {

                Method setUpdateUser=enity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                Method setUpdateTime=enity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);

                setUpdateUser.invoke(enity,currendID);
                setUpdateTime.invoke(enity,now);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

    }
}
