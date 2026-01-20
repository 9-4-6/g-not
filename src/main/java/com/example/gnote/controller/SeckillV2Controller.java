package com.example.gnote.controller;

import jakarta.annotation.Resource;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/v2")
public class SeckillV2Controller {
    @Resource
    private  StringRedisTemplate stringRedisTemplate;
    @Resource
    private  RedissonClient redissonClient;

    @GetMapping("/seckill")
    public String seckill() {
        long goodsId = 1001;

        // 1. 定义锁key和库存key
        String lockKey = "seckill:lock:" + goodsId; // 商品维度锁
        String stockKey = "seckill:stock:" + goodsId;

        // 2. 获取分布式锁（设置30秒自动释放，避免死锁）
        RLock lock = redissonClient.getLock(lockKey);
        try {
            // 尝试获取锁：最多等1秒，持有锁最多30秒
            boolean locked = lock.tryLock(1, 30, TimeUnit.SECONDS);
            if (!locked) {
                return "秒杀太火爆，请稍后再试";
            }
            // 查库存+扣库存（锁内原子执行）
            String stockStr = stringRedisTemplate.opsForValue().get(stockKey);
            if (stockStr == null) {
                return "商品不存在";
            }
            int stock = Integer.parseInt(stockStr);
            if (stock <= 0) {
                return "秒杀已结束（库存不足）";
            }

            stringRedisTemplate.opsForValue().set(stockKey, String.valueOf(stock - 1));
            // 模拟生成订单
            String orderId = "order_" + System.currentTimeMillis() + "_" + goodsId ;
            return "秒杀成功！订单号：" + orderId + "，剩余库存：" + (stock - 1);
        } catch (InterruptedException e) {
            return "秒杀失败，请重试";
        } finally {
            // 释放锁（仅持有锁的线程释放）
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
