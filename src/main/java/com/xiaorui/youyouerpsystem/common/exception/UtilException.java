package com.xiaorui.youyouerpsystem.common.exception;


import java.io.Serial;

/**
 * @description: 工具类异常
 * @author: xiaorui
 * @date: 2026-03-10 21:44
 **/

public class UtilException  extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UtilException(Throwable e)
    {
        super(e.getMessage(), e);
    }

    public UtilException(String message)
    {
        super(message);
    }

    public UtilException(String message, Throwable throwable)
    {
        super(message, throwable);
    }

}
