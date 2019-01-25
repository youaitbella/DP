package org.inek.dataportal.common.data.KhComparison.entities;

import javax.persistence.Transient;

public class AEBPage {

    @Transient
    private String _importetFrom = "";

    public String getImportetFrom() { return _importetFrom; }

    public void setImportetFrom(String importetFrom) { this._importetFrom = importetFrom; }
}
