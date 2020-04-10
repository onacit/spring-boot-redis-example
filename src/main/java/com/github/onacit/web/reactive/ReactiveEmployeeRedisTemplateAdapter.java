package com.github.onacit.web.reactive;

import com.github.onacit.web.bind.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.github.onacit.web.bind.Employee.key;

@Validated
@Component
@RequiredArgsConstructor
@Slf4j
public class ReactiveEmployeeRedisTemplateAdapter {

    public Mono<Employee> get(@NotBlank final String id) {
        return reactiveRedisTemplate
                .opsForValue()
                .get(key(id))
                ;
    }

    public Mono<Boolean> set(@Valid @NotNull final Employee entity) {
        return reactiveRedisTemplate
                .opsForValue()
                .set(entity.key(), entity)
                ;
    }

    public Mono<Long> del(@NotBlank final String id) {
        return reactiveRedisTemplate
                .delete(key(id))
                ;
    }

    private final ReactiveRedisTemplate<String, Employee> reactiveRedisTemplate;
}
