package com.github.onacit.web.reactive;

import com.github.onacit.web.bind.AbstractEmployeesControllerIT;
import lombok.extern.slf4j.Slf4j;

import static com.github.onacit.web.reactive.ReactiveEmployeesController.REQUEST_MAPPING_PATH;

@Slf4j
class ReactiveEmployeesControllerIT extends AbstractEmployeesControllerIT {

    @Override
    protected String getRequestMappingPath() {
        return REQUEST_MAPPING_PATH;
    }
}