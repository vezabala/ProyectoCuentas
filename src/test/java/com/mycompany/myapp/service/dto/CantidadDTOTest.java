package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CantidadDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CantidadDTO.class);
        CantidadDTO cantidadDTO1 = new CantidadDTO();
        cantidadDTO1.setId(1L);
        CantidadDTO cantidadDTO2 = new CantidadDTO();
        assertThat(cantidadDTO1).isNotEqualTo(cantidadDTO2);
        cantidadDTO2.setId(cantidadDTO1.getId());
        assertThat(cantidadDTO1).isEqualTo(cantidadDTO2);
        cantidadDTO2.setId(2L);
        assertThat(cantidadDTO1).isNotEqualTo(cantidadDTO2);
        cantidadDTO1.setId(null);
        assertThat(cantidadDTO1).isNotEqualTo(cantidadDTO2);
    }
}
