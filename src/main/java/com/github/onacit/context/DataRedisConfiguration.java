package com.github.onacit.context;

import com.github.onacit.web.bind.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import static com.github.onacit.web.bind.Employee.keySerializer;
import static com.github.onacit.web.bind.Employee.valueSerializer;

@Configuration
@Slf4j
public class DataRedisConfiguration {

    @EmployeeRedisTemplate
    @Bean
    public RedisTemplate<String, Employee> employeeRedisTemplate(final RedisConnectionFactory factory) {
        final RedisTemplate<String, Employee> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(keySerializer());
        template.setValueSerializer(valueSerializer());
        template.afterPropertiesSet();
        return template;
    }
}
