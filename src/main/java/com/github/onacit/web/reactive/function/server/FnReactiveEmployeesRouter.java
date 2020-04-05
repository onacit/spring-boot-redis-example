package com.github.onacit.web.reactive.function.server;

import com.github.onacit.web.reactive.ReactiveEmployeesController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.github.onacit.web.bind.EmployeesController.PATH_TEMPLATE_EMPLOYEE_ID;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@Slf4j
public class FnReactiveEmployeesRouter {

    public static final String REQUEST_MAPPING_PATH = "/fn" + ReactiveEmployeesController.REQUEST_MAPPING_PATH;

    @Bean
    public RouterFunction<ServerResponse> routerFunction(final FnReactiveEmployeesHandler handler) {
        final RouterFunction<ServerResponse> nested
                = route(POST("/").and(contentType(APPLICATION_JSON)), handler::create)
                .andRoute(GET("/" + PATH_TEMPLATE_EMPLOYEE_ID).and(accept(APPLICATION_JSON)), handler::read)
                .andRoute(PUT("/" + PATH_TEMPLATE_EMPLOYEE_ID).and(contentType(APPLICATION_JSON)), handler::update)
                .andRoute(DELETE("/" + PATH_TEMPLATE_EMPLOYEE_ID), handler::delete);
        return nest(path(REQUEST_MAPPING_PATH), nested);
    }
}
