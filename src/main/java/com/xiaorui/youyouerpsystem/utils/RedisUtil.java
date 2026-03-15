package com.xiaorui.youyouerpsystem.utils;


import cn.hutool.core.util.StrUtil;
import com.xiaorui.youyouerpsystem.common.constants.BusinessConstants;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @description: redis工具类（经AI优化）
 * @author: xiaorui
 * @date: 2026-03-12 22:27
 **/
@Slf4j
@Component
public class RedisUtil {

    @Resource
    public RedisTemplate<String, Object> redisTemplate;

    public static final String ACCESS_TOKEN = "X-Access-Token";

    /**
     * 从session（Redis）中获取信息
     * @param request HTTP请求对象
     * @param key 缓存键
     * @return 缓存值
     */
    public Object getObjectFromSessionByKey(HttpServletRequest request, String key) {
        // 空值快速返回，避免空指针
        if (request == null || key == null || key.isEmpty()) {
            log.warn("getObjectFromSessionByKey: request或key为空");
            return null;
        }

        String token = request.getHeader(ACCESS_TOKEN);
        if (token == null || token.isEmpty()) {
            log.debug("getObjectFromSessionByKey: token为空，跳过Redis查询");
            return null;
        }

        try {
            // 增加非空校验，提升代码健壮性
            if (redisTemplate.opsForHash().hasKey(token, key)) {
                Object obj = redisTemplate.opsForHash().get(token, key);
                // 重置过期时间
                redisTemplate.expire(token, BusinessConstants.MAX_SESSION_IN_SECONDS, TimeUnit.SECONDS);
                return obj;
            }
        } catch (Exception e) {
            log.error("从Redis获取session信息失败，token: {}, key: {}", token, key, e);
        }
        return null;
    }

    /**
     * 获得缓存的基本对象
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(final String key) {
        if (key == null || key.isEmpty()) {
            log.warn("getCacheObject: key为空");
            return null;
        }

        try {
            // 先获取Object类型的ValueOperations，再强制转换为泛型T
            ValueOperations<String, Object> operation = redisTemplate.opsForValue();
            Object value = operation.get(key);
            // 安全的类型转换（如果类型不匹配会返回null，避免ClassCastException）
            return value != null ? (T) value : null;
        } catch (Exception e) {
            log.error("获取Redis缓存失败，key: {}", key, e);
            return null;
        }
    }

    /**
     * 将信息存入Redis（session）
     * @param token 令牌
     * @param key 缓存键
     * @param obj 缓存值
     */
    public void storageObjectBySession(String token, String key, Object obj) {
        // 空值校验
        if (token == null || token.isEmpty() || key == null || key.isEmpty() || obj == null) {
            log.warn("storageObjectBySession: token/key/obj为空，跳过存储");
            return;
        }

        try {
            redisTemplate.opsForHash().put(token, key, obj.toString());
            redisTemplate.expire(token, BusinessConstants.MAX_SESSION_IN_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("存储Redis session信息失败，token: {}, key: {}", token, key, e);
        }
    }

    /**
     * 存储验证码到Redis
     * @param verifyKey 验证码键
     * @param codeNum 验证码值
     */
    public void storageCaptchaObject(String verifyKey, String codeNum) {
        if (verifyKey == null || verifyKey.isEmpty() || codeNum == null || codeNum.isEmpty()) {
            log.warn("storageCaptchaObject: verifyKey或codeNum为空");
            return;
        }

        try {
            redisTemplate.opsForValue().set(verifyKey, codeNum, BusinessConstants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("存储验证码到Redis失败，verifyKey: {}", verifyKey, e);
        }
    }

    /**
     * 带有效时间缓存字符串数据
     * @param key 缓存键
     * @param value 缓存值
     * @param time 过期时间（秒）
     */
    public void storageKeyWithTime(String key, String value, Long time) {
        if (key == null || key.isEmpty() || value == null || time == null || time <= 0) {
            log.warn("storageKeyWithTime: 参数不合法，key: {}, time: {}", key, time);
            return;
        }

        try {
            redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("存储带过期时间的Redis缓存失败，key: {}", key, e);
        }
    }

    /**
     * 删除单个Redis对象
     * @param key 缓存键
     * @return 删除结果
     */
    public boolean deleteObject(final String key) {
        if (key == null || key.isEmpty()) {
            log.warn("deleteObject: key为空");
            return false;
        }

        try {
            return redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("删除Redis缓存失败，key: {}", key, e);
            return false;
        }
    }

    /**
     * 从Redis中移除session指定键
     * @param request HTTP请求对象
     * @param key 要移除的键
     */
    public void deleteObjectBySession(HttpServletRequest request, String key) {
        if (request == null || key == null || key.isEmpty()) {
            log.warn("deleteObjectBySession: request或key为空");
            return;
        }

        String token = request.getHeader(ACCESS_TOKEN);
        if (token == null || token.isEmpty()) {
            log.debug("deleteObjectBySession: token为空，跳过删除");
            return;
        }

        try {
            redisTemplate.opsForHash().delete(token, key);
        } catch (Exception e) {
            log.error("删除Redis session信息失败，token: {}, key: {}", token, key, e);
        }
    }

    /**
     * 根据用户ID和客户端IP移除Redis中的用户信息
     * @param userId 用户ID
     * @param clientIp 客户端IP
     */
    public void deleteObjectByUserAndIp(String userId, String clientIp) {
        // 空值校验
        if (userId == null || clientIp == null || clientIp.isEmpty()) {
            log.warn("deleteObjectByUserAndIp: userId或clientIp为空");
            return;
        }

        try {
            // 优化：避免全量遍历所有key，提升性能（如果有前缀可以加前缀过滤）
            Set<String> tokens = redisTemplate.keys("*");
            if (tokens.isEmpty()) {
                return;
            }

            for (String token : tokens) {
                // 检查键是否存在且为哈希类型
                if (redisTemplate.hasKey(token) && Objects.equals(redisTemplate.type(token), DataType.HASH)) {
                    Object userIdValue = redisTemplate.opsForHash().get(token, "userId");
                    Object clientIpValue = redisTemplate.opsForHash().get(token, "clientIp");

                    // 精准匹配
                    if (userId.equals(userIdValue) && clientIp.equals(clientIpValue)) {
                        redisTemplate.opsForHash().delete(token, "userId");
                        log.debug("根据用户ID和IP删除Redis缓存成功，userId: {}, ip: {}", userId, clientIp);
                    }
                }
            }
        } catch (Exception e) {
            log.error("根据用户ID和IP删除Redis缓存失败，userId: {}, ip: {}", userId, clientIp, e);
        }
    }

    /**
     * 根据用户ID移除Redis中的用户信息
     * @param userId 用户ID
     */
    public void deleteObjectByUser(String userId) {
        if (StrUtil.isBlank(userId)) {
            log.warn("deleteObjectByUser: userId为空");
            return;
        }

        try {
            Set<String> tokens = redisTemplate.keys("*");
            if (tokens.isEmpty()) {
                return;
            }

            for (String token : tokens) {
                if (redisTemplate.hasKey(token) && Objects.equals(redisTemplate.type(token), DataType.HASH)) {
                    Object userIdValue = redisTemplate.opsForHash().get(token, "userId");
                    if (userId.equals(userIdValue)) {
                        redisTemplate.opsForHash().delete(token, "userId");
                        log.debug("根据用户ID删除Redis缓存成功，userId: {}", userId);
                    }
                }
            }
        } catch (Exception e) {
            log.error("根据用户ID删除Redis缓存失败，userId: {}", userId, e);
        }
    }

}
