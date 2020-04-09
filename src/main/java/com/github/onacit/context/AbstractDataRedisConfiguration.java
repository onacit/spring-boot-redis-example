package com.github.onacit.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;

import javax.annotation.PostConstruct;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Slf4j
public abstract class AbstractDataRedisConfiguration {

    @Qualifier
    @Documented
    @Target({FIELD, METHOD, TYPE, PARAMETER})
    @Retention(RUNTIME)
    public @interface EmployeeRedisTemplate {

    }

    @PostConstruct
    private void onPostConstruct() {
        log.debug("redisProperties: {}", redisProperties);
    }

    //@Bean
    RedisStandaloneConfiguration redisStandaloneConfiguration() {
        return new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
    }

    @Autowired
    private RedisProperties redisProperties;
}
