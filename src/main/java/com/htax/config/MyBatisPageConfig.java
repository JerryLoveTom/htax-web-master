package com.htax.config;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

import javax.servlet.MultipartConfigElement;

/**
 * @author ppzz
 * @date 2022-05-13 20:16
 */
@Component
@MapperScan("com.htax.modules.*.dao")
public class MyBatisPageConfig {

//    @Bean
//    public MybatisPlusInterceptor mybatisPlusInterceptor(){
//        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
//        // 设置乐观锁
//        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
//        // 设置分页
//        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
//        return  interceptor;
//    }
    @Bean
    public MultipartConfigElement multipartConfigElement(){
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 允许上传的最大值
        factory.setMaxFileSize(DataSize.of(50, DataUnit.MEGABYTES));
        factory.setMaxRequestSize(DataSize.of(50,DataUnit.MEGABYTES));
        return factory.createMultipartConfig();
    }
}
