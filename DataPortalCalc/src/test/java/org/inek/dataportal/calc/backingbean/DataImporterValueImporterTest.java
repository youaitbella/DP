package org.inek.dataportal.calc.backingbean;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

class DataImporterValueImporterTest {

    @Test
    void parseIntegerTest() {

        String value = "";

        try {
            Assertions.assertThat(DataImporterValueImporter.parseInteger(value)).isEqualTo(1);
        }
        catch (Exception ex) {
            Assertions.assertThat(ex).isInstanceOf(ParseException.class);
        }

        String value1 = "1";

        try {
            Assertions.assertThat(DataImporterValueImporter.parseInteger(value1)).isEqualTo(1);
        }
        catch (Exception ex) {
            Assertions.assertThat(true).isFalse();
        }

        String value3 = "1,1";

        try {
            Assertions.assertThat(DataImporterValueImporter.parseInteger(value3)).isEqualTo(1);
        }
        catch (Exception ex) {
            Assertions.assertThat(ex).isInstanceOf(ParseException.class);
        }

        String value2 = "a";

        try {
            Assertions.assertThat(DataImporterValueImporter.parseInteger(value2)).isEqualTo(1);
        }
        catch (Exception ex) {
            Assertions.assertThat(ex).isInstanceOf(ParseException.class);
        }
    }

    @Test
    void parseDoubleTest() {

        String value = "";

        try {
            Assertions.assertThat(DataImporterValueImporter.parseDouble(value)).isEqualTo(1);
        }
        catch (Exception ex) {
            Assertions.assertThat(ex).isInstanceOf(ParseException.class);
        }

        String value1 = "1";

        try {
            Assertions.assertThat(DataImporterValueImporter.parseDouble(value1)).isEqualTo(1);
        }
        catch (Exception ex) {
            Assertions.assertThat(true).isFalse();
        }

        String value3 = "1,1";

        try {
            Assertions.assertThat(DataImporterValueImporter.parseDouble(value3)).isEqualTo(1.1);
        }
        catch (Exception ex) {
            Assertions.assertThat(true).isFalse();
        }

        String value2 = "a";

        try {
            Assertions.assertThat(DataImporterValueImporter.parseDouble(value2)).isEqualTo(1);
        }
        catch (Exception ex) {
            Assertions.assertThat(ex).isInstanceOf(ParseException.class);
        }
    }
}