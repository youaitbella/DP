package org.inek.dataportal.common.data.KhComparison.enums;

public enum PsyGroup {

    GenerallyPsy(1, "Allgemeine Psychiatrie\n"),
    Psychosomatic(2, "Psychosomatik"),
    KiJu(3, "Kinder- und Jugendpsychiatrie"),
    Other(4, "Sonstige Einrichtungen");

    private final int _id;
    private final String _text;

    PsyGroup(int id, String text) {
        _id = id;
        _text = text;
    }

    public int getId() {
        return _id;
    }

    public String getText() {
        return _text;
    }
}
