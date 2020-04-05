package com.github.onacit.web.reactive;

import com.github.onacit.web.bind.Employee;
import com.github.onacit.web.bind.EmployeesController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.util.Objects;

import static com.github.onacit.web.bind.Employee.key;
import static com.github.onacit.web.bind.EmployeesController.PATH_NAME_EMPLOYEE_ID;
import static com.github.onacit.web.bind.EmployeesController.PATH_TEMPLATE_EMPLOYEE_ID;
import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpRequest;
import static reactor.core.publisher.Mono.error;

@Validated
@RestController
@RequestMapping(path = ReactiveEmployeesController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
public class ReactiveEmployeesController extends AbstractReactiveEmployees {

    private static final org.slf4j.Logger logger = getLogger(lookup().lookupClass());

    public static final String REQUEST_MAPPING_PATH = "/reactive" + EmployeesController.REQUEST_MAPPING_PATH;

    //@PostMapping(consumes = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Object>> create(final HttpRequest request,
                                               @Valid @RequestBody final Employee entity) {
        return reactiveRedisTemplate()
                .opsForValue()
                .set(key(entity.getId()), entity)
                .filter(ok -> ok != null && ok)
                .map(v -> {
//                    final URI location = fromCurrentRequestUri() // ServletUriComponentsBuilder
//                            .pathSegment(PATH_TEMPLATE_EMPLOYEE_ID)
//                            .build()
//                            .expand(entity.getId())
//                            .toUri();
                    final URI location = fromHttpRequest(request) // UriComponentsBuilder
                            .pathSegment(PATH_TEMPLATE_EMPLOYEE_ID)
                            .build()
                            .expand(entity.getId())
                            .toUri();
                    return created(location).build();
                })
                .switchIfEmpty(error(new ResponseStatusException(INTERNAL_SERVER_ERROR)));
    }

    @GetMapping(path = PATH_TEMPLATE_EMPLOYEE_ID, produces = APPLICATION_JSON_VALUE)
    public Mono<Employee> read(@NotBlank @PathVariable(name = PATH_NAME_EMPLOYEE_ID) final String id) {
        return get(id)
                .switchIfEmpty(error(new ResponseStatusException(NOT_FOUND)));
    }

    @ResponseStatus(NO_CONTENT)
    @PutMapping(path = PATH_TEMPLATE_EMPLOYEE_ID, consumes = APPLICATION_JSON_VALUE)
    public Mono<Boolean> update(@PathVariable(name = PATH_NAME_EMPLOYEE_ID) final String id,
                                @Valid @RequestBody final Employee entity) {
        if (!Objects.equals(entity.getId(), id)) {
            throw new ResponseStatusException(BAD_REQUEST, "$.id(" + entity.getId() + ") != /{id}(" + id + ")");
        }
        return set(entity)
                .filter(ok -> ok != null && ok)
                .switchIfEmpty(error(new ResponseStatusException(INTERNAL_SERVER_ERROR)));
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping(path = PATH_TEMPLATE_EMPLOYEE_ID)
    public Mono<Long> delete(@PathVariable(name = PATH_NAME_EMPLOYEE_ID) final String id) {
        return del(id)
                .filter(v -> v != null && v >= 0L)
                .switchIfEmpty(error(new ResponseStatusException(INTERNAL_SERVER_ERROR)));
    }
}
