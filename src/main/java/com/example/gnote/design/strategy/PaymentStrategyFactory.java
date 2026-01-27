package com.example.gnote.design.strategy;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author guozhong
 * @date 2026/1/27
 * @description 策略工厂
 */
@Component
public class PaymentStrategyFactory {
    @Resource
    private List<PaymentStrategy> paymentStrategyList;
    private static Map<Integer, PaymentStrategy> strategies = new HashMap<>();
    @PostConstruct
    public void init() {
        strategies = paymentStrategyList.stream().collect(Collectors.toMap(PaymentStrategy::getChannelType, Function.identity()));
    }

    public PaymentStrategy create(Integer channel) {
        PaymentStrategy strategy = strategies.get(channel);
        if (strategy == null) {
            throw new IllegalArgumentException("不支持的支付渠道: " + channel);
        }
        return strategy;
    }
}
