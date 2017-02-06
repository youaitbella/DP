/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc.iface;

/**
 *
 * @author muellermi
 */
public interface ListCostCenter {

    int getAmount();

    int getBaseInformationId();

    int getCostCenterId();

    int getCostCenterNumber();

    String getCostCenterText();

    double getFullVigorCnt();

    int getId();

    String getServiceKey();

    String getServiceKeyDescription();

    double getServiceSum();

    void setAmount(int amount);

    void setBaseInformationId(int baseInformationId);

    void setCostCenterId(int costCenterId);

    void setCostCenterNumber(int costCenterNumber);

    void setCostCenterText(String costCenterText);

    void setFullVigorCnt(double fullVigorCnt);

    void setId(int id);

    void setServiceKey(String serviceKey);

    void setServiceKeyDescription(String serviceKeyDescription);

    void setServiceSum(double serviceSum);

}
