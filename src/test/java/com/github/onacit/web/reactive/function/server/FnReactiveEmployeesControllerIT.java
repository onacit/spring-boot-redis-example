package com.github.onacit.web.reactive.function.server;

import com.github.onacit.web.bind.AbstractEmployeesControllerIT;
import lombok.extern.slf4j.Slf4j;

import static com.github.onacit.web.reactive.function.server.FnReactiveEmployeesRouter.REQUEST_MAPPING_PATH;

@Slf4j
class FnReactiveEmployeesControllerIT extends AbstractEmployeesControllerIT {

    @Override
    protected String getRequestMappingPath() {
        return REQUEST_MAPPING_PATH;
    }
}