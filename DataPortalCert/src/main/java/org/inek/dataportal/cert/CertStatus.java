/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.cert;

/**
 *
 * @author muellermi
 */
public enum CertStatus {
    Unknown(-1, "Unbekannte Phase"),
    New(0, "Neu"),
    PasswordRequested(1, "Passwort abgefragt"),
    TestUpload1(10, "Testphase v1, Daten geliefert"),
    TestFailed1(11, "Testphase v1, Fehlerhaft"),
    TestUpload2(12, "Testphase v2, Daten geliefert"),
    TestFailed2(13, "Testphase v2, Fehlerhaft"),
    TestUpload3(14, "Testphase v3, Daten geliefert"),
    TestSucceed(20, "Testphase erfolgreich"),
    CertUpload1(30, "Zertiphase v1, Daten geliefert"),
    CertFailed1(31, "Zertiphase v1, Fehlerhaft"),
    CertUpload2(32, "Zertiphase v2, Daten geliefert"),
    CertSucceed(40, "Zertiphase erfolgreich"),
    CertificationFailed(80, "Grouper NICHT zertifiziert"),
    CertificationPassed(90, "Grouper zertifiziert");

    CertStatus(int status, String label) {
        _status = status;
        _label = label;
    }

    private final int _status;
    public int getStatus() {
        return _status;
    }

    private final String _label;
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
