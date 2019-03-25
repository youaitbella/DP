package org.inek.dataportal.calc.backingbean;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DataImporterValueImporterTest {

    @Test
    void parseIntegerWith_1_Returns1() {
        String value = "1";

        try {
            assertThat(NumberParser.parseInteger(value)).isEqualTo(1);
        } catch (Exception ex) {
            Assertions.assertThat(true).isFalse();
        }

    }

    @Test
    void parseIntegerWith_4Dot711_Returns_4711() {
        String value = "4.711";

        try {
            assertThat(NumberParser.parseInteger(value)).isEqualTo(4711);
        } catch (Exception ex) {
            Assertions.assertThat(true).isFalse();
        }

    }
    @Test
    void parseIntegerWith_EmptyString_ThrowsException() {

        String value = "";
        Throwable exception = assertThrows(ParseException.class, () -> {
            NumberParser.parseInteger(value);
        });

        assertThat(exception.getMessage()).isEqualTo(NumberParser.NOT_AN_INTEGER);
    }

    @Test
    void parseIntegerWith_1Comma1_ThrowsException() {
        String value = "1,1";
        Throwable exception = assertThrows(ParseException.class, () -> {
            NumberParser.parseInteger(value);
        });

        assertThat(exception.getMessage()).isEqualTo(NumberParser.NOT_AN_INTEGER);
    }

    @Test
    void parseIntegerWith_a_ThrowsException() {
        String value = "a";
        Throwable exception = assertThrows(ParseException.class, () -> {
            NumberParser.parseInteger(value);
        });

        assertThat(exception.getMessage()).isEqualTo(NumberParser.NOT_AN_INTEGER);
    }

    @Test
    void parseDoubleWith_1_Returns_1() {
        String value = "1";

        try {
            Assertions.assertThat(NumberParser.parseDouble(value)).isEqualTo(1);
        } catch (Exception ex) {
            Assertions.assertThat(true).isFalse();
        }

    }

    @Test
    void parseDoubleWith_1Comma1_Returns_1Dot1() {
        String value = "1,1";

        try {
            Assertions.assertThat(NumberParser.parseDouble(value)).isEqualTo(1.1);
        } catch (Exception ex) {
            Assertions.assertThat(true).isFalse();
        }

    }

    @Test
    void parseDoubleWith_4Dot711Comma1_Returns_4711Dot1() {
        String value = "4711,1";

        try {
            Assertions.assertThat(NumberParser.parseDouble(value)).isEqualTo(4711.1);
        } catch (Exception ex) {
            Assertions.assertThat(true).isFalse();
        }

    }

    @Test
    void parseDoubleWith_Empty_ThrowsException() {

        String value = "";

        try {
            Assertions.assertThat(NumberParser.parseDouble(value)).isEqualTo(1);
        } catch (Exception ex) {
            Assertions.assertThat(ex).isInstanceOf(ParseException.class);
        }

    }

    @Test
    void parseDoubleWith_a_ThrowsException() {
        String value = "a";
        Throwable exception = assertThrows(ParseException.class, () -> {
            NumberParser.parseDouble(value);
        });

        assertThat(exception.getMessage()).isEqualTo(NumberParser.NOT_A_NUMBER);
    }
}