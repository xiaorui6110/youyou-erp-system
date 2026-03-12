package com.xiaorui.youyouerpsystem.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @description: redis配置
 * @author: xiaorui
 * @date: 2026-03-12 22:43
 **/
@Configuration
public class RedisConfig {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 配置 RedisTemplate，支持 LocalDateTime 序列化
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 1. 配置 Jackson ObjectMapper，启用 Java 8 日期时间支持
        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        // 2. 配置 LocalDateTime 的序列化/反序列化器（指定格式化样式）
        LocalDateTimeSerializer localDateTimeSerializer = new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
        LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));

        javaTimeModule.addSerializer(LocalDateTime.class, localDateTimeSerializer);
        javaTimeModule.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);

        // 3. 注册 JavaTimeModule，启用 Java 8 日期时间支持
        objectMapper.registerModule(javaTimeModule);
        // 可选：关闭时间戳序列化（默认会将日期序列化为时间戳，关闭后用指定格式字符串）
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // 4. 配置 Redis 序列化器
        GenericJackson2JsonRedisSerializer jsonRedisSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        // 5. 设置 Key 和 Value 的序列化器
        redisTemplate.setKeySerializer(stringRedisSerializer);
        // Value 用 JSON 序列化（支持 LocalDateTime）
        redisTemplate.setValueSerializer(jsonRedisSerializer);
        // Hash Key 用字符串序列化
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        // Hash Value 用 JSON 序列化
        redisTemplate.setHashValueSerializer(jsonRedisSerializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}
