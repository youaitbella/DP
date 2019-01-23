package org.inek.dataportal.insurance.care.backingbean;

public class SignatureEntry {

    private final String _signature;
    private final int _ik;
    private final String _hospitalName;
    private final int _year;
    private final int _quarter;

    public SignatureEntry(){
        this("", 0, "", 0, 0);
    }
    public SignatureEntry(String signature, int ik, String hospitalName, int year, int quarter) {
        _signature = signature;
        _ik = ik;
        _hospitalName = hospitalName;
        _year = year;
        _quarter = quarter;
    }

    public String getSignature() {
        return _signature;
    }

    public int getIk() {
        return _ik;
    }

    public String getHospitalName() {
        return _hospitalName;
    }

    public int getYear() {
        return _year;
    }

    public int getQuarter() {
        return _quarter;
    }
}
