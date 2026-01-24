package com.example.gnote;

import com.example.gnote.lock.CustomLock;
import com.example.gnote.spring.config.LifecycleConfig;
import com.example.gnote.spring.lifecycle.MyLifecycleBean;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class GNoteApplicationTests {
    @Test
    void springTest()throws InterruptedException{
       /* ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationfile.xml");
        System.out.println("context 启动成功");
        // 从 context 中取出我们的 Bean，而不是用 new MessageServiceImpl() 这种方式
        MessageService messageService = context.getBean(MessageService.class);
        // 这句将输出: hello world
        System.out.println(messageService.getMessage());*/

        // 启动注解配置的 Spring 上下文
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(LifecycleConfig.class);
        // 获取 Bean（触发初始化）
        MyLifecycleBean bean = context.getBean(MyLifecycleBean.class);
        System.out.println("=== Bean 初始化完成 ===");
        // 关闭上下文（可测试销毁逻辑，本文重点在初始化）
        context.close();
    }

    @Test
    void contextLoads() throws InterruptedException {

       /* Lock lock = new ReentrantLock();
        lock.lock();
        try {
            int a = 1+2;
            System.out.println(a);
        }finally {
            lock.unlock();
        }*/

        CustomLock lock = new CustomLock();
        CountDownLatch latch = new CountDownLatch(2);

        // 线程1获取锁
        new Thread(() -> {
            lock.lock();
            try {
                System.out.println("线程1获取自定义锁");
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                System.out.println("线程1释放自定义锁");
                latch.countDown();
            }
        }).start();

        // 线程2尝试获取锁（会阻塞，直到线程1释放）
        new Thread(() -> {
            lock.lock();
            try {
                System.out.println("线程2获取自定义锁");
            } finally {
                lock.unlock();
                System.out.println("线程2释放自定义锁");
                latch.countDown();
            }
        }).start();

        latch.await();
    }

}
