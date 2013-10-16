package org.inek.dataportal.utils;

/**
 *
 * @author muellermi
 */
public enum PropertyKey {

        ApplicationURL("https://daten.inek.org"),
        FolderRoot("//fileserver1/company$/EDV/Datenportal/"),
        DropBoxTypeId("1"),
        FolderUpload("upload"),
        ClientVersion("20120101.5"),
        ManagerURL("https://daten.inek.org/DataPortal"),
        ExceptionEmail("dominik.vohl@inek-drg.de"),
        ManagerEmail("dsportalanmeldungen@inek-drg.de");
    
    // 
    String _defaultValue;
    
    PropertyKey(String defaultValue) {
        _defaultValue = defaultValue;
    }

    public String getDefault() {
        return _defaultValue;
    }
    
    @Override
    public String toString(){
        return this.name();
    }
}