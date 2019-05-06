package org.inek.dataportal.common.helper.excelimport;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.assertj.core.api.Assertions;
import org.inek.dataportal.common.exceptions.FormulaInCellException;
import org.inek.dataportal.common.exceptions.IntegerInDoubleCellException;
import org.inek.dataportal.common.exceptions.StringInNumericCellException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;


class CellImportHelperTest {

    @Test
    void getStringFromCellWithNullTest() {
        Assertions.assertThatCode(() -> {
            Assertions.assertThat(CellImportHelper.getStringFromCell(null, true)).isEqualTo("");
        }).doesNotThrowAnyException();
    }

    @Test
    void getStringFromCellWithFormulaTest() {
        Cell newCell = createNewCell();
        newCell.setCellType(CellType.FORMULA);

        Throwable thrown = Assertions.catchThrowable(() -> {
            CellImportHelper.getStringFromCell(newCell);
        });

        Assertions.assertThat(thrown).isInstanceOf(FormulaInCellException.class);
        Assertions.assertThat(((FormulaInCellException) thrown).getCell()).isEqualTo(newCell);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Test", " Test", "T e s t", "1234", "0123", "01", "äöü,.;'", "Testsatz: \"dasd\".", "", "123.00"})
    void getStringFromCellWithValidStringsTest(String cellValue) {
        Cell newCell = createNewCell(cellValue);
        Assertions.assertThatCode(() -> {
            Assertions.assertThat(CellImportHelper.getStringFromCell(newCell)).isEqualTo(cellValue);
        }).doesNotThrowAnyException();
    }


    @ParameterizedTest
    @CsvSource({
            "2, 2",
            "12, 12",
            "12245680, 12245680"
    })
    void getStringFromCellWithIntegerNumberTest(int cellValue, String expected) {
        Cell newCell = createNewCell(cellValue);
        Assertions.assertThatCode(() -> {
            Assertions.assertThat(CellImportHelper.getStringFromCell(newCell)).isEqualTo(expected);
        }).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @CsvSource({
            "2.20, 2.2",
            "12.235, 12.235",
            "1224568.001, 1224568.001"
    })
    void getStringFromCellWithDoubleNumberTest(double cellValue, String expected) {
        Cell newCell = createNewCell(cellValue);
        Assertions.assertThatCode(() -> {
            Assertions.assertThat(CellImportHelper.getStringFromCell(newCell)).isEqualTo(expected);
        }).doesNotThrowAnyException();
    }

    @Test
    void getIntegerFromCellWithNullTest() {
        Assertions.assertThatCode(() -> {
            Assertions.assertThat(CellImportHelper.getIntegerFromCell(null, true, true)).isEqualTo(0);
        }).doesNotThrowAnyException();
    }

    @Test
    void getIntegerFromCellWithFormulaTest() {
        Cell newCell = createNewCell();
        newCell.setCellType(CellType.FORMULA);

        Throwable thrown = Assertions.catchThrowable(() -> {
            CellImportHelper.getIntegerFromCell(newCell);
        });

        Assertions.assertThat(thrown).isInstanceOf(FormulaInCellException.class);
        Assertions.assertThat(((FormulaInCellException) thrown).getCell()).isEqualTo(newCell);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2345, 100000, 999999999})
    void getIntegerFromCellWithValidIntegersWithoutAcceptDoubleTest(int cellValue) {
        Cell newCell = createNewCell(cellValue);
        Assertions.assertThatCode(() -> {
            Assertions.assertThat(CellImportHelper.getIntegerFromCell(newCell)).isEqualTo(cellValue);
        }).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(doubles = {2.20, 12.235, 1224568.001})
    void getIntegerFromCellWithDoublesWithoutAcceptDoubleTest(double cellValue) {
        Cell newCell = createNewCell(cellValue);

        Throwable thrown = Assertions.catchThrowable(() -> {
            CellImportHelper.getIntegerFromCell(newCell, false, false);
        });

        Assertions.assertThat(thrown).isInstanceOf(IntegerInDoubleCellException.class);
        Assertions.assertThat(((IntegerInDoubleCellException) thrown).getCell()).isEqualTo(newCell);
    }

    @ParameterizedTest
    @CsvSource({
            "1.10, 1",
            "2345.00, 2345",
            "2345.216584, 2345",
            "2345.001, 2345"
    })
    void getIntegerFromCellWithDoublesWithAcceptDoubleTest(double cellValue, int expected) {
        Cell newCell = createNewCell(cellValue);
        Assertions.assertThatCode(() -> {
            Assertions.assertThat(CellImportHelper.getIntegerFromCell(newCell, true, true)).isEqualTo(expected);
        }).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {"1.10", "", "Testen"})
    void getIntegerFromCellWithStringValuesTest(String cellValue) {
        Cell newCell = createNewCell(cellValue);

        Throwable thrown = Assertions.catchThrowable(() -> {
            CellImportHelper.getIntegerFromCell(newCell, false, false);
        });

        Assertions.assertThat(thrown).isInstanceOf(StringInNumericCellException.class);
        Assertions.assertThat(((StringInNumericCellException) thrown).getCell()).isEqualTo(newCell);
    }

    @Test
    void getDoubleFromCellWithNullTest() {
        Assertions.assertThatCode(() -> {
            Assertions.assertThat(CellImportHelper.getDoubleFromCell(null, true)).isEqualTo(0);
        }).doesNotThrowAnyException();
    }

    @Test
    void getDoubleFromCellWithFormulaTest() {
        Cell newCell = createNewCell();
        newCell.setCellType(CellType.FORMULA);

        Throwable thrown = Assertions.catchThrowable(() -> {
            CellImportHelper.getDoubleFromCell(newCell);
        });

        Assertions.assertThat(thrown).isInstanceOf(FormulaInCellException.class);
        Assertions.assertThat(((FormulaInCellException) thrown).getCell()).isEqualTo(newCell);
    }

    @ParameterizedTest
    @CsvSource({
            "1.0, 1.0",
            "0.2220, 0.222",
            "1.12351, 1.12351"
    })
    void getDoubleFromCellWithValidDoublesTest(double cellValue, double expected) {
        Cell newCell = createNewCell(cellValue);
        Assertions.assertThatCode(() -> {
            Assertions.assertThat(CellImportHelper.getDoubleFromCell(newCell)).isEqualTo(expected);
        }).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1.0",
            "20, 20.0",
            "21534, 21534.0"
    })
    void getDoubleFromCellWithValidIntegersTest(int cellValue, double expected) {
        Cell newCell = createNewCell(cellValue);
        Assertions.assertThatCode(() -> {
            Assertions.assertThat(CellImportHelper.getDoubleFromCell(newCell)).isEqualTo(expected);
        }).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {"1.10", "Testen"})
    void getDoubleFromCellWithStringValuesTest(String cellValue) {
        Cell newCell = createNewCell(cellValue);

        Throwable thrown = Assertions.catchThrowable(() -> {
            CellImportHelper.getDoubleFromCell(newCell );
        });

        Assertions.assertThat(thrown).isInstanceOf(StringInNumericCellException.class);
        Assertions.assertThat(((StringInNumericCellException) thrown).getCell()).isEqualTo(newCell);
    }

    @Test
    void getDoubleFromCellWithEmptyStringValuesTest() {
        Assertions.assertThatCode(() -> {
            Assertions.assertThat(CellImportHelper.getDoubleFromCell(createNewCell(""), true)).isEqualTo(0.0);
            Assertions.assertThat(CellImportHelper.getDoubleFromCell(createNewCell(null), true)).isEqualTo(0.0);
        }).doesNotThrowAnyException();
    }


    private Cell createNewCell() {
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet();
        Row row = sheet.createRow(0);
        return row.createCell(0);
    }

    private Cell createNewCell(String value) {
        Cell cell = createNewCell();
        cell.setCellValue(value);
        return cell;
    }

    private Cell createNewCell(int value) {
        Cell cell = createNewCell();
        cell.setCellValue(value);
        return cell;
    }

    private Cell createNewCell(double value) {
        Cell cell = createNewCell();
        cell.setCellValue(value);
        return cell;
    }
}