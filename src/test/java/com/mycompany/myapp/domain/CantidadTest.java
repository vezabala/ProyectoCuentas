package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CantidadTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cantidad.class);
        Cantidad cantidad1 = new Cantidad();
        cantidad1.setId(1L);
        Cantidad cantidad2 = new Cantidad();
        cantidad2.setId(cantidad1.getId());
        assertThat(cantidad1).isEqualTo(cantidad2);
        cantidad2.setId(2L);
        assertThat(cantidad1).isNotEqualTo(cantidad2);
        cantidad1.setId(null);
        assertThat(cantidad1).isNotEqualTo(cantidad2);
    }
}
