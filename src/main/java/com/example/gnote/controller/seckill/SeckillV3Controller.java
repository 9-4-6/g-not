package com.example.gnote.controller.seckill;

import com.example.gnote.pojo.Result;
import com.example.gnote.pojo.ResultData;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/v3")
public class SeckillV3Controller {
    private final StringRedisTemplate stringRedisTemplate;

    // 原子执行“查库存+扣库存”
    private static final String SECKILL_LUA_SCRIPT = """
            -- 参数说明：
            -- KEYS[1] = 库存key
            -- ARGV[1] = 扣减的库存数（固定为1）
            -- 1. 判断库存是否充足
            local stock = tonumber(redis.call('get', KEYS[1]));
            if not stock or stock <= 0 then
                return 1; -- 1=库存不足
            end
            -- 2. 扣减库存
            redis.call('decr', KEYS[1]);
            return 2; -- 2=秒杀成功
            """;

    // 初始化Lua脚本对象
    private final DefaultRedisScript<Long> seckillScript = new DefaultRedisScript<>();

    public SeckillV3Controller(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        seckillScript.setScriptText(SECKILL_LUA_SCRIPT);
        seckillScript.setResultType(Long.class);
    }

    // 简化后的秒杀接口：仅需商品ID，无用户ID参数
    @GetMapping("/seckill")
    public Result<ResultData> seckill() {
        long goodsId = 1001;
        String stockKey = "seckill:stock:" + goodsId;
        Long result = stringRedisTemplate.execute(
                seckillScript,
                Collections.singletonList(stockKey), // KEYS数组：仅库存key
                "1" // ARGV参数：扣减数量（固定为1）
        );

        // 空值校验
        if (result == null) {
            return Result.fail(500, "秒杀失败，请重试");
        }

        // 根据脚本返回值处理结果
        return switch (result.intValue()) {
            case 1 -> {
                yield Result.fail(400, "秒杀已结束（库存不足）");
            }
            case 2 -> {
                String orderId = "order_" + System.currentTimeMillis() + "_" + goodsId;
                String remainingStock = stringRedisTemplate.opsForValue().get(stockKey);
                // 构造成功响应的业务数据
                ResultData data = new ResultData();
                data.setGoodsId(goodsId);
                data.setOrderId(orderId);
                data.setRemainingStock(remainingStock);
                yield Result.success("秒杀成功", data);
            }
            default -> {
                yield Result.fail(500, "秒杀失败，请重试");
            }
        };
    }
}
