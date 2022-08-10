package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SumarDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SumarDTO.class);
        SumarDTO sumarDTO1 = new SumarDTO();
        sumarDTO1.setId(1L);
        SumarDTO sumarDTO2 = new SumarDTO();
        assertThat(sumarDTO1).isNotEqualTo(sumarDTO2);
        sumarDTO2.setId(sumarDTO1.getId());
        assertThat(sumarDTO1).isEqualTo(sumarDTO2);
        sumarDTO2.setId(2L);
        assertThat(sumarDTO1).isNotEqualTo(sumarDTO2);
        sumarDTO1.setId(null);
        assertThat(sumarDTO1).isNotEqualTo(sumarDTO2);
    }
}
