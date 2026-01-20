package com.example.gnote.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    // 状态码：200=成功，500=失败，400=参数错误等
    private int code;
    // 提示信息
    private String msg;
    // 业务数据（比如订单号、剩余库存）
    private T data;

    // 快捷方法：成功返回（带数据）
    public static <T> Result<T> success(String msg, T data) {
        return new Result<>(200, msg, data);
    }

    // 快捷方法：失败返回
    public static <T> Result<T> fail(int code, String msg) {
        return new Result<>(code, msg, null);
    }
}
