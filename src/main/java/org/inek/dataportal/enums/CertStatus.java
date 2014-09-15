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
    Unknown(-1),
    New(0),
    PasswordRequested(1),
    TestUpload1(10),
    TestFailed1(11),
    TestUpload2(12),
    TestFailed2(13),
    TestUpload3(14),
    TestFailed3(15),
    TestSucceed(20),
    CertUpload1(30),
    CertFailed1(31),
    CertUpload2(32),
    CertFailed2(33),
    CertSucceed(40),
    CertificationFailed(80),
    CertificationPassed(90);

    private CertStatus(int status) {
        _status = status;
    }

    private final int _status;
    public int getStatus() {
        return _status;
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
