package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SumarTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sumar.class);
        Sumar sumar1 = new Sumar();
        sumar1.setId(1L);
        Sumar sumar2 = new Sumar();
        sumar2.setId(sumar1.getId());
        assertThat(sumar1).isEqualTo(sumar2);
        sumar2.setId(2L);
        assertThat(sumar1).isNotEqualTo(sumar2);
        sumar1.setId(null);
        assertThat(sumar1).isNotEqualTo(sumar2);
    }
}
