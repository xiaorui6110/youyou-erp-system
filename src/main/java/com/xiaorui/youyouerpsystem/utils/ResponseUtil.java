package com.xiaorui.youyouerpsystem.utils;


import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.filter.ValueFilter;
import com.xiaorui.youyouerpsystem.common.response.BusinessCodeEnum;
import com.xiaorui.youyouerpsystem.common.response.ResponseCode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

/**
 * @description: 响应工具类（字符转换）
 * @author: xiaorui
 * @date: 2026-03-13 16:54
 **/

public class ResponseUtil {

    public static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    static {
        FORMAT.setTimeZone(TimeZone.getTimeZone("GMT+8"));
    }

    public static final class ResponseFilter implements ValueFilter {
        @Override
        public Object apply(Object object, String name, Object value) {
            // 这三个时间字段不格式化
            if ("createTime".equals(name) || "modifyTime".equals(name) || "updateTime".equals(name)) {
                return value;
            }
            // 其他日期格式化为 yyyy-MM-dd
            if (value instanceof Date) {
                return DateUtil.format((Date) value, FORMAT);
            }
            return value;
        }
    }

    public static String backJson(ResponseCode responseCode) {
        if (responseCode == null) {
            return null;
        }
        return JSON.toJSONString(responseCode, new ResponseFilter());
    }

    public static String returnJson(Map<String, Object> map, String message, String code) {
        map.put("message", message);
        return backJson(new ResponseCode(code, map));
    }

    public static String returnStr(Map<String, Object> objectMap, int res) {
        if (res > 0) {
            return returnJson(objectMap, BusinessCodeEnum.OK.name, BusinessCodeEnum.OK.code);
        } else if (res == -1) {
            return returnJson(objectMap, BusinessCodeEnum.TEST_USER.name, BusinessCodeEnum.TEST_USER.code);
        } else {
            return returnJson(objectMap, BusinessCodeEnum.ERROR.name, BusinessCodeEnum.ERROR.code);
        }
    }

}
