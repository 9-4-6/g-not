package com.example.gnote;

import com.example.gnote.lock.CustomLock;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class GNoteApplicationTests {

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
