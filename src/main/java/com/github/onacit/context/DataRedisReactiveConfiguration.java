package com.github.onacit.context;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import javax.annotation.PostConstruct;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataRedisReactiveConfiguration {

    @PostConstruct
    private void onPostConstruct() {
        standaloneConfig = new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
    }

    @Bean
    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() {
        return new LettuceConnectionFactory(standaloneConfig);
    }

    private final RedisProperties redisProperties;

    private transient RedisStandaloneConfiguration standaloneConfig;
}
