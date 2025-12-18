package com.bootteam.springcloudgateway.configuration;

import com.bootteam.springcloudgateway.domain.ApiKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public ReactiveRedisTemplate<String, ApiKey> apiKeyRedisTemplate(
            ReactiveRedisConnectionFactory factory) {

        // Use the new JacksonJsonRedisSerializer (non-deprecated in 4.x)
        JacksonJsonRedisSerializer<ApiKey> serializer = new JacksonJsonRedisSerializer<>(ApiKey.class);

        RedisSerializationContext<String, ApiKey> context = RedisSerializationContext
                .<String, ApiKey>newSerializationContext(RedisSerializer.string()) // Key
                .value(serializer) // Value (4.x accepts the serializer directly here)
                .hashKey(RedisSerializer.string())
                .hashValue(serializer)
                .build();

        return new ReactiveRedisTemplate<>(factory, context);
    }
}



