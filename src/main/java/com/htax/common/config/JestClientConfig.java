package com.htax.common.config;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @description:jest注册类
 * @author:wangJ
 * @date: 2022/5/17 13:59
 */
@Component
public class JestClientConfig {

    @Value("${elasticsearch.url}")
    String esUrl;

    @Bean
    public io.searchbox.client.JestClient getJestClient(){
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig.Builder(esUrl).multiThreaded(true).build());
        return factory.getObject();
    }
}
