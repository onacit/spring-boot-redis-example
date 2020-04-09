package com.github.onacit.web.bind;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.Objects;

import static com.github.onacit.web.bind.Employee.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Validated
@RestController
@RequestMapping(path = EmployeesController.REQUEST_MAPPING_PATH)
@RequiredArgsConstructor
@Slf4j
public class EmployeesController {

    public static final String REQUEST_MAPPING_PATH = "/employees";

    public static final String PATH_NAME_EMPLOYEE_ID = "employeeId";

    public static final String PATH_VALUE_EMPLOYEE_ID = ".+";

    public static final String PATH_TEMPLATE_EMPLOYEE_ID
            = "{" + PATH_NAME_EMPLOYEE_ID + ":" + PATH_VALUE_EMPLOYEE_ID + "}";

    @PostConstruct
    private void onPostConstruct() {
        redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(keySerializer());
        redisTemplate.setValueSerializer(valueSerializer());
        redisTemplate.afterPropertiesSet();
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@NotNull final ServerHttpRequest request,
                                    @Valid @RequestBody final Employee entity) {
        redisTemplate.opsForValue().set(entity.key(), entity);
        final URI location = UriComponentsBuilder.fromUri(request.getURI())
                .pathSegment(PATH_TEMPLATE_EMPLOYEE_ID)
                .build()
                .expand(entity.getId())
                .toUri();
        return created(location).build();
    }

    @GetMapping(path = PATH_TEMPLATE_EMPLOYEE_ID, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> read(@NotBlank @PathVariable(name = PATH_NAME_EMPLOYEE_ID) final String id) {
        final Employee entity = redisTemplate.opsForValue().get(key(id));
        if (entity == null) {
            throw new ResponseStatusException(NOT_FOUND, "no employee identified by /{id}(" + id + ")");
        }
        return ok(entity);
    }

    @ResponseStatus(NO_CONTENT)
    @PutMapping(path = PATH_TEMPLATE_EMPLOYEE_ID, consumes = APPLICATION_JSON_VALUE)
    public void update(@PathVariable(name = PATH_NAME_EMPLOYEE_ID) final String id,
                       @Valid @RequestBody final Employee entity) {
        if (!Objects.equals(entity.getId(), id)) {
            throw new ResponseStatusException(BAD_REQUEST, "$.id(" + entity.getId() + ") != /{id}(" + id + ")");
        }
        redisTemplate.opsForValue().set(key(id), entity); // void
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping(path = PATH_TEMPLATE_EMPLOYEE_ID)
    public void delete(@PathVariable(name = PATH_NAME_EMPLOYEE_ID) final String id) {
        final Boolean deleted = redisTemplate.delete(key(id));
    }

    private final RedisConnectionFactory redisConnectionFactory;

    private transient RedisTemplate<String, Employee> redisTemplate;
}
