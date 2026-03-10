package com.xiaorui.youyouerpsystem.common.exception;


import com.xiaorui.youyouerpsystem.common.constants.ExceptionConstants;
import org.slf4j.Logger;

/**
 * @description: 封装日志打印，收集日志
 * @author: xiaorui
 * @date: 2026-03-06 23:50
 **/

public class YouYouException {

    public static void readFail(Logger logger, Exception e) {
        logger.error("异常码[{}],异常提示[{}],异常[{}]",
                ExceptionConstants.DATA_READ_FAIL_CODE, ExceptionConstants.DATA_READ_FAIL_MSG,e);
        throw new BusinessRunTimeException(ExceptionConstants.DATA_READ_FAIL_CODE,
                ExceptionConstants.DATA_READ_FAIL_MSG);
    }

    public static void writeFail(Logger logger, Exception e) {
        logger.error("异常码[{}],异常提示[{}],异常[{}]",
                ExceptionConstants.DATA_WRITE_FAIL_CODE,ExceptionConstants.DATA_WRITE_FAIL_MSG,e);
        throw new BusinessRunTimeException(ExceptionConstants.DATA_WRITE_FAIL_CODE,
                ExceptionConstants.DATA_WRITE_FAIL_MSG);
    }

}
