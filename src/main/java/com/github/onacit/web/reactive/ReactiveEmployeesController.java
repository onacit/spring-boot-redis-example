package com.github.onacit.web.reactive;

import com.github.onacit.web.bind.Employee;
import com.github.onacit.web.bind.EmployeesController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.Objects;

import static com.github.onacit.web.bind.EmployeesController.PATH_NAME_EMPLOYEE_ID;
import static com.github.onacit.web.bind.EmployeesController.PATH_TEMPLATE_EMPLOYEE_ID;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.status;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static org.springframework.web.util.UriComponentsBuilder.fromUri;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;

@Validated
@RestController
@RequestMapping(path = ReactiveEmployeesController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
@Slf4j
public class ReactiveEmployeesController {

    public static final String REQUEST_MAPPING_PATH = "/reactive" + EmployeesController.REQUEST_MAPPING_PATH;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Object>> create(@NotNull final ServerHttpRequest request,
                                               @Valid @RequestBody final Employee entity) {
        return reactiveEmployeeRedisTemplateAdapter.set(entity)
                .filter(ok -> ok != null && ok)
                .map(ok -> {
                    final URI location = fromUri(request.getURI())
                            .pathSegment(PATH_TEMPLATE_EMPLOYEE_ID)
                            .build()
                            .expand(entity.getId())
                            .toUri();
                    return created(location).build();
                })
                .switchIfEmpty(just(status(INTERNAL_SERVER_ERROR).build()));
    }

    @GetMapping(path = PATH_TEMPLATE_EMPLOYEE_ID, produces = APPLICATION_JSON_VALUE)
    public Mono<Employee> read(@NotBlank @PathVariable(name = PATH_NAME_EMPLOYEE_ID) final String id) {
        return reactiveEmployeeRedisTemplateAdapter.get(id)
                .switchIfEmpty(error(new ResponseStatusException(NOT_FOUND)));
    }

    @ResponseStatus(NO_CONTENT)
    @PutMapping(path = PATH_TEMPLATE_EMPLOYEE_ID, consumes = APPLICATION_JSON_VALUE)
    public Mono<Boolean> update(@PathVariable(name = PATH_NAME_EMPLOYEE_ID) final String id,
                                @Valid @RequestBody final Employee entity) {
        if (!Objects.equals(entity.getId(), id)) {
            throw new ResponseStatusException(BAD_REQUEST, "$.id(" + entity.getId() + ") != /{id}(" + id + ")");
        }
        return reactiveEmployeeRedisTemplateAdapter.set(entity)
                .filter(ok -> ok != null && ok)
                .switchIfEmpty(error(new ResponseStatusException(INTERNAL_SERVER_ERROR)));
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping(path = PATH_TEMPLATE_EMPLOYEE_ID)
    public Mono<Long> delete(@PathVariable(name = PATH_NAME_EMPLOYEE_ID) final String id) {
        return reactiveEmployeeRedisTemplateAdapter.del(id)
                .filter(v -> v != null && (v == 0L || v == 1L))
                .switchIfEmpty(error(new ResponseStatusException(INTERNAL_SERVER_ERROR)));
    }

    private final ReactiveEmployeeRedisTemplateAdapter reactiveEmployeeRedisTemplateAdapter;
}
