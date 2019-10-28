package org.inek.dataportal.care.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

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
            "771234"
            , "770000"
            , "777777"
            , "771234"
            , "779863"})
    void isValidVZNumberValidValuesTest(String value) {
        Assertions.assertThat(CareValueChecker.isFormalValidVzNumber(value)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "77123"
            , "120000"
            , "01"
            , "Keins"
            , ""})
    void isValidVZNumberNotValidValuesTest(String value) {
        Assertions.assertThat(CareValueChecker.isFormalValidVzNumber(value)).isFalse();
    }

}