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
    UploadTest1(10),
    FailTest1(11),
    SuccessTest1(12),
    UploadTest2(20),
    FailTest2(21),
    SuccessTest2(22),
    UploadTest3(30),
    FailTest3(31),
    SuccessTest3(32),
    UploadCert1(40),
    FailCert1(41),
    SuccessCert1(42),
    UploadCert2(540),
    FailCert2(51),
    SuccessCert2(52),
    Failed(80),
    Passed(90);

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
