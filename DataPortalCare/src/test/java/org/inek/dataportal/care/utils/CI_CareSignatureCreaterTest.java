package org.inek.dataportal.care.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class CI_CareSignatureCreaterTest {

    @Test
    void createSignatureTest() {
        List<String> values = new ArrayList<>();
        for (int i = 0 ; i < 10000 ; i++) {
            String output = CareSignatureCreater.createPvSignature();
            values.add(output);
            Assertions.assertThat(output).startsWith("PV").hasSize(20);
            Assertions.assertThat(values).containsOnlyOnce(output);
        }
    }
}