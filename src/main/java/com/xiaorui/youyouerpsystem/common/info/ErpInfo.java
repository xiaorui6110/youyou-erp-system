package com.xiaorui.youyouerpsystem.common.info;


import lombok.Getter;

/**
 * @description: 系统响应枚举
 * @author: xiaorui
 * @date: 2026-03-10 21:07
 **/

@Getter
public enum ErpInfo {

    /**
     * 状态码
     */
    OK(200, "成功"),
    BAD_REQUEST(400, "请求错误或参数错误"),
    UNAUTHORIZED(401, "未认证用户"),
    INVALID_VERIFY_CODE(461, "错误的验证码"),
    ERROR(500, "服务内部错误"),
    WARING_MSG(201, "提醒信息"),
    REDIRECT(301, "session失效，重定向"),
    FORWARD_REDIRECT(302, "转发请求session失效"),
    FORWARD_FAILED(303, "转发请求失败!"),
    TEST_USER(-1, "演示用户禁止操作");

    public final int code;
    public final String msg;

    /**
     * 定义枚举构造函数
     */
    ErpInfo(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
