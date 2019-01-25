package org.inek.dataportal.common.data.KhComparison.checker;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RenumerationCheckerTest {

    @Test
    void isFormalValidEtTest() {
        List<String> validEts = new ArrayList<>();
        validEts.add("ET01.02");
        validEts.add("ET99.99");
        validEts.add("ET00.00");

        List<String> notValidEts = new ArrayList<>();
        notValidEts.add("E01.02");
        notValidEts.add("ET123.99");
        notValidEts.add("ET00.123");
        notValidEts.add("00.123");
        notValidEts.add("et00.23");
        notValidEts.add("ET00,23");
        notValidEts.add("ET00.23 ");
        notValidEts.add("ET00.23.");
        notValidEts.add("ET00.23,");
        notValidEts.add("");

        for (String value : validEts) {
            Assertions.assertThat(RenumerationChecker.isFormalValidEt(value)).as(value).isTrue();
        }

        for (String value : notValidEts) {
            Assertions.assertThat(RenumerationChecker.isFormalValidEt(value)).as(value).isFalse();
        }
    }

    @Test
    void isFormalValidZeTest() {
        List<String> validEZes = new ArrayList<>();
        validEZes.add("ZE01.02");
        validEZes.add("ZE99.99");
        validEZes.add("ZE00.00");
        validEZes.add("ZP2018-33");
        validEZes.add("ZP2017-26.010");
        validEZes.add("ZP2019-99.999");
        validEZes.add("ZP2019-00.000");

        List<String> notValidZes = new ArrayList<>();
        notValidZes.add("ZE01.02 ");
        notValidZes.add("ZE123.99");
        notValidZes.add("ZP3015.23");
        notValidZes.add("ZE00.123");
        notValidZes.add("ze00.23");
        notValidZes.add("ZP2018.23");
        notValidZes.add("ZP2018-23,123");
        notValidZes.add("ZP2018-23.");
        notValidZes.add("ZP2018-23,");
        notValidZes.add("ZP2018-23 ");
        notValidZes.add("");

        for (String value : validEZes) {
            Assertions.assertThat(RenumerationChecker.isFormalValidZe(value)).as(value).isTrue();
        }

        for (String value : notValidZes) {
            Assertions.assertThat(RenumerationChecker.isFormalValidZe(value)).as(value).isFalse();
        }
    }

    @Test
    void isFormalValidPeppTest() {
        List<String> validPepps = new ArrayList<>();
        validPepps.add("PA03A");
        validPepps.add("P003B");
        validPepps.add("PK01A");
        validPepps.add("PP10A");
        validPepps.add("PA14A");
        validPepps.add("TK14Z");
        validPepps.add("PK10Z");
        validPepps.add("TA20Z");
        validPepps.add("PK14B");
        validPepps.add("TA15Z");
        validPepps.add("PA03B");
        validPepps.add("PK02Z");
        validPepps.add("PK14C");
        validPepps.add("PA15A");
        validPepps.add("TA02Z");
        validPepps.add("PK01Z");
        validPepps.add("PP04B");
        validPepps.add("PA02B");
        validPepps.add("PA02A");
        validPepps.add("PA02D");
        validPepps.add("PK04B");
        validPepps.add("PA04B");
        validPepps.add("P003A");
        validPepps.add("PP04A");
        validPepps.add("PK04A");
        validPepps.add("PA02C");
        validPepps.add("TA19Z");
        validPepps.add("TP20Z");
        validPepps.add("PA04A");
        validPepps.add("PK14A");
        validPepps.add("PA14B");
        validPepps.add("PP10B");
        validPepps.add("PA01B");
        validPepps.add("P002Z");
        validPepps.add("PA01A");
        validPepps.add("PA04C");
        validPepps.add("TK04Z");
        validPepps.add("PK01B");
        validPepps.add("PK02B");
        validPepps.add("PK02A");
        validPepps.add("PA15B");
        validPepps.add("PP14Z");
        validPepps.add("P003C");
        validPepps.add("PA15C");
        validPepps.add("PK03Z");

        List<String> notValidPepps = new ArrayList<>();
        notValidPepps.add("P003C ");
        notValidPepps.add("p003C");
        notValidPepps.add("T003C");
        notValidPepps.add("P0002Z");
        notValidPepps.add("P003C,");
        notValidPepps.add("P003C.");
        notValidPepps.add("P0033");
        notValidPepps.add("AK03C");
        notValidPepps.add("PP0C");
        notValidPepps.add("sad");
        notValidPepps.add("");

        for (String value : validPepps) {
            Assertions.assertThat(RenumerationChecker.isFormalValidPepp(value)).as(value).isTrue();
        }

        for (String value : notValidPepps) {
            Assertions.assertThat(RenumerationChecker.isFormalValidPepp(value)).as(value).isFalse();
        }
    }
}