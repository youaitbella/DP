package org.inek.dataportal.common.faceletvalidators;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class PredominantPrintableAsciiValidatorTest {

    @Test
    void isValidNameWithValidTextsTest() {
        List<String> values = new ArrayList<>();
        values.add("Küche");
        values.add("Ergänzt");
        values.add("Ergönzt");
        values.add("KÜche");
        values.add("ErgÄnzt");
        values.add("ErgÖnzt");
        values.add("saß sadsa dsa da sdsa sf safsa fsa fsafsaklfasklfaf safsaf asklf aslfk asfl ksafa");

        PredominantPrintableAsciiValidator t = new PredominantPrintableAsciiValidator();

        for (String value : values) {
            Assertions.assertThat(t.isValidName(value)).as(value).isTrue();
        }
    }
}