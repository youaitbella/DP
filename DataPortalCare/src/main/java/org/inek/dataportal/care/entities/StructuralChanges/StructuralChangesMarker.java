package org.inek.dataportal.care.entities.StructuralChanges;

import java.util.HashMap;
import java.util.Map;

public enum StructuralChangesMarker {

    NEUTRAL(0),
    MARKER_1(1),
    MARKER_2(2);

    private int _id;

    public int getId() {
        return _id;
    }

    private static Map<Integer, StructuralChangesMarker> markers = new HashMap<>();

    static {
        for (StructuralChangesMarker marker : StructuralChangesMarker.values()) {
            markers.put(marker.getId(), marker);
        }
    }

    StructuralChangesMarker(int id) {
        _id = id;
    }

    public static StructuralChangesMarker fromId(int id){
        return markers.computeIfAbsent(id, (i) -> StructuralChangesMarker.NEUTRAL);
    }

    public StructuralChangesMarker nextColor() {
        Integer nextId = markers.keySet().stream().sorted().filter(id -> id > _id).findFirst().orElse(NEUTRAL.getId());
        return fromId(nextId);
    }
}
