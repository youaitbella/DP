package org.inek.dataportal.base.timed;

import java.util.Objects;

public class IdText {

    public IdText(int id, String text) {
        this.id = id;
        this.text = text;
    }

    private int id;

    public int getId() {
        return id;
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
        IdText idText = (IdText) o;
        return id == idText.id &&
                text.equals(idText.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, text);
    }
}
