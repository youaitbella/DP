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

import java.io.*;

/**
 * @author lautenti
 */
public class CI_AebImporterTest {

    public CI_AebImporterTest() {

    }

    @Test
    public void fullImportTest() throws IOException {
        File file = new File("src\\test\\resources\\Vorlage_AEB_2018_für_Datenportal_Final.xlsx");
        Assumptions.assumeThat(file.isFile()).isTrue();

        try (InputStream inputStream = new FileInputStream(file)) {
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
    }

    @Test
    public void fullImportKK1Test() throws IOException {
        File file = new File("src\\test\\resources\\AEB_KK_1.xlsx");
        Assumptions.assumeThat(file.isFile()).isTrue();
        try (InputStream inputStream = new FileInputStream(file)) {
            AebImporter importer = new AebImporter();
            AEBBaseInformation baseInfo = new AEBBaseInformation();
            baseInfo.setAebPageB1(new AEBPageB1());

            Assertions.assertThat(importer.startImport(baseInfo, inputStream)).isTrue();

            Assertions.assertThat(baseInfo.getAebPageE1_1()).hasSize(279);
            Assertions.assertThat(baseInfo.getAebPageE1_2()).hasSize(5);
            Assertions.assertThat(baseInfo.getAebPageE2()).hasSize(0);
            Assertions.assertThat(baseInfo.getAebPageE3_1()).hasSize(0);
            Assertions.assertThat(baseInfo.getAebPageE3_2()).hasSize(1);
            Assertions.assertThat(baseInfo.getAebPageE3_3()).hasSize(1);

            Assertions.assertThat(baseInfo.getAebPageE1_1().stream()
                    .filter(c -> "PA01A".equals(c.getPepp()) && c.getCompensationClass() == 7)
                    .findFirst()
                    .orElseGet(() -> {throw new IllegalStateException("No such AEB");})
                    .getCaseCount()
            ).isEqualTo(0);
            Assertions.assertThat(baseInfo.getAebPageE1_1().stream()
                    .filter(c -> "PA01A".equals(c.getPepp()) && c.getCompensationClass() == 7)
                    .findFirst()
                    .orElseGet(() -> {throw new IllegalStateException("No such AEB");})
                    .getCalculationDays()
            ).isEqualTo(2889);

            Assertions.assertThat(baseInfo.getAebPageB1().getTotalAgreementPeriod()).isEqualTo(0);
            Assertions.assertThat(baseInfo.getAebPageB1().getChangedTotal()).isEqualTo(55453196.0);
            Assertions.assertThat(baseInfo.getAebPageB1().getChangedProceedsBudget()).isEqualTo(55189839.0);
            Assertions.assertThat(baseInfo.getAebPageB1().getSumValuationRadioRenumeration()).isEqualTo(55189839.0);
            Assertions.assertThat(baseInfo.getAebPageB1().getSumEffectivValuationRadio()).isEqualTo(202158.9726);
            Assertions.assertThat(baseInfo.getAebPageB1().getBasisRenumerationValueCompensation()).isEqualTo(273);
            Assertions.assertThat(baseInfo.getAebPageB1().getBasisRenumerationValueNoCompensation()).isEqualTo(272.68);
        }
    }

    @Test
    public void fullImportKK2Test() throws IOException {
        File file = new File("src\\test\\resources\\AEB_KK_2.xlsx");
        Assumptions.assumeThat(file.isFile()).isTrue();
        try (InputStream inputStream = new FileInputStream(file)) {
            AebImporter importer = new AebImporter();
            AEBBaseInformation baseInfo = new AEBBaseInformation();
            baseInfo.setAebPageB1(new AEBPageB1());

            Assertions.assertThat(importer.startImport(baseInfo, inputStream)).isTrue();

            Assertions.assertThat(baseInfo.getAebPageE1_1()).hasSize(308);
            Assertions.assertThat(baseInfo.getAebPageE1_2()).hasSize(8);
            Assertions.assertThat(baseInfo.getAebPageE2()).hasSize(0);
            Assertions.assertThat(baseInfo.getAebPageE3_1()).hasSize(0);
            Assertions.assertThat(baseInfo.getAebPageE3_2()).hasSize(2);
            Assertions.assertThat(baseInfo.getAebPageE3_3()).hasSize(12);

            Assertions.assertThat(baseInfo.getAebPageE1_1().stream()
                    .filter(c -> "PA01A".equals(c.getPepp()) && c.getCompensationClass() == 7)
                    .findFirst()
                    .orElseGet(() -> {throw new IllegalStateException("No such AEB");})
                    .getCaseCount()
            ).isEqualTo(11);
            Assertions.assertThat(baseInfo.getAebPageE1_1().stream()
                    .filter(c -> "PA01A".equals(c.getPepp()) && c.getCompensationClass() == 7)
                    .findFirst()
                    .orElseGet(() -> {throw new IllegalStateException("No such AEB");})
                    .getCalculationDays()
            ).isEqualTo(688);

            Assertions.assertThat(baseInfo.getAebPageB1().getTotalAgreementPeriod()).isEqualTo(25992153.0);
            Assertions.assertThat(baseInfo.getAebPageB1().getChangedTotal()).isEqualTo(27335175.0);
            Assertions.assertThat(baseInfo.getAebPageB1().getChangedProceedsBudget()).isEqualTo(27087492.65);
            Assertions.assertThat(baseInfo.getAebPageB1().getSumValuationRadioRenumeration()).isEqualTo(27087492.65);
            Assertions.assertThat(baseInfo.getAebPageB1().getSumEffectivValuationRadio()).isEqualTo(108307.1087);
            Assertions.assertThat(baseInfo.getAebPageB1().getBasisRenumerationValueCompensation()).isEqualTo(250.10);
            Assertions.assertThat(baseInfo.getAebPageB1().getBasisRenumerationValueNoCompensation()).isEqualTo(244.15);
        }
    }

    @Test
    public void fullImportOherTablePositionTest() throws IOException {
        File file = new File("src\\test\\resources\\Vorlage_AEB_Andere_Tabellenposition.xlsx");
        Assumptions.assumeThat(file.isFile()).isTrue();
        try (InputStream inputStream = new FileInputStream(file)) {
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
    }

    @Test
    public void importWithAllowedEmptyColumnTest() throws IOException {
        File file = new File("src\\test\\resources\\AEB_Test_Leere_Spalte.xlsx");
        Assumptions.assumeThat(file.isFile()).isTrue();
        try (InputStream inputStream = new FileInputStream(file)) {
            AebImporter importer = new AebImporter();
            AEBBaseInformation baseInfo = new AEBBaseInformation();
            baseInfo.setAebPageB1(new AEBPageB1());

            Assertions.assertThat(importer.startImport(baseInfo, inputStream)).isTrue();

            Assertions.assertThat(baseInfo.getAebPageE1_1()).hasSize(2);
            Assertions.assertThat(baseInfo.getAebPageE1_1().get(0).getCaseCount()).isEqualTo(0);
            Assertions.assertThat(baseInfo.getAebPageE1_1().get(1).getCaseCount()).isEqualTo(0);
            Assertions.assertThat(baseInfo.getAebPageE3_3()).hasSize(1);
            Assertions.assertThat(baseInfo.getAebPageE3_3().get(0).getCaseCount()).isEqualTo(0);
        }
    }

    @Test
    public void wrongFormatTest() throws IOException {
        File file = new File("src\\test\\resources\\Vorlage_AEB_2018_für_Datenportal_FalschesFormat.xlsx");
        Assumptions.assumeThat(file.isFile()).isTrue();
        try (InputStream inputStream = new FileInputStream(file)) {
            AebImporter importer = new AebImporter();
            AEBBaseInformation baseInfo = new AEBBaseInformation();
            baseInfo.setAebPageB1(new AEBPageB1());

            Assertions.assertThat(importer.startImport(baseInfo, inputStream)).isFalse();
        }
    }

    @Test
    public void importB1DifferentOrderTest() throws IOException {
        File file = new File("src\\test\\resources\\AEB_TestB1.xlsx");
        Assumptions.assumeThat(file.isFile()).isTrue();
        try (InputStream inputStream = new FileInputStream(file)) {
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

}
