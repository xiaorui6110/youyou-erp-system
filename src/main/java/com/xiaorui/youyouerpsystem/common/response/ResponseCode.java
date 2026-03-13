package com.xiaorui.youyouerpsystem.common.response;


import com.alibaba.fastjson2.annotation.JSONCreator;
import com.alibaba.fastjson2.annotation.JSONField;

/**
 * @description: 响应码
 * @author: xiaorui
 * @date: 2026-03-13 16:55
 **/

public record ResponseCode(String code, Object data) {

    @JSONCreator
    public ResponseCode(@JSONField(name = "code") String code, @JSONField(name = "data") Object data) {
        this.code = code;
        this.data = data;
    }

}
