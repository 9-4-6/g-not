package com.example.gnote.design.strategy;

import org.springframework.stereotype.Component;

/**
 * @author guozhong
 * @date 2026/1/27
 * @description 微信支付
 */
@Component
public class WechatPayStrategy implements PaymentStrategy {
    @Override
    public boolean pay(double amount, String orderId) {
        System.out.printf("[微信支付] 支付 %.2f 元，订单号：%s%n", amount, orderId);
        return true;
    }

    @Override
    public Integer getChannelType() {
        return 1;
    }
}
