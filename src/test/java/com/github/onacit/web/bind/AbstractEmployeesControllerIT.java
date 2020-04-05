package com.github.onacit.web.bind;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.PostConstruct;
import java.net.URI;

import static java.lang.System.nanoTime;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;
import static org.springframework.web.util.UriComponentsBuilder.fromUri;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public abstract class AbstractEmployeesControllerIT {

    protected abstract String getRequestMappingPath();

    @PostConstruct
    private void onPostConstruct() {
        baseUri = fromHttpUrl("http://localhost:" + localServerPort).path(getRequestMappingPath()).build().toUri();
    }

    @Test
    void testReadExpectStatusNotFoundForUnknown() {
        final URI uri = fromUri(baseUri).path("/" + nanoTime()).build().toUri();
        final ResponseEntity<Employee> responseEntity = restTemplate.getForEntity(uri, Employee.class);
        assertThat(responseEntity)
                .isNotNull()
                .satisfies(v -> {
                    assertEquals(NOT_FOUND, v.getStatusCode());
                });
    }

    @Test
    void testUpdateExpectStatusNoContent() {
        final Employee employee = new Employee();
        employee.setId(Long.toString(nanoTime()));
        employee.setName("name");
        final URI uri = fromUri(baseUri).pathSegment(employee.getId()).build().toUri();
        final MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(CONTENT_TYPE, APPLICATION_JSON_VALUE);
        final HttpEntity<String> requestEntity = new HttpEntity<>(employee.toJsonString(), headers);
        final ResponseEntity<Void> responseEntity = restTemplate.exchange(
                uri,
                PUT,
                requestEntity,
                Void.class);
        assertEquals(NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    void testDeleteExpectStatusNoContent() {
        final URI uri = fromUri(baseUri).path("/" + nanoTime()).build().toUri();
        final ResponseEntity<Void> responseEntity = restTemplate.exchange(
                uri,
                DELETE,
                null,
                Void.class);
        assertEquals(NO_CONTENT, responseEntity.getStatusCode());
    }

    @LocalServerPort
    private int localServerPort;

    private transient URI baseUri;

    @Autowired
    private TestRestTemplate restTemplate;
}