package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RestarTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Restar.class);
        Restar restar1 = new Restar();
        restar1.setId(1L);
        Restar restar2 = new Restar();
        restar2.setId(restar1.getId());
        assertThat(restar1).isEqualTo(restar2);
        restar2.setId(2L);
        assertThat(restar1).isNotEqualTo(restar2);
        restar1.setId(null);
        assertThat(restar1).isNotEqualTo(restar2);
    }
}
