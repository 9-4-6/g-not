package com.example.gnote.design.strategy;

/**
 * @author guozhong
 * @date 2026/1/27
 * @description 支付
 */
public interface PaymentStrategy {
    boolean pay(double amount, String orderId);
    Integer getChannelType();
}
