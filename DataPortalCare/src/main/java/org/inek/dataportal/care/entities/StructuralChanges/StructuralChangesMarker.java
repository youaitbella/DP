package org.inek.dataportal.care.enums;

public enum StructuralChangesMarker {

    NEUTRAL(0, "Neutral"),
    ORANGE(1, "Orange"),
    GREEN(2, "Green");;

    private int _id;
    private String _colour;

    public int getId() {
        return _id;
    }

    public String getColour() {
        return _colour;
    }

    StructuralChangesMarker(int id, String colour) {
        _id = id;
        _colour = colour;
    }

    public static StructuralChangesMarker fromColor(String colour){
        for (StructuralChangesMarker marker : StructuralChangesMarker.values()) {
            if (marker.getColour().toUpperCase().equals(colour.toUpperCase())) {
                return marker;
            }
        }
        return StructuralChangesMarker.NEUTRAL;
    }

    public static StructuralChangesMarker fromId(int id){
        for (StructuralChangesMarker marker : StructuralChangesMarker.values()) {
            if (marker.getId() == id) {
                return marker;
            }
        }
        return StructuralChangesMarker.NEUTRAL;
    }

    public StructuralChangesMarker nextColor() {
        return fromId(_id+1);
    }
}
