package com.xiaorui.youyouerpsystem.common.exception;


import com.xiaorui.youyouerpsystem.common.constants.ExceptionConstants;
import org.slf4j.Logger;

/**
 * @description: 封装日志打印，收集日志（统一异常日志处理工具）（经AI优化）
 * @author: xiaorui
 * @date: 2026-03-06 23:50
 **/

public class LoggerException {

    /**
     * 私有化构造方法，避免实例化工具类
     */
    private LoggerException() {
        throw new UnsupportedOperationException("这是一个工具类，不能实例化！");
    }

    /**
     * 数据读取失败日志打印 + 业务异常抛出
     *
     * @param logger 日志对象（保证日志归属具体类）
     * @param e 异常对象（可为null，兼容无底层异常的场景）
     * @param bizDesc 业务描述（可选，补充具体业务场景，如"查询用户订单"）
     */
    public static void readFail(Logger logger, Exception e, String... bizDesc) {
        logAndThrow(logger, e,
                ExceptionConstants.DATA_READ_FAIL_CODE,
                ExceptionConstants.DATA_READ_FAIL_MSG,
                bizDesc);
    }

    /**
     * 数据写入失败日志打印 + 业务异常抛出
     *
     * @param logger 日志对象
     * @param e 异常对象（可为null）
     * @param bizDesc 业务描述（可选）
     */
    public static void writeFail(Logger logger, Exception e, String... bizDesc) {
        logAndThrow(logger, e,
                ExceptionConstants.DATA_WRITE_FAIL_CODE,
                ExceptionConstants.DATA_WRITE_FAIL_MSG,
                bizDesc);
    }

    /**
     * 通用日志打印 + 异常抛出方法（核心封装，避免重复代码）
     *
     * @param logger 日志对象
     * @param e 异常对象
     * @param errorCode 异常码
     * @param errorMsg 异常提示
     * @param bizDesc 业务描述（可变参数，兼容无描述场景）
     */
    private static void logAndThrow(Logger logger, Exception e, String errorCode, String errorMsg, String... bizDesc) {
        // 1. 空值校验：避免logger为空导致NPE
        if (logger == null) {
            throw new IllegalArgumentException("日志对象Logger不能为空！");
        }
        // 2. 拼接业务描述（增强日志可读性）
        String businessDesc = bizDesc != null && bizDesc.length > 0 ? bizDesc[0] : "无具体业务描述";

        // 3. 标准化日志格式（包含业务场景，便于日志检索）
        logger.error("【业务异常】- 异常码：{}，异常提示：{}，业务场景：{}，异常详情：", errorCode, errorMsg, businessDesc, e);

        // 4. 抛出业务异常（保持原有逻辑）
        throw new BusinessRunTimeException(errorCode, errorMsg);
    }

    // ========== 扩展：新增通用异常方法（便于后续扩展其他异常类型） ==========
    /**
     * 通用业务异常日志打印
     *
     * @param logger 日志对象
     * @param e 异常对象
     * @param errorCode 异常码
     * @param errorMsg 异常提示
     * @param bizDesc 业务描述
     */
    public static void businessFail(Logger logger, Exception e, String errorCode, String errorMsg, String bizDesc) {
        logAndThrow(logger, e, errorCode, errorMsg, bizDesc);
    }
}