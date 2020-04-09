package com.github.onacit.context;

import com.github.onacit.web.bind.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import static com.github.onacit.web.bind.Employee.keySerializer;
import static com.github.onacit.web.bind.Employee.valueSerializer;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataRedisConfiguration extends AbstractDataRedisConfiguration {

    @Bean
    public RedisConnectionFactory redisConnectionFactory(
            final RedisStandaloneConfiguration redisStandaloneConfiguration) {
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @EmployeeRedisTemplate
    @Bean
    public RedisTemplate<String, Employee> employeeRedisTemplate(final RedisConnectionFactory redisConnectionFactory) {
        final RedisTemplate<String, Employee> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(keySerializer());
        redisTemplate.setValueSerializer(valueSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

//    private final RedisProperties redisProperties;
//    private transient RedisStandaloneConfiguration standaloneConfig;
}
