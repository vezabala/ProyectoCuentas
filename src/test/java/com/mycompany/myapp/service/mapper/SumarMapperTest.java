package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SumarMapperTest {

    private SumarMapper sumarMapper;

    @BeforeEach
    public void setUp() {
        sumarMapper = new SumarMapperImpl();
    }
}
