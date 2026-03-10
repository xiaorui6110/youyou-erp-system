package com.xiaorui.youyouerpsystem.utils;


import lombok.Data;


/**
 * @description: 字符串工具类
 * @author: xiaorui
 * @date: 2026-03-10 21:32
 **/
@Data
public class StringUtil {

    private StringUtil() {

    }

    private static String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public final static String REGEX = "'|#|%|;|--| and | and|and | or | or|or | not | not|not " +
            "| use | use|use | insert | insert|insert | delete | delete|delete | update | update|update " +
            "| select | select|select | count | count|count | group | group|group | union | union|union " +
            "| create | create|create | drop | drop|drop | truncate | truncate|truncate | alter | alter|alter " +
            "| grant | grant|grant | execute | execute|execute | exec | exec|exec | xp_cmdshell | xp_cmdshell|xp_cmdshell " +
            "| call | call|call | declare | declare|declare | source | source|source | sql | sql|sql ";
    private static final char SEPARATOR = '_';

    /**
     * 判断对象/字符串是否为空
     */
    public static boolean isNull(Object object)
    {
        return object == null;
    }

    public static boolean isNotNull(Object object)
    {
        return !isNull(object);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 驼峰转下划线命名
     */
    public static String toUnderScoreCase(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        // 前置字符是否大写
        boolean preCharIsUpperCase = true;
        // 当前字符是否大写
        boolean curreCharIsUpperCase = true;
        // 下一字符是否大写
        boolean nexteCharIsUpperCase = true;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (i > 0) {
                preCharIsUpperCase = Character.isUpperCase(str.charAt(i - 1));
            } else {
                preCharIsUpperCase = false;
            }
            curreCharIsUpperCase = Character.isUpperCase(c);
            if (i < (str.length() - 1)) {
                nexteCharIsUpperCase = Character.isUpperCase(str.charAt(i + 1));
            }
            if (preCharIsUpperCase && curreCharIsUpperCase && !nexteCharIsUpperCase) {
                sb.append(SEPARATOR);
            } else if ((i != 0 && !preCharIsUpperCase) && curreCharIsUpperCase) {
                sb.append(SEPARATOR);
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }




}
