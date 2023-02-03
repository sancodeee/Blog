package com.ws.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import lombok.experimental.Accessors;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

//自定义基于注解的序列化机制
/*
* 在springboot整合redis组件提供的缓存自动配置类RedisCacheConfiguration中，通过redis连接工厂RedisConnectionFactory定义了一个缓存管理器，
* 使用默认的JdkSerializationRedisSerializer序列化方式
* 如果想修改序列化方式，需要自定义一个cacheManager的Bean组件
* */
@Configuration
@Accessors(chain = true)
public class RedisConfig {

//    @Bean
//    public RedisCacheConfiguration provideRedisCacheConfiguration(){
//
//        //加载默认配置
//        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
//        //返回Jackson序列化器
//        return config.serializeValuesWith(
//                RedisSerializationContext.SerializationPair
//                        .fromSerializer(new GenericFastJsonRedisSerializer()));
//
//    }

//    @Bean
//    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
//        //分别创建String和JSON格式序列化对象，对缓存数据key和value进行转换
//        RedisSerializer<String> stringRedisSerializer = new StringRedisSerializer();
//        Jackson2JsonRedisSerializer<?> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
//        //解决查询缓存转换异常的问题
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
//        //定义缓存数据序列化方式及时效
//        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
//                //定义缓存数据的有效期为1分钟
//                .entryTtl(Duration.ofMinutes(1))
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(stringRedisSerializer))
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
//                //禁用缓存空值，不缓存null校验
//                .disableCachingNullValues();
//        return RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(configuration).build();
//    }




}
