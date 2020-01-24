package org.inek.dataportal.base.timed;

import java.util.Objects;

public class IdIkText {

    public IdIkText(int id, int ik, String text) {
        this.id = id;
        this.ik = ik;
        this.text = text;
    }

    private int id;

    public int getId() {
        return id;
    }

    private int ik;

    public int getIk() {
        return ik;
    }

    private String text;

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IdIkText idIkText = (IdIkText) o;
        return id == idIkText.id &&
                ik == idIkText.ik &&
                text.equals(idIkText.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ik, text);
    }
}
