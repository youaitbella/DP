package org.inek.dataportal.care.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class CareValueCheckerTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "1234"
            , "0123"
            , "8888"
            , "9999"
            , "0000"})
    void isValidFabNumberValidValuesTest(String value) {
        Assertions.assertThat(CareValueChecker.isValidFabNumber(value)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "234"
            , ""
            , "01234"
            , "a"
            , "fab1"
            , "124556"})
    void isValidFabNumberNotValidValuesTest(String value) {
        Assertions.assertThat(CareValueChecker.isValidFabNumber(value)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "771234000"
            , "770000000"
            , "777777000"
            , "771234000"
            , "779863000"})
    void isValidVZNumberValidValuesTest(String value) {
        Assertions.assertThat(CareValueChecker.isFormalValidVzNumber(value)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "77123"
            , "120000"
            , "01"
            , "771234001"
            , "771234"
            , "Keins"
            , ""})
    void isValidVZNumberNotValidValuesTest(String value) {
        Assertions.assertThat(CareValueChecker.isFormalValidVzNumber(value)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "771234000"
            , "  770000000  "
            , " 777777000 kl"
            , "   \t  771234000   hhh"
            , "779863000    xxx"})
    void startsWithFormalValidVzNumber(String value) {
        Assertions.assertThat(CareValueChecker.startsWithFormalValidVzNumber(value)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "771234000xxx"
            , "x  770000000  "
            , "7777000 kl"
            , "000   hhh"
            , "77986300x0xxx"})
    void doesNotStartWithFormalValidVzNumber(String value) {
        Assertions.assertThat(CareValueChecker.startsWithFormalValidVzNumber(value)).isFalse();
    }

    @ParameterizedTest
    @CsvSource({
            "771234000, 771234000"
            , "  770000000  , 770000000"
            , " 777777000 kl, 777777000"
            , "   \t  771234000   hhh, 771234000"
            , "779863000    xxx, 779863000"
            , "771234000xxx, 0"
            , "x  770000000  , 0"
            , "7777000 kl, 0"
            , "000   hhh, 0"
            , "77986300x0xxx, 0"})
    void extractFormalValidVzNumber(String value, int number) {
        Assertions.assertThat(CareValueChecker.extractFormalValidVzNumber(value)).isEqualTo(number);
    }

}