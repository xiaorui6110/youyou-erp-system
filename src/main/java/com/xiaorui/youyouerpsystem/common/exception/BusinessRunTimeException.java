package com.xiaorui.youyouerpsystem.common.exception;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 业务运行时异常
 * @author: xiaorui
 * @date: 2026-03-06 23:50
 **/
@Slf4j
@Getter
public class BusinessRunTimeException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;
    private final String code;
    private final Map<String, Object> data;

    public BusinessRunTimeException(String code, String reason) {
        super(reason);
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("message", reason);
        this.code = code;
        this.data = objectMap;
    }

    public BusinessRunTimeException(String code, String reason, Throwable throwable) {
        super(reason, throwable);
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("message", reason);
        this.code = code;
        this.data = objectMap;
    }
}
