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
    public static final int SERVICE_SUCCESS_CODE = 200;
    public static final String SERVICE_SUCCESS_MSG = "操作成功";
    /**
     * 数据查询异常
     */
    public static final int DATA_READ_FAIL_CODE = 300;
    public static final String DATA_READ_FAIL_MSG = "数据查询异常";
    /**
     * 数据写入异常
     */
    public static final int DATA_WRITE_FAIL_CODE = 301;
    public static final String DATA_WRITE_FAIL_MSG = "数据写入异常";

    /**
     * 系统运行时未知错误
     **/
    public static final int SERVICE_SYSTEM_ERROR_CODE = 500;
    public static final String SERVICE_SYSTEM_ERROR_MSG = "未知异常";
    /**
     * 检测到存在依赖数据，是否强制删除？
     **/
    public static final int DELETE_FORCE_CONFIRM_CODE = 601;
    public static final String DELETE_FORCE_CONFIRM_MSG = "检测到存在依赖数据，不能删除！";

    /**
     * 文件扩展名必须为xls
     **/
    public static final int FILE_EXTENSION_ERROR_CODE = 701;
    public static final String FILE_EXTENSION_ERROR_MSG = "文件扩展名必须为xls";


}
