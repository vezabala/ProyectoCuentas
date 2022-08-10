package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CantidadMapperTest {

    private CantidadMapper cantidadMapper;

    @BeforeEach
    public void setUp() {
        cantidadMapper = new CantidadMapperImpl();
    }
}
