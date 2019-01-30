package org.inek.dataportal.common.exceptions;

import org.apache.poi.ss.usermodel.Cell;

public class FormulaInCellException extends Exception {

    private Cell _cell;

    public Cell getCell() {
        return _cell;
    }

    public FormulaInCellException() {
    }

    public FormulaInCellException(String message) {
        super(message);
    }

    public FormulaInCellException(Cell cell) {
        _cell = cell;
    }
}
