package com.github.onacit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.reactive.context.ReactiveWebApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@Slf4j
public class Application {

    public static void main(final String... args) {
        final ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        log.debug("reactive: {}", context instanceof ReactiveWebApplicationContext);
    }
}
