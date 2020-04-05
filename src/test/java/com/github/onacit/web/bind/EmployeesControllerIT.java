package com.github.onacit.web.bind;

import lombok.extern.slf4j.Slf4j;

import static com.github.onacit.web.bind.EmployeesController.REQUEST_MAPPING_PATH;

@Slf4j
class EmployeesControllerIT extends AbstractEmployeesControllerIT {

    @Override
    protected String getRequestMappingPath() {
        return REQUEST_MAPPING_PATH;
    }
}