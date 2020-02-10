package org.inek.dataportal.care.proof;

import java.util.Objects;

public class PpugInfo {

    private final double ppug;
    private final double part;

    public PpugInfo(double ppug, double part) {
        this.ppug = ppug;
        this.part = part;
    }

    public double getPpug() {
        return ppug;
    }

    public double getPart() {
        return part;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PpugInfo ppugInfo = (PpugInfo) o;
        return Double.compare(ppugInfo.ppug, ppug) == 0 &&
                Double.compare(ppugInfo.part, part) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ppug, part);
    }
}
