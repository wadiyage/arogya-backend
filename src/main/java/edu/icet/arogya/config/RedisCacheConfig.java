package edu.icet.arogya.config;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class RedisCacheConfig {

    @Bean
    public KeyGenerator customKeyGenerator() {
        return (target, method, params) ->
                method.getName() + "_" + Arrays.deepToString(params);
    }
}
