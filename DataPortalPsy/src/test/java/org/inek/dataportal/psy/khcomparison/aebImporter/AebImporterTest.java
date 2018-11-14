/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.psy.khcomparison.aebImporter;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.Assumptions;
import org.inek.dataportal.common.data.KhComparison.entities.AEBBaseInformation;
import org.inek.dataportal.common.data.KhComparison.entities.AEBPageB1;
import org.inek.dataportal.common.data.KhComparison.importer.AebImporter;
import org.junit.jupiter.api.Test;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

//import org.junit.jupiter.api.Assumptions;


/**
 * @author lautenti
 */
public class AebImporterTest {

    public AebImporterTest() {

    }

    @Test
    public void fullImportTest() {
        File file = new File("D:\\tmp\\Vorlage_AEB_2018_für_Datenportal_Final.xlsx");
        Assumptions.assumeThat(file.isFile()).isTrue();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (Exception ex) {

        }

        AebImporter importer = new AebImporter();
        AEBBaseInformation baseInfo = new AEBBaseInformation();
        baseInfo.setAebPageB1(new AEBPageB1());

        Assertions.assertThat(importer.startImport(baseInfo, inputStream)).isTrue();

        Assertions.assertThat(baseInfo.getAebPageE1_1()).hasSize(290);
        Assertions.assertThat(baseInfo.getAebPageE1_2()).hasSize(10);
        Assertions.assertThat(baseInfo.getAebPageE2()).hasSize(0);
        Assertions.assertThat(baseInfo.getAebPageE3_1()).hasSize(0);
        Assertions.assertThat(baseInfo.getAebPageE3_2()).hasSize(0);
        Assertions.assertThat(baseInfo.getAebPageE3_3()).hasSize(0);
    }

    @Test
    public void importB1DifferentOrderTest() {
        File file = new File("D:\\tmp\\AEB_TestB1.xlsx");
        Assumptions.assumeThat(file.isFile()).isTrue();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (Exception ex) {

        }

        AebImporter importer = new AebImporter();
        AEBBaseInformation baseInfo = new AEBBaseInformation();
        baseInfo.setAebPageB1(new AEBPageB1());

        Assertions.assertThat(importer.startImport(baseInfo, inputStream)).isTrue();

        Assertions.assertThat(baseInfo.getAebPageB1().getTotalAgreementPeriod()).isEqualTo(3500000);
        Assertions.assertThat(baseInfo.getAebPageB1().getChangedTotal()).isEqualTo(4517550);
        Assertions.assertThat(baseInfo.getAebPageB1().getChangedProceedsBudget()).isEqualTo(4492550);
        Assertions.assertThat(baseInfo.getAebPageB1().getSumValuationRadioRenumeration()).isEqualTo(4492550);
        Assertions.assertThat(baseInfo.getAebPageB1().getSumEffectivValuationRadio()).isEqualTo(52);
        Assertions.assertThat(baseInfo.getAebPageB1().getBasisRenumerationValueCompensation()).isEqualTo(1.45);
        Assertions.assertThat(baseInfo.getAebPageB1().getBasisRenumerationValueNoCompensation()).isEqualTo(1);
    }

}
