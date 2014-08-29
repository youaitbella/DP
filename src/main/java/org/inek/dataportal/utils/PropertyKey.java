package org.inek.dataportal.utils;

/**
 *
 * @author muellermi
 */
public enum PropertyKey {

    ApplicationURL("https://daten.inek.org"),
    FolderRoot("//vFileserver01/company$/EDV/Datenportal/"),
    CertiFolderRoot("//vFileserver01/company$/EDV/Projekte/Zertifizierung/Pruefung/"),
    DropBoxTypeId("1"),
    FolderUpload("upload"),
    ClientVersion("20140101.0"),
    ManagerURL("https://daten.inek.org/DataPortal"),
    LocalManagerURL("http://vdataportal01:8080/DataPortal"),
    ExceptionEmail("PortalAdmin@inek-drg.de"),
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
    public String toString() {
        return this.name();
    }

}
