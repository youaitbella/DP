package org.inek.dataportal.common.exceptions;

import org.apache.poi.ss.usermodel.Cell;

public class IntegerInDoubleCellException extends Exception {

    private Cell _cell;

    public Cell getCell() {
        return _cell;
    }

    public IntegerInDoubleCellException() {
    }

    public IntegerInDoubleCellException(String message) {
        super(message);
    }

    public IntegerInDoubleCellException(Cell cell) {
        _cell = cell;
    }
}
