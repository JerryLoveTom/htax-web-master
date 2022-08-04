package com.htax.common.aspect;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.htax.common.annotation.EsIndex;
import com.htax.common.annotation.SysLog;
import com.htax.common.utils.ElasticSearchUtils;
import okhttp3.internal.Internal;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: es索引切面
 * @author:wangJ
 * @date: 2022/5/17 11:05
 */
@Aspect
@Component
public class EsIndexAspect {
    @Resource
    private ElasticSearchUtils elasticSearchUtils;

    @Pointcut("@annotation(com.htax.common.annotation.EsIndex)")
    public void EsIndexPointCut() {
    }

    @Around("EsIndexPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;

        //索引数据
        indexData(point);
        return result;
    }

    private void indexData(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String index = "";
        String indexType = "";
        //注解上的描述
        EsIndex esIndex = method.getAnnotation(EsIndex.class);
        if (esIndex != null) {
            index = esIndex.index();
            indexType = esIndex.indexType();
        }
        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        //请求的参数
        Object[] args = joinPoint.getArgs();
        Map<String,Object> map = new HashMap<>();
//        Object target = joinPoint.getTarget().getClass().getName();
//        System.out.println(target);
//        String[] names  = ((CodeSignature)joinPoint.getSignature()).getParameterNames();
//        for (int i = 0; i < names.length ; i++) {
//            map.put(names[i],args[i]);
//        }
        Object object = args[0];
        String dd = object.getClass().getName();
        System.out.println(dd);
        Class c = object.getClass();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor p: propertyDescriptors) {
                String key = p.getName();
                if(!key.equals("class")){
                    Method getter = p.getReadMethod();
                    Object value = getter.invoke(object);
                    map.put(key,value);
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        elasticSearchUtils.updateIndexDoc(index,indexType,map);
    }
}
