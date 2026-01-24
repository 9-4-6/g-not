package com.example.gnote.spring.lifecycle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextAware;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.context.*;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringValueResolver;


/**
 * @author guozhong
 * @date 2026/1/23
 * @description
 */
public class MyLifecycleBean implements
        BeanNameAware, BeanClassLoaderAware, BeanFactoryAware,
                // 应用上下文 Aware 接口（ApplicationContext 环境生效）
                EnvironmentAware, EmbeddedValueResolverAware,
                ResourceLoaderAware, ApplicationEventPublisherAware,
                MessageSourceAware, ApplicationContextAware,
                // 初始化接口
                InitializingBean,
                // 销毁接口
                DisposableBean {

    // ---------------------- 第一步：Aware 接口（资源注入） ----------------------
    @Override
    public void setBeanName(String name) {
        System.out.println("【1】BeanNameAware#setBeanName —— 注入Bean名称");
        System.out.println("  → 当前Bean名称：" + name);
        System.out.println("  → 用途：根据Bean名称做差异化逻辑（如多数据源区分）");
        System.out.println("-------------------------------------------------");
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        System.out.println("【2】BeanClassLoaderAware#setBeanClassLoader —— 注入类加载器");
        System.out.println("  → 类加载器类型：" + classLoader.getClass().getSimpleName());
        System.out.println("  → 用途：手动加载自定义类（如第三方jar中的类）");
        System.out.println("-------------------------------------------------");
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("【3】BeanFactoryAware#setBeanFactory —— 注入Bean工厂");
        System.out.println("  → BeanFactory类型：" + beanFactory.getClass().getSimpleName());
        System.out.println("  → 用途：手动从容器获取其他Bean（如 beanFactory.getBean(\"xxx\")）");
        System.out.println("-------------------------------------------------");
    }

    @Override
    public void setEnvironment(Environment environment) {
        System.out.println("【4】EnvironmentAware#setEnvironment —— 注入环境变量");
        System.out.println("  → 读取配置：spring.profiles.active = " + environment.getProperty("spring.profiles.active", "默认环境"));
        System.out.println("  → 用途：根据环境（dev/test/prod）加载不同配置");
        System.out.println("-------------------------------------------------");
    }

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        System.out.println("【5】EmbeddedValueResolverAware#setEmbeddedValueResolver —— 注入占位符解析器");
        String resolved = resolver.resolveStringValue("${user.name:默认用户}");
        System.out.println("  → 解析占位符 ${user.name}：" + resolved);
        System.out.println("  → 用途：手动解析配置文件中的${}表达式");
        System.out.println("-------------------------------------------------");
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        System.out.println("【6】ResourceLoaderAware#setResourceLoader —— 注入资源加载器");
        boolean exists = resourceLoader.getResource("classpath:application.properties").exists();
        System.out.println("  → 检查classpath:application.properties是否存在：" + exists);
        System.out.println("  → 用途：加载文件、classpath等资源");
        System.out.println("-------------------------------------------------");
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        System.out.println("【7】ApplicationEventPublisherAware#setApplicationEventPublisher —— 注入事件发布器");
        publisher.publishEvent(new ApplicationEvent("自定义事件") {});
        System.out.println("  → 用途：发布Spring事件（如用户注册后发布事件，解耦业务）");
        System.out.println("-------------------------------------------------");
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        System.out.println("【8】MessageSourceAware#setMessageSource —— 注入国际化消息源");
        String msg = messageSource.getMessage("welcome", null, "默认欢迎语", null);
        System.out.println("  → 国际化消息：" + msg);
        System.out.println("  → 用途：多语言文案支持");
        System.out.println("-------------------------------------------------");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("【9】ApplicationContextAware#setApplicationContext —— 注入应用上下文");
        System.out.println("  → 上下文ID：" + applicationContext.getId());
        System.out.println("  → 用途：全局获取容器、批量获取某类型Bean");
        System.out.println("-------------------------------------------------");
    }



    // ---------------------- 第二步：BeanPostProcessor 前置处理 ----------------------
    // 注：该方法不在当前类，在自定义BeanPostProcessor中实现

    // ---------------------- 第三步：初始化逻辑（3种方式） ----------------------
    @PostConstruct
    public void postConstructMethod() {
        System.out.println("【12】@PostConstruct —— 注解式初始化方法");
        System.out.println("  → 优先级：高于自定义init-method，高于afterPropertiesSet");
        System.out.println("  → 优点：无Spring耦合，推荐使用");
        System.out.println("-------------------------------------------------");
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("【13】InitializingBean#afterPropertiesSet —— 框架内置初始化方法");
        System.out.println("  → 执行时机：Bean属性赋值完成后");
        System.out.println("  → 用途：初始化资源（如数据库连接、线程池）");
        System.out.println("-------------------------------------------------");
    }

    // 自定义init-method（在@Bean注解中配置）
    public void customInitMethod() {
        System.out.println("【14】自定义init-method —— 配置式初始化方法");
        System.out.println("  → 优先级：最低");
        System.out.println("  → 优点：完全解耦，可配置不同方法名");
        System.out.println("-------------------------------------------------");
    }

    // ---------------------- 第四步：BeanPostProcessor 后置处理 ----------------------
    // 注：该方法不在当前类，在自定义BeanPostProcessor中实现

    // ---------------------- 销毁阶段（3种方式） ----------------------
    @PreDestroy
    public void preDestroyMethod() {
        System.out.println("【销毁1】@PreDestroy —— 注解式销毁方法");
        System.out.println("  → 优先级：高于自定义destroy-method，低于DisposableBean#destroy");
        System.out.println("  → 用途：释放资源（如关闭连接、停止线程池）");
        System.out.println("-------------------------------------------------");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("【销毁2】DisposableBean#destroy —— 框架内置销毁方法");
        System.out.println("  → 执行时机：容器关闭时");
        System.out.println("  → 用途：框架标准的销毁逻辑");
        System.out.println("-------------------------------------------------");
    }

    // 自定义destroy-method（在@Bean注解中配置）
    public void customDestroyMethod() {
        System.out.println("【销毁3】自定义destroy-method —— 配置式销毁方法");
        System.out.println("  → 优先级：最低");
        System.out.println("  → 优点：完全解耦，可配置不同方法名");
        System.out.println("-------------------------------------------------");
    }
}


