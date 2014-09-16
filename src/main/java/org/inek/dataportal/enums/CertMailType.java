package org.inek.dataportal.enums;

import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;

/**
 *
 * @author vohldo
 */
public enum CertMailType {
    Opening(1, "Eröffnungs-Email"),
    Information(2, "Informations-Email"),
    ErrorTest(3, "Fehler-Testphase"),
    PassedTest(4,"Testphase-Bestanden"),
    ErrorCert(5,"Fehler-Zertifizierungphase"),
    Certified(6,"Zertifiziert-Email");
    

    private CertMailType(int id, String label) {
        _id = id;
        _label = label;
    }

    private final int _id;
    public int getId() {
        return _id;
    }
    
    private final String _label;
    public String getLabel() {
        return _label;
    }
    
    public static String getTypeFromId(int id) {
        for(CertMailType certType : CertMailType.values()) {
            if(certType.getId() == id)
                return certType.getLabel();
        }
        return "";
    }
    
    public static int getTypeFromLabel(String label) {
        for(CertMailType certType : CertMailType.values()) {
            if(certType.getLabel().equals(label))
                return certType.getId();
        }
        return 0;
    }
    
    public static List<SelectItem> getSelectItems() {
        List<SelectItem> list = new ArrayList<>();
        for(CertMailType certType : CertMailType.values()) {
            list.add(new SelectItem(certType.getLabel()));
        }
        return list;
    }
}
