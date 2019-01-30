package org.inek.dataportal.common.exceptions;

import org.apache.poi.ss.usermodel.Cell;

public class StringInNumericCellException extends Exception {

    private Cell _cell;

    public Cell getCell() {
        return _cell;
    }

    public StringInNumericCellException() {
    }

    public StringInNumericCellException(String message) {
        super(message);
    }

    public StringInNumericCellException(Cell cell) {
        _cell = cell;
    }

}
