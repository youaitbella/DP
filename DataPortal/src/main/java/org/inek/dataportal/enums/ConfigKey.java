package org.inek.dataportal.enums;

/**
 *
 * @author muellermi
 */
public enum ConfigKey {

    IsNubCreateEnabled(false),
    IsNubSendEnabled(true),
    RemindNubSeal(true),
    IsDrgProposalCreateEnabled(true),
    IsDrgProposalSendEnabled(true),
    IsPeppProposalCreateEnabled(true),
    IsPeppProposalSendEnabled(true),
    IsModelIntentionSendEnabled(true),
    IsStatemenOfParticipanceCreateEnabled(false),
    IsCalationBasicsDrgCreateEnabled(false),
    IsCalationBasicsPsyCreateEnabled(false),
    IsStatemenOfParticipanceSendEnabled(false),
    IsCalationBasicsDrgSendEnabled(false),
    IsCalationBasicsPsySendEnabled(false),
    DataServiceClientVersion("20160101.3"),
    ApplicationURL("https://daten.inek.org"),
    FolderRoot("//vFileserver01/company$/EDV/Datenportal/"),
    CertiFolderRoot("//vFileserver01/company$/EDV/Projekte/Zertifizierung/Pruefung/"),
    DocumentScanBase("//vFileserver01/company$/EDV/Datenportal/documents"),
    DocumentScanDir(false),
    Feature(true),
    DropBoxTypeId(1),
    FolderUpload("upload"),
    ManagerURL("https://daten.inek.org/DataPortal"),
    LocalManagerURL("http://vdataportal01:8080/DataPortal"),
    ExceptionEmail("PortalAdmin@inek-drg.de"),
    ManagerEmail("dsportalanmeldungen@inek-drg.de"), 
    ReportValidity(60);

    //
    Object _defaultValue;

    private ConfigKey(Object defaultValue) {
        _defaultValue = defaultValue;
    }

    public String getDefault() {
        if (_defaultValue instanceof String ){}
        return "" + _defaultValue;
    }
    
    public boolean isBool(){
        return _defaultValue instanceof Boolean;
    }
    
    public boolean getBoolDefault() {
        return (boolean) _defaultValue;
    }
    
    public boolean isInt(){
        return _defaultValue instanceof Integer;
    }
    
    public int getIntDefault() {
        return (int) _defaultValue;
    }
    
    //private getType

    @Override
    public String toString() {
        return this.name();
    }

}

