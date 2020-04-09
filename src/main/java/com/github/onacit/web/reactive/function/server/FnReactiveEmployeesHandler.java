package com.github.onacit.web.reactive.function.server;

import com.github.onacit.web.bind.Employee;
import com.github.onacit.web.reactive.ReactiveEmployeesAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.Objects;

import static com.github.onacit.web.bind.EmployeesController.PATH_NAME_EMPLOYEE_ID;
import static com.github.onacit.web.bind.EmployeesController.PATH_TEMPLATE_EMPLOYEE_ID;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static reactor.core.publisher.Mono.just;

@Validated
@Component
@RequiredArgsConstructor
@Slf4j
public class FnReactiveEmployeesHandler {

    public Mono<ServerResponse> create(@NotNull final ServerRequest request) {
        return request
                .bodyToMono(Employee.class)
                .flatMap(v -> reactiveEmployeesAdapter.set(v)
                        .filter(ok -> ok != null && ok)
                        .flatMap(ok -> {
                            final URI location = request
                                    .uriBuilder()
                                    .pathSegment(PATH_TEMPLATE_EMPLOYEE_ID)
                                    .build(v.getId());
                            return created(location).build();
                        })
                        .switchIfEmpty(status(INTERNAL_SERVER_ERROR).build()));
    }

    public Mono<ServerResponse> read(@NotNull final ServerRequest request) {
        return just(request.pathVariable(PATH_NAME_EMPLOYEE_ID))
                .flatMap(reactiveEmployeesAdapter::get)
                .flatMap(v -> ok().body(fromValue(v)))
                .switchIfEmpty(notFound().build());
    }

    public Mono<ServerResponse> update(@NotNull final ServerRequest request) {
        return request.bodyToMono(Employee.class)
                .flatMap(v -> {
                    final String id = request.pathVariable(PATH_NAME_EMPLOYEE_ID);
                    if (!Objects.equals(v.getId(), id)) {
                        return badRequest().bodyValue("$.id(" + v.getId() + ") != /{id}(" + id + ")");
                    }
                    return reactiveEmployeesAdapter.set(v)
                            .filter(ok -> ok != null && ok)
                            .flatMap(ok -> noContent().build())
                            .switchIfEmpty(status(INTERNAL_SERVER_ERROR).build());
                });
    }

    public Mono<ServerResponse> delete(@NotNull final ServerRequest request) {
        return just(request.pathVariable(PATH_NAME_EMPLOYEE_ID))
                .flatMap(reactiveEmployeesAdapter::del)
                .filter(v -> v != null && v >= 0L)
                .flatMap(v -> noContent().build())
                .switchIfEmpty(status(INTERNAL_SERVER_ERROR).build())
                ;
    }

    private final ReactiveEmployeesAdapter reactiveEmployeesAdapter;
}
