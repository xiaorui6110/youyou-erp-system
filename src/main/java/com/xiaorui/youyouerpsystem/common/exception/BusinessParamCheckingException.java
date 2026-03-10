package com.xiaorui.youyouerpsystem.common.exception;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 业务参数检查异常
 * @author: xiaorui
 * @date: 2026-03-06 23:49
 **/
@Slf4j
@Getter
public class BusinessParamCheckingException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;
    private final int code;
    private final Map<String, Object> data;

    public BusinessParamCheckingException(int code, String reason) {
        super(reason);
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("message", reason);
        this.code = code;
        this.data = objectMap;
    }

    public BusinessParamCheckingException(int code, String reason, Throwable throwable) {
        super(reason, throwable);
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("message", reason);
        this.code = code;
        this.data = objectMap;
    }
}
