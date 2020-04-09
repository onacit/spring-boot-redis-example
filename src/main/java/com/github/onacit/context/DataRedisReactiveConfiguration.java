package com.github.onacit.context;

import com.github.onacit.web.bind.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializationContext.RedisSerializationContextBuilder;

import static com.github.onacit.web.bind.Employee.keySerializer;
import static com.github.onacit.web.bind.Employee.valueSerializer;
import static org.springframework.data.redis.serializer.RedisSerializationContext.newSerializationContext;

@Configuration
@Slf4j
public class DataRedisReactiveConfiguration {

    @EmployeeRedisTemplate
    @Bean
    public ReactiveRedisTemplate<String, Employee> reactiveEmployeeRedisTemplate(
            final ReactiveRedisConnectionFactory factory) {
        final RedisSerializationContextBuilder<String, Employee> builder = newSerializationContext(keySerializer());
        final RedisSerializationContext<String, Employee> context = builder.value(valueSerializer()).build();
        return new ReactiveRedisTemplate<>(factory, context);
    }
}
