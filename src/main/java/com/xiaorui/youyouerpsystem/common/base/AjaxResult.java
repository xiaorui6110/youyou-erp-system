package com.xiaorui.youyouerpsystem.common.base;


import com.xiaorui.youyouerpsystem.utils.StringUtil;

import java.io.Serial;
import java.util.HashMap;
import java.util.Objects;

/**
 * @description: 操作消息提醒
 * @author: xiaorui
 * @date: 2026-03-06 23:57
 **/

public class AjaxResult extends HashMap<String, Object> {

    @Serial
    private static final long serialVersionUID = 1L;

    public static final String CODE_TAG = "code";
    public static final String MSG_TAG = "msg";
    public static final String DATA_TAG = "data";

    /**
     * 状态类型
     */
    public enum Type {
        /**
         * 成功/警告/错误
         */
        SUCCESS(0),
        WARN(301),
        ERROR(500);

        private final int value;

        Type(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }
    }


    /**
     * 初始化一个新创建的 AjaxResult 对象，使其表示一个空消息。
     */
    public AjaxResult() {

    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     */
    public AjaxResult(Type type, String msg) {
        super.put(CODE_TAG, type.value);
        super.put(MSG_TAG, msg);
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     */
    public AjaxResult(Type type, String msg, Object data) {
        super.put(CODE_TAG, type.value);
        super.put(MSG_TAG, msg);
        if (StringUtil.isNotNull(data)) {
            super.put(DATA_TAG, data);
        }
    }

    /**
     * 返回成功消息
     */
    public static AjaxResult success()
    {
        return AjaxResult.success("操作成功");
    }
    public static AjaxResult success(Object data)
    {
        return AjaxResult.success("操作成功", data);
    }
    public static AjaxResult success(String msg)
    {
        return AjaxResult.success(msg, null);
    }
    public static AjaxResult success(String msg, Object data)
    {
        return new AjaxResult(Type.SUCCESS, msg, data);
    }

    /**
     * 返回警告消息
     */
    public static AjaxResult warn(String msg)
    {
        return AjaxResult.warn(msg, null);
    }
    public static AjaxResult warn(String msg, Object data)
    {
        return new AjaxResult(Type.WARN, msg, data);
    }

    /**
     * 返回错误消息
     */
    public static AjaxResult error()
    {
        return AjaxResult.error("操作失败");
    }
    public static AjaxResult error(String msg)
    {
        return AjaxResult.error(msg, null);
    }
    public static AjaxResult error(String msg, Object data)
    {
        return new AjaxResult(Type.ERROR, msg, data);
    }

    /**
     * 判断是否成功/警告/错误
     */
    public boolean isSuccess() {
        return Objects.equals(Type.SUCCESS.value, this.get(CODE_TAG));
    }
    public boolean isWarn() {
        return Objects.equals(Type.WARN.value, this.get(CODE_TAG));
    }
    public boolean isError() {
        return Objects.equals(Type.ERROR.value, this.get(CODE_TAG));
    }

    /**
     * 方便链式调用
     *
     * @param key 键
     * @param value 值
     * @return 数据对象
     */
    @Override
    public AjaxResult put(String key, Object value) {
        super.put(key, value);
        return this;
    }

}
