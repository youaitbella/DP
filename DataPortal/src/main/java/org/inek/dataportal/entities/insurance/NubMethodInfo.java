package org.inek.dataportal.entities.insurance;

import java.io.Serializable;
import java.util.Arrays;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author muellermi
 */
@Entity
public class NubMethodInfo implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="Property RequestId">
    @Column(name = "prDatenportalId")
    @Id
    private int _requestId;

    public int getRequestId() {
        return _requestId;
    }

    public void setRequestId(int requestId) {
        this._requestId = requestId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property RequestName">
    @Column(name = "prNubName")
    private String _requestName;

    public String getRequestName() {
        return _requestName;
    }

    public void setRequestName(String requestName) {
        this._requestName = requestName;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property MethodIk">
    @Column(name = "prFkCaId")
    private int _methodId;

    public int getMethodId() {
        return _methodId;
    }

    public void setMethodId(int methodId) {
        this._methodId = methodId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property MethodName">
    @Column(name = "caName")
    private String _methodName;

    public String getMethodName() {
        return _methodName;
    }

    public void setMethodName(String methodName) {
        this._methodName = methodName;
    }
    // </editor-fold>

    @Override
    public String toString() {
        String regex = " |\\.|,|\\r|\\n|\\t|-|®|©";
        String info = "N" + _requestId + "   [" + _methodName + "]";
        if (!_methodName.replaceAll(regex, "").equals(_requestName.replaceAll(regex, ""))) {
            info += " ### Name Anfrage = " + _requestName;
        }
        return info;
    }

}
