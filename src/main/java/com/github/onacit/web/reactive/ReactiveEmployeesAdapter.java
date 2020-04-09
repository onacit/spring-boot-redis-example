package com.github.onacit.web.reactive;

import com.github.onacit.web.bind.Employee;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializationContext.RedisSerializationContextBuilder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

import static com.github.onacit.web.bind.Employee.key;
import static com.github.onacit.web.bind.Employee.keySerializer;
import static com.github.onacit.web.bind.Employee.valueSerializer;
import static lombok.AccessLevel.PROTECTED;
import static org.springframework.data.redis.serializer.RedisSerializationContext.newSerializationContext;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReactiveEmployeesAdapter {

    @PostConstruct
    private void onPostConstruct() {
        final RedisSerializationContextBuilder<String, Employee> builder = newSerializationContext(keySerializer());
        final RedisSerializationContext<String, Employee> context = builder.value(valueSerializer()).build();
        reactiveRedisTemplate = new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, context);
    }

    public Mono<Employee> get(final String id) {
        return reactiveRedisTemplate
                .opsForValue()
                .get(key(id))
                ;
    }

    public Mono<Boolean> set(final Employee entity) {
        return reactiveRedisTemplate
                .opsForValue()
                .set(entity.key(), entity)
                ;
    }

    public Mono<Long> del(final String id) {
        return reactiveRedisTemplate
                .delete(key(id))
                ;
    }

    private final ReactiveRedisConnectionFactory reactiveRedisConnectionFactory;

    @Accessors(fluent = true)
    @Getter(PROTECTED)
    private ReactiveRedisTemplate<String, Employee> reactiveRedisTemplate;
}
