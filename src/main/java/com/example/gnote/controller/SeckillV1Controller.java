package com.example.gnote.controller;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class SeckillV1Controller {
    @Resource
    private  StringRedisTemplate stringRedisTemplate;
    @GetMapping("/seckill")
    public String seckill() {
        // 拼接Redis库存key
        String stockKey = "seckill:stock:" + 1001;
        // 获取当前库存
        String stockStr = stringRedisTemplate.opsForValue().get(stockKey);
        if (stockStr == null) {
            return "商品不存在";
        }
        int stock = Integer.parseInt(stockStr);
        // 判断库存是否充足
        if (stock <= 0) {
            return "秒杀已结束（库存不足）";
        }
        // 关键：加50ms延迟，放大并发竞争
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 扣减库存（非原子操作，高并发会超卖）
        stringRedisTemplate.opsForValue().set(stockKey, String.valueOf(stock - 1));
        String orderId = "order_" + System.currentTimeMillis() + "_" + 1001;
        return "秒杀成功！订单号：" + orderId + "，剩余库存：" + (stock - 1);
    }

}
