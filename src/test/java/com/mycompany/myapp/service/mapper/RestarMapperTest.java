package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RestarMapperTest {

    private RestarMapper restarMapper;

    @BeforeEach
    public void setUp() {
        restarMapper = new RestarMapperImpl();
    }
}
