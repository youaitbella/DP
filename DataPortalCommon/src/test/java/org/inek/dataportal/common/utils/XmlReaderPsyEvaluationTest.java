package org.inek.dataportal.common.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class XmlReaderPsyEvaluationTest {

    @Test
    void getStatementByIdTest() {
        String value1 = XmlReaderPsyEvaluation.getStatementById("Gruppe_2_3_8_9");
        String value2 = XmlReaderPsyEvaluation.getStatementById("KH_2_3_5_6_8_9");

        Assertions.assertThat(value1).isNotEmpty();
        Assertions.assertThat(value2).isNotEmpty();
    }
}