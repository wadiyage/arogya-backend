package edu.icet.arogya.config;

import lombok.NonNull;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;
import java.util.Arrays;

@Configuration
@EnableCaching
public class RedisCacheConfig {

    @Bean
    public KeyGenerator customKeyGenerator() {
        return (target, method, params) ->
                method.getName() + "_" + Arrays.deepToString(params);
    }

    @Bean
    public RedisSerializer<@NonNull Object> redisValueSerializer() {
        return RedisSerializer.json();
    }
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(
            RedisSerializer<@NonNull Object> redisValueSerializer
    ) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                                redisValueSerializer
                        )
                )
                .disableCachingNullValues();
    }

    @Bean
    public CacheManager cacheManager(
            RedisConnectionFactory redisConnectionFactory,
            RedisCacheConfiguration redisCacheConfiguration
    ) {
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }
}
