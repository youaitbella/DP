package org.inek.dataportal.common.helper.excelimport;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.inek.dataportal.common.exceptions.FormulaInCellException;
import org.inek.dataportal.common.exceptions.IntegerInDoubleCellException;
import org.inek.dataportal.common.exceptions.StringInNumericCellException;

public class CellImportHelper {

    public static String getStringFromCell(Cell cell) throws Exception {
        return getStringFromCell(cell, false);
    }

    public static String getStringFromCell(Cell cell, Boolean allowNullValue) throws Exception {
        if (cell == null && allowNullValue) return "";
        isFormulaInCellCheck(cell);
        if (cell.getCellTypeEnum().equals(CellType.NUMERIC)) {
            try {
                int numericCellValue = (int) cell.getNumericCellValue();
                return numericCellValue == cell.getNumericCellValue() ? String.valueOf(numericCellValue) : String.valueOf(cell.getNumericCellValue());
            } catch (Exception ex) {
                return String.valueOf(cell.getNumericCellValue());
            }
        }
        return cell.getStringCellValue();
    }

    public static Integer getIntegerFromCell(Cell cell) throws Exception {
        return getIntegerFromCell(cell, true, false);
    }

    public static Integer getIntegerFromCell(Cell cell, Boolean acceptDouble, Boolean allowNullValue) throws Exception {
        if (cell == null && allowNullValue) return 0;
        isFormulaInCellCheck(cell);
        if (cell.getCellTypeEnum().equals(CellType.NUMERIC)) {
            int numericCellValue = (int) cell.getNumericCellValue();
            if (numericCellValue == cell.getNumericCellValue() || acceptDouble) {
                return numericCellValue;
            } else {
                throw new IntegerInDoubleCellException(cell);
            }
        } else {
            if (allowNullValue) return 0;
            throw new StringInNumericCellException(cell);
        }
    }

    public static double getDoubleFromCell(Cell cell) throws Exception {
        return getDoubleFromCell(cell, false);
    }

    public static double getDoubleFromCell(Cell cell, Boolean allowNullValue) throws Exception {
        return getDoubleFromCell(cell, allowNullValue, false);
    }

    public static double getDoubleFromCell(Cell cell, Boolean allowNullValue, Boolean allowFormula) throws Exception {
        if (cell == null && allowNullValue) return 0;
        if (!allowFormula) {
            isFormulaInCellCheck(cell);
        }

        if (cell.getCellTypeEnum().equals(CellType.NUMERIC) || (cell.getCellTypeEnum().equals(CellType.FORMULA) && allowFormula)) {
            return cell.getNumericCellValue();
        } else if ("".equals(cell.getStringCellValue()) && allowNullValue) {
            return 0;
        }
        else {
            throw new StringInNumericCellException(cell);
        }
    }

    private static void isFormulaInCellCheck(Cell cell) throws Exception {
        if (cell == null) return;
        if (cell.getCellTypeEnum().equals(CellType.FORMULA)) {
            throw new FormulaInCellException(cell);
        }
    }
}
