package com.github.onacit.web.bind;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

import static java.util.Objects.requireNonNull;

@Setter
@Getter
@Slf4j
public class Employee implements Serializable {

    public static StringRedisSerializer keySerializer() {
        return new StringRedisSerializer();
    }

    public static RedisSerializer<Employee> valueSerializer() {
        return new Jackson2JsonRedisSerializer<>(Employee.class);
    }

    public static String key(final String id) {
        return requireNonNull(id, "id is null");
    }

    /**
     * Returns the spring representation of the object.
     *
     * @return the spring representation of the object.
     */
    @Override
    public String toString() {
        return super.toString() + "{"
               + ",id=" + id
               + ",name=" + name
               + "}";
    }

    /**
     * Returns the JSON representation of the object. This method is only for unit testing.
     *
     * @return the JSON representation of the object.
     */
    public String toJsonString() {
        try {
            return new ObjectMapper().writer().writeValueAsString(this);
        } catch (final JsonProcessingException jpe) {
            throw new RuntimeException(jpe);
        }
    }

    public String key() {
        return key(id);
    }

    @NotBlank
    private String id;

    @NotBlank
    private String name;
}
