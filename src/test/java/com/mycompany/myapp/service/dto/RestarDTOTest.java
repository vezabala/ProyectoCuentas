package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RestarDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RestarDTO.class);
        RestarDTO restarDTO1 = new RestarDTO();
        restarDTO1.setId(1L);
        RestarDTO restarDTO2 = new RestarDTO();
        assertThat(restarDTO1).isNotEqualTo(restarDTO2);
        restarDTO2.setId(restarDTO1.getId());
        assertThat(restarDTO1).isEqualTo(restarDTO2);
        restarDTO2.setId(2L);
        assertThat(restarDTO1).isNotEqualTo(restarDTO2);
        restarDTO1.setId(null);
        assertThat(restarDTO1).isNotEqualTo(restarDTO2);
    }
}
