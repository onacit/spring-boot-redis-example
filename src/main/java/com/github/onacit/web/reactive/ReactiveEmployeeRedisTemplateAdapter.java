package com.github.onacit.web.reactive;

import com.github.onacit.context.EmployeeRedisTemplate;
import com.github.onacit.web.bind.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static com.github.onacit.web.bind.Employee.key;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReactiveEmployeeRedisTemplateAdapter {

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

    @EmployeeRedisTemplate
    //@Accessors(fluent = true)
    //@Getter(PROTECTED)
    private final ReactiveRedisTemplate<String, Employee> reactiveRedisTemplate;
}
