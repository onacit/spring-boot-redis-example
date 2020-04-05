package com.github.onacit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {

    public static void main(final String... args) {
        if (true) {
            final ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        } else {
            final ConfigurableApplicationContext context = new SpringApplicationBuilder(Application.class)
                    .web(WebApplicationType.REACTIVE)
                    .run(args);
        }
    }
}
