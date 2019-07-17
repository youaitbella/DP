package org.inek.dataportal.common.exceptions;

import org.apache.poi.ss.usermodel.Cell;

public class ValueToLongCellException extends Exception {

    private Cell _cell;

    public Cell getCell() {
        return _cell;
    }

    private int _maxAllowedLength = 0;

    public int getMaxAllowedLength() {
        return _maxAllowedLength;
    }

    public ValueToLongCellException() {
    }

    public ValueToLongCellException(String message) {
        super(message);
    }

    public ValueToLongCellException(Cell cell, int maxAllowedLength) {
        _cell = cell;
        _maxAllowedLength = maxAllowedLength;
    }

}
