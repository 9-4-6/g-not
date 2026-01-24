package com.example.gnote.seckill.pojo;

import lombok.Data;

@Data
public class ResultData {
    private String orderId;
    private long goodsId;
    private String remainingStock;
}
