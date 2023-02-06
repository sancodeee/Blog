package com.ws.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
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

//        自定义cacheManager
//        @Bean
//        public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory){
//            return RedisCacheManager
//                    .builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
//                    .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig()
//                            .entryTtl(Duration.ofMillis(10)))
//                    .build();
//        }

}
