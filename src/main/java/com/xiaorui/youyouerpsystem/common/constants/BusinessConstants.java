package com.xiaorui.youyouerpsystem.common.constants;


/**
 * @description: 业务常量
 * @author: xiaorui
 * @date: 2026-03-06 23:14
 **/

public class BusinessConstants {

    // ================================ 日期时间相关 ================================

    /**
     * 默认的日期格式
     */
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 一天的初始时间
     */
    public static final String DAY_FIRST_TIME = " 00:00:00";

    /**
     * 一天的结束时间
     */
    public static final String DAY_LAST_TIME = " 23:59:59";

    // =============================== 分页相关 ================================

    /**
     * 默认的分页起始页页码
     */
    public static final Integer DEFAULT_PAGINATION_PAGE_NUMBER = 1;

    /**
     * 无数据时列表返回的默认数据条数
     */
    public static final Long DEFAULT_LIST_NULL_NUMBER = 0L;

    /**
     * 默认的分页条数
     */
    public static final Integer DEFAULT_PAGINATION_PAGE_SIZE = 10;

    // =============================== 用户业务相关 ================================

    /**
     * 删除标记 deleteFlag  '0'未删除 '1'已删除
     * */
    public static final String DELETE_FLAG_DELETED = "1";
    public static final String DELETE_FLAG_EXISTS = "0";

    /**
     * 新增用户默认密码
     * */
    public static final String USER_DEFAULT_PASSWORD = "12345678";

    /**
     * 用户是否系统自带
     * 0-非系统自带，1-系统自带
     * */
    public static final byte USER_NOT_SYSTEM = 0;
    public static final byte USER_IS_SYSTEM = 1;

    /**
     * 用户是否为管理者
     * 0-管理者，1-员工
     * */
    public static final byte USER_IS_MANAGER = 0;
    public static final byte USER_NOT_MANAGER = 1;
    /**
     * 用户状态
     * 0-正常，2-封禁
     * */
    public static final byte USER_STATUS_NORMAL = 0;
    public static final byte USER_STATUS_BANNED = 2;





}
