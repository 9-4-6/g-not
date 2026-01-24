package com.example.gnote.spring.lifecycle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author guozhong
 * @date 2026/1/23
 * @description 生命周期bean处理器
 */
public class MyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof MyLifecycleBean) {
            System.out.println("【11】BeanPostProcessor#postProcessBeforeInitialization —— 初始化前置处理");
            System.out.println("  → 处理Bean：" + beanName);
            System.out.println("  → 用途：修改Bean属性、解析@Autowired注解、生成代理前增强");
            System.out.println("-------------------------------------------------");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof MyLifecycleBean) {
            System.out.println("【15】BeanPostProcessor#postProcessAfterInitialization —— 初始化后置处理");
            System.out.println("  → 处理Bean：" + beanName);
            System.out.println("  → 用途：生成AOP代理、最终修改Bean实例（初始化最后一步）");
            System.out.println("-------------------------------------------------");
        }
        return bean;
    }
}
