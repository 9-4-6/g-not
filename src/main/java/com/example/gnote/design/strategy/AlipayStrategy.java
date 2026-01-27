package com.example.gnote.design.strategy;

import org.springframework.stereotype.Component;

/**
 * @author guozhong
 * @date 2026/1/27
 * @description 阿里支付
 */
@Component
public class AlipayStrategy implements PaymentStrategy {
    @Override
    public boolean pay(double amount, String orderId) {
        System.out.printf("[支付宝] 支付 %.2f 元，订单号：%s%n", amount, orderId);
        return true;
    }

    @Override
    public Integer getChannelType() {
        return 2;
    }

}
