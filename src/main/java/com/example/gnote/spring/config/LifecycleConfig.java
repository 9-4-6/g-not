package com.example.gnote.spring.config;

import com.example.gnote.spring.lifecycle.MyBeanPostProcessor;
import com.example.gnote.spring.lifecycle.MyLifecycleBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author guozhong
 * @date 2026/1/23
 * @description 生命周期配置文件
 */
@Configuration
public class LifecycleConfig {
    // 注册测试Bean，指定init-method和destroy-method
    @Bean(initMethod = "customInitMethod", destroyMethod = "customDestroyMethod")
    public MyLifecycleBean myLifecycleBean() {
        return new MyLifecycleBean();
    }

    // 注册自定义 BeanPostProcessor
    @Bean
    public MyBeanPostProcessor myBeanPostProcessor() {
        return new MyBeanPostProcessor();
    }
}
