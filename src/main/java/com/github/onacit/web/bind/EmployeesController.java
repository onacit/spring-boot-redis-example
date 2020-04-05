package com.github.onacit.web.bind;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
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

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

import static com.github.onacit.web.bind.Employee.key;
import static com.github.onacit.web.bind.Employee.keySerializer;
import static com.github.onacit.web.bind.Employee.valueSerializer;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
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

    //@PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(
//            final HttpRequest request,
//            final HttpServletRequest request,
            @Valid @RequestBody final Employee entity) {
        redisTemplate.opsForValue().set(entity.key(), entity);
//        final URI location = fromCurrentRequestUri() // ServletUriComponentsBuilder
//                .pathSegment(PATH_TEMPLATE_EMPLOYEE_ID)
//                .build()
//                .expand(entity.getId())
//                .toUri();
//        final URI location = fromHttpRequest(request) // UriComponentsBuilder
//                .pathSegment(PATH_TEMPLATE_EMPLOYEE_ID)
//                .build()
//                .expand(entity.getId())
//                .toUri();
//        final URI location = fromHttpUrl(request.getRequestURI())
//                .pathSegment(PATH_TEMPLATE_EMPLOYEE_ID)
//                .build()
//                .expand(entity.getId())
//                .toUri();
//        return ResponseEntity.created(location).build();
        return null;
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
