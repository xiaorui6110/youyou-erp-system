package com.xiaorui.youyouerpsystem.common.constants;


/**
 * @description: 异常常量
 * @author: xiaorui
 * @date: 2026-03-06 23:14
 **/

public class ExceptionConstants {

    public static final String GLOBAL_RETURNS_CODE = "code";
    public static final String GLOBAL_RETURNS_MESSAGE = "msg";
    public static final String GLOBAL_RETURNS_DATA = "data";

    /**
     * 正常返回/操作成功
     **/
    public static final String SERVICE_SUCCESS_CODE = "200";
    public static final String SERVICE_SUCCESS_MSG = "操作成功";
    /**
     * 数据查询异常
     */
    public static final String DATA_READ_FAIL_CODE = "300";
    public static final String DATA_READ_FAIL_MSG = "数据查询异常";
    /**
     * 数据写入异常
     */
    public static final String DATA_WRITE_FAIL_CODE = "301";
    public static final String DATA_WRITE_FAIL_MSG = "数据写入异常";

    /**
     * 系统运行时未知错误
     **/
    public static final String SERVICE_SYSTEM_ERROR_CODE = "500";
    public static final String SERVICE_SYSTEM_ERROR_MSG = "未知异常";
    /**
     * 检测到存在依赖数据，是否强制删除？
     **/
    public static final String DELETE_FORCE_CONFIRM_CODE = "601";
    public static final String DELETE_FORCE_CONFIRM_MSG = "检测到存在依赖数据，不能删除！";

    /**
     * 文件扩展名必须为xls
     **/
    public static final String FILE_EXTENSION_ERROR_CODE = "701";
    public static final String FILE_EXTENSION_ERROR_MSG = "文件扩展名必须为xls";

    /**
     * 用户信息
     * type = 5
     * */
    public static final String USER_ADD_FAILED_CODE = "500000";
    public static final String USER_ADD_FAILED_MSG = "添加用户信息失败";
    public static final String USER_DELETE_FAILED_CODE = "500001";
    public static final String USER_DELETE_FAILED_MSG = "删除用户信息失败";
    public static final String USER_EDIT_FAILED_CODE = "500002";
    public static final String USER_EDIT_FAILED_MSG = "修改用户信息失败";
    public static final String USER_LOGIN_NAME_ALREADY_EXISTS_CODE = "500003";
    public static final String USER_LOGIN_NAME_ALREADY_EXISTS_MSG = "登录名在本系统已存在";
    public static final String USER_OVER_LIMIT_FAILED_CODE = "500004";
    public static final String USER_OVER_LIMIT_FAILED_MSG = "用户录入数量超出限制，请联系平台管理员";
    public static final String USER_NAME_LIMIT_USE_CODE = "500005";
    public static final String USER_NAME_LIMIT_USE_MSG = "此用户名限制使用";
    public static final String USER_ENABLE_OVER_LIMIT_FAILED_CODE = "500006";
    public static final String USER_ENABLE_OVER_LIMIT_FAILED_MSG = "启用的用户数量超出限制，请联系平台管理员";
    public static final String USER_LIMIT_TENANT_DELETE_CODE = "500008";
    public static final String USER_LIMIT_TENANT_DELETE_MSG = "抱歉，租户不能被删除";
    public static final String USER_LEADER_IS_EXIST_CODE = "500009";
    public static final String USER_LEADER_IS_EXIST_MSG = "抱歉，当前机构已经存在经理";
    public static final String USER_JCAPTCHA_ERROR_CODE = "500010";
    public static final String USER_JCAPTCHA_ERROR_MSG = "验证码错误";
    public static final String USER_JCAPTCHA_EXPIRE_CODE = "500011";
    public static final String USER_JCAPTCHA_EXPIRE_MSG = "验证码已失效";
    public static final String USER_JCAPTCHA_EMPTY_CODE = "500012";
    public static final String USER_JCAPTCHA_EMPTY_MSG = "验证码不能为空";


}
