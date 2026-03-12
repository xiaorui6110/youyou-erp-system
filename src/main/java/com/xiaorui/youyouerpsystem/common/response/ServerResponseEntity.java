package com.xiaorui.youyouerpsystem.common.response;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Objects;

/**
 * @description: 服务器响应实体
 * @author: xiaorui
 * @date: 2026-03-11 22:43
 **/
@Slf4j
@Data
public class ServerResponseEntity<T> implements Serializable {

    /**
     * 状态码
     */
    private String code;

    /**
     * 信息
     */
    private String msg;

    /**
     * 数据
     */
    private T data;

    /**
     * 版本
     */
    private String version;

    /**
     * 时间戳
     */
    private Long timestamp;

    public boolean isSuccess() {
        return Objects.equals(ResponseEnum.OK.getValue(), this.code);
    }

    public ServerResponseEntity() {
        this.version = "youyouerp-1.0.0";
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> ServerResponseEntity<T> success(T data) {
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setData(data);
        serverResponseEntity.setMsg(ResponseEnum.OK.getMsg());
        serverResponseEntity.setCode(ResponseEnum.OK.getValue());
        return serverResponseEntity;
    }

    public static <T> ServerResponseEntity<T> success() {
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setCode(ResponseEnum.OK.getValue());
        serverResponseEntity.setMsg(ResponseEnum.OK.getMsg());
        return serverResponseEntity;
    }

    public static <T> ServerResponseEntity<T> success(String code, T data) {
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setCode(code);
        serverResponseEntity.setData(data);
        return serverResponseEntity;
    }

    public static <T> ServerResponseEntity<T> showFailMsg(String msg) {
        log.error(msg);
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setMsg(msg);
        serverResponseEntity.setCode(ResponseEnum.SHOW_FAIL.getValue());
        return serverResponseEntity;
    }

    public static <T> ServerResponseEntity<T> fail(ResponseEnum responseEnum) {
        log.error(responseEnum.toString());
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setMsg(responseEnum.getMsg());
        serverResponseEntity.setCode(responseEnum.getValue());
        return serverResponseEntity;
    }

    public static <T> ServerResponseEntity<T> fail(ResponseEnum responseEnum, T data) {
        log.error(responseEnum.toString());
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setMsg(responseEnum.getMsg());
        serverResponseEntity.setCode(responseEnum.getValue());
        serverResponseEntity.setData(data);
        return serverResponseEntity;
    }

    public static <T> ServerResponseEntity<T> fail(String code, String msg, T data) {
        log.error(msg);
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setMsg(msg);
        serverResponseEntity.setCode(code);
        serverResponseEntity.setData(data);
        return serverResponseEntity;
    }

    public static <T> ServerResponseEntity<T> fail(String code, String msg) {
        return fail(code, msg, null);
    }

    public static <T> ServerResponseEntity<T> fail(Integer code, T data) {
        ServerResponseEntity<T> serverResponseEntity = new ServerResponseEntity<>();
        serverResponseEntity.setCode(String.valueOf(code));
        serverResponseEntity.setData(data);
        return serverResponseEntity;
    }

}
