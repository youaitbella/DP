/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.enums;

/**
 *
 * @author muellermi
 */
public enum CertStatus {
    Unknown(-1, "Unbekannte Phase"),
    New(0, "Neu"),
    PasswordRequested(1, "Passwort abgefragt"),
    TestUpload1(10, "Testphase Versuch 1 - Daten hochgeladen"),
    TestFailed1(11, "Testphase Versuch 1 - Fehlerhaft"),
    TestUpload2(12, "Testphase Versuch 2 - Daten hochgeladen"),
    TestFailed2(13, "Testphase Versuch 2 - Fehlerhaft"),
    TestUpload3(14, "Testphase Versuch 3 - Daten hochgeladen"),
    TestFailed3(15, "Testphase Versuch 3 - Fehlerhaft"),
    TestSucceed(20, "Testphase erfolgreich bestanden"),
    CertUpload1(30, "Zertiphase Versuch 1 - Daten hochgeladen"),
    CertFailed1(31, "Zertiphase Versuch 1 - Fehlerhaft"),
    CertUpload2(32, "Zertiphase Versuch 2 - Daten hochgeladen"),
    CertFailed2(33, "Zertiphase Versuch 2 - Fehlerhaft"),
    CertSucceed(40, "Zertiphase erfolgreich bstanden"),
    CertificationFailed(80, "Grouper NICHT zertifiziert"),
    CertificationPassed(90, "Grouper zertifiziert");

    private CertStatus(int status, String label) {
        _status = status;
        _label = label;
    }

    private final int _status;
    public int getStatus() {
        return _status;
    }
    
    public final String _label;
    public String getLabel() {
        return _label;
    }
    

    public static CertStatus fromStatus(int status) {
        for (CertStatus certStatus : CertStatus.values()) {
            if (certStatus.getStatus() == status) {
                return certStatus;
            }
        }
        return CertStatus.Unknown;
    }
}
