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

import static org.junit.jupiter.api.Assertions.fail;



class CellImportHelperTest {

    @Test
    void getStringFromCellWithNullTest() {
        try {
            Assertions.assertThat(CellImportHelper.getStringFromCell(null, true)).isEqualTo("");
        } catch (Exception ex) {
            Assertions.assertThat(true).isFalse();
        }
    }

    @Test
    void getStringFromCellWithFormulaTest() {
        Cell newCell = createNewCell();

        newCell.setCellType(CellType.FORMULA);

        try {
            Assertions.assertThat(CellImportHelper.getStringFromCell(newCell)).isEqualTo("Test");
        } catch (Exception ex) {
            Assertions.assertThat(ex).isExactlyInstanceOf(FormulaInCellException.class);
            Assertions.assertThat(((FormulaInCellException) ex).getCell()).isEqualTo(newCell);
        }
    }

    @Test
    void getStringFromCellWithValidStringsTest() {
        List<Tuple<Cell, String>> values = new ArrayList<Tuple<Cell, String>>();

        values.add(new Tuple<Cell, String>(createNewCell("Test"), "Test"));
        values.add(new Tuple<Cell, String>(createNewCell(" Test"), " Test"));
        values.add(new Tuple<Cell, String>(createNewCell("T e s t"), "T e s t"));
        values.add(new Tuple<Cell, String>(createNewCell("1234"), "1234"));
        values.add(new Tuple<Cell, String>(createNewCell("0123"), "0123"));
        values.add(new Tuple<Cell, String>(createNewCell("01"), "01"));
        values.add(new Tuple<Cell, String>(createNewCell("äöü,.;'"), "äöü,.;'"));
        values.add(new Tuple<Cell, String>(createNewCell("Testsatz: \"dasd\"."), "Testsatz: \"dasd\"."));
        values.add(new Tuple<Cell, String>(createNewCell(""), ""));


        for (Tuple<Cell, String> pair : values) {
            try {
                Assertions.assertThat(CellImportHelper.getStringFromCell(pair.cell)).isEqualTo(pair.expected);
            } catch (Exception ex) {
                Assertions.assertThat(true).isFalse();
            }
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"Test", " Test", "T e s t", "1234", "0123", "01", "äöü,.;'", "Testsatz: \"dasd\".", "", "123.00"})
    void getStringFromCellWithValidStringsTest(String cellValue) {
        Cell cell = createNewCell(cellValue);
        try {
            Assertions.assertThat(CellImportHelper.getStringFromCell(cell)).isEqualTo(cellValue);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void getStringFromCellWithIntegerNumberTest() {
        List<Tuple<Cell, String>> values = new ArrayList<Tuple<Cell, String>>();

        values.add(new Tuple<Cell, String>(createNewCell(2), "2"));
        values.add(new Tuple<Cell, String>(createNewCell(12), "12"));
        values.add(new Tuple<Cell, String>(createNewCell(1224568.00), "1224568"));

        for (Tuple<Cell, String> pair : values) {
            try {
                Assertions.assertThat(CellImportHelper.getStringFromCell(pair.cell)).isEqualTo(pair.expected);
            } catch (Exception ex) {
                Assertions.assertThat(true).isFalse();
            }
        }
    }

    @Test
    void getStringFromCellWithDoubleNumberTest() {
        List<Tuple<Cell, String>> values = new ArrayList<Tuple<Cell, String>>();

        values.add(new Tuple<Cell, String>(createNewCell(2.20), "2.2"));
        values.add(new Tuple<Cell, String>(createNewCell(12.235), "12.235"));
        values.add(new Tuple<Cell, String>(createNewCell(1224568.001), "1224568.001"));

        for (Tuple<Cell, String> pair : values) {
            try {
                Assertions.assertThat(CellImportHelper.getStringFromCell(pair.cell)).isEqualTo(pair.expected);
            } catch (Exception ex) {
                Assertions.assertThat(true).isFalse();
            }
        }
    }

    @ParameterizedTest
    @CsvSource({
            "2, 2",
            "12, 12",
            "1224568.00, 1224568",
            "2.20, 2.2",
            "12.235, 12.235",
            "1224568.001, 1224568.001"
    })
    void getStringFromCellWithValidNumbersTest(double cellValue, String expected) {
        Cell cell = createNewCell(cellValue);
        try {
            Assertions.assertThat(CellImportHelper.getStringFromCell(cell)).isEqualTo(expected);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void getIntegerFromCellWithNullTest() {
        try {
            Assertions.assertThat(CellImportHelper.getIntegerFromCell(null, true, true)).isEqualTo(0);
        } catch (Exception ex) {
            Assertions.assertThat(true).isFalse();
        }
    }

    @Test
    void getIntegerFromCellWithFormulaTest() {
        Cell newCell = createNewCell();

        newCell.setCellType(CellType.FORMULA);

        try {
            Assertions.assertThat(CellImportHelper.getIntegerFromCell(newCell)).isEqualTo(1);
        } catch (Exception ex) {
            Assertions.assertThat(ex).isExactlyInstanceOf(FormulaInCellException.class);
            Assertions.assertThat(((FormulaInCellException) ex).getCell()).isEqualTo(newCell);
        }
    }

    @Test
    void getIntegerFromCellWithValidIntegersWithoutAcceptDoubleTest() {
        List<Tuple<Cell, Integer>> values = new ArrayList<Tuple<Cell, Integer>>();

        values.add(new Tuple<Cell, Integer>(createNewCell(1), 1));
        values.add(new Tuple<Cell, Integer>(createNewCell(2345), 2345));
        values.add(new Tuple<Cell, Integer>(createNewCell(100000), 100000));
        values.add(new Tuple<Cell, Integer>(createNewCell(999999999), 999999999));


        for (Tuple<Cell, Integer> pair : values) {
            try {
                Assertions.assertThat(CellImportHelper.getIntegerFromCell(pair.cell)).isEqualTo(pair.expected);
            } catch (Exception ex) {
                Assertions.assertThat(true).isFalse();
            }
        }

    }

    @Test
    void getIntegerFromCellWithDoublesWithoutAcceptDoubleTest() {
        List<Tuple<Cell, Integer>> values = new ArrayList<Tuple<Cell, Integer>>();

        values.add(new Tuple<Cell, Integer>(createNewCell(1.10), 1111));
        values.add(new Tuple<Cell, Integer>(createNewCell(2345.10), 2111345));

        for (Tuple<Cell, Integer> pair : values) {
            try {
                Assertions.assertThat(CellImportHelper.getIntegerFromCell(pair.cell, false, false)).isEqualTo(pair.expected);
            } catch (Exception ex) {
                Assertions.assertThat(ex).isExactlyInstanceOf(IntegerInDoubleCellException.class);
                Assertions.assertThat(((IntegerInDoubleCellException) ex).getCell()).isEqualTo(pair.cell);
            }
        }
    }

    @Test
    void getIntegerFromCellWithDoublesWithtAcceptDoubleTest() {
        List<Tuple<Cell, Integer>> values = new ArrayList<Tuple<Cell, Integer>>();

        values.add(new Tuple<Cell, Integer>(createNewCell(1.10), 1));
        values.add(new Tuple<Cell, Integer>(createNewCell(2345.00), 2345));
        values.add(new Tuple<Cell, Integer>(createNewCell(2345.216584), 2345));
        values.add(new Tuple<Cell, Integer>(createNewCell(2345.001), 2345));

        for (Tuple<Cell, Integer> pair : values) {
            try {
                Assertions.assertThat(CellImportHelper.getIntegerFromCell(pair.cell)).isEqualTo(pair.expected);
            } catch (Exception ex) {
                Assertions.assertThat(true).isFalse();
            }
        }
    }

    @Test
    void getIntegerFromCellWithStringValuesTest() {
        List<Tuple<Cell, Integer>> values = new ArrayList<Tuple<Cell, Integer>>();

        values.add(new Tuple<Cell, Integer>(createNewCell("1.10"), 104455145));
        values.add(new Tuple<Cell, Integer>(createNewCell("Testen"), 2345));

        for (Tuple<Cell, Integer> pair : values) {
            try {
                Assertions.assertThat(CellImportHelper.getIntegerFromCell(pair.cell)).isEqualTo(pair.expected);
            } catch (Exception ex) {
                Assertions.assertThat(ex).isExactlyInstanceOf(StringInNumericCellException.class);
                Assertions.assertThat(((StringInNumericCellException) ex).getCell()).isEqualTo(pair.cell);
            }
        }
    }

    @Test
    void getDoubleFromCellWithNullTest() {
        try {
            Assertions.assertThat(CellImportHelper.getDoubleFromCell(null, true)).isEqualTo(0);
        } catch (Exception ex) {
            Assertions.assertThat(true).isFalse();
        }
    }

    @Test
    void getDoubleFromCellWithFormulaTest() {
        Cell newCell = createNewCell();

        newCell.setCellType(CellType.FORMULA);

        try {
            Assertions.assertThat(CellImportHelper.getDoubleFromCell(newCell)).isEqualTo(1);
        } catch (Exception ex) {
            Assertions.assertThat(ex).isExactlyInstanceOf(FormulaInCellException.class);
            Assertions.assertThat(((FormulaInCellException) ex).getCell()).isEqualTo(newCell);
        }
    }

    @Test
    void getDoubleFromCellWithValidDoublesTest() {
        List<Tuple<Cell, Double>> values = new ArrayList<Tuple<Cell, Double>>();

        values.add(new Tuple<Cell, Double>(createNewCell(1.0), 1.0));
        values.add(new Tuple<Cell, Double>(createNewCell(0.2220), 0.222));
        values.add(new Tuple<Cell, Double>(createNewCell(1.12351), 1.12351));

        for (Tuple<Cell, Double> pair : values) {
            try {
                Assertions.assertThat(CellImportHelper.getDoubleFromCell(pair.cell)).isEqualTo(pair.expected);
            } catch (Exception ex) {
                Assertions.assertThat(true).isFalse();
            }
        }
    }

    @Test
    void getDoubleFromCellWithValidIntegersTest() {
        List<Tuple<Cell, Double>> values = new ArrayList<Tuple<Cell, Double>>();

        values.add(new Tuple<Cell, Double>(createNewCell(1), 1.0));
        values.add(new Tuple<Cell, Double>(createNewCell(20), 20.0));
        values.add(new Tuple<Cell, Double>(createNewCell(21534), 21534.0));

        for (Tuple<Cell, Double> pair : values) {
            try {
                Assertions.assertThat(CellImportHelper.getDoubleFromCell(pair.cell)).isEqualTo(pair.expected);
            } catch (Exception ex) {
                Assertions.assertThat(true).isFalse();
            }
        }
    }

    @Test
    void getDoubleFromCellWithStringValuesTest() {
        List<Tuple<Cell, Double>> values = new ArrayList<Tuple<Cell, Double>>();

        values.add(new Tuple<Cell, Double>(createNewCell("1.10"), 104455145.0));
        values.add(new Tuple<Cell, Double>(createNewCell("Testen"), 2345.0));

        for (Tuple<Cell, Double> pair : values) {
            try {
                Assertions.assertThat(CellImportHelper.getDoubleFromCell(pair.cell)).isEqualTo(pair.expected);
            } catch (Exception ex) {
                Assertions.assertThat(ex).isExactlyInstanceOf(StringInNumericCellException.class);
                Assertions.assertThat(((StringInNumericCellException) ex).getCell()).isEqualTo(pair.cell);
            }
        }
    }

    @Test
    void getDoubleFromCellWithEmptyStringValuesTest() {
        List<Tuple<Cell, Double>> values = new ArrayList<Tuple<Cell, Double>>();

        values.add(new Tuple<Cell, Double>(createNewCell(""), 0.0));
        values.add(new Tuple<Cell, Double>(null, 0.0));

        for (Tuple<Cell, Double> pair : values) {
            try {
                Assertions.assertThat(CellImportHelper.getDoubleFromCell(pair.cell, true)).isEqualTo(pair.expected);
            } catch (Exception ex) {
                Assertions.assertThat(true).isFalse();
            }
        }
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

    private class Tuple<X, Y> {
        public final X cell;
        public final Y expected;

        public Tuple(X cell, Y expected) {
            this.cell = cell;
            this.expected = expected;
        }
    }
}