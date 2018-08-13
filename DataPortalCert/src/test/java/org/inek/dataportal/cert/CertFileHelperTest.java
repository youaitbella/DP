/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.cert;

import org.inek.dataportal.cert.Helper.CertFileHelper;
import org.assertj.core.api.Assertions;
import org.inek.dataportal.cert.entities.RemunerationSystem;
import org.inek.dataportal.common.enums.RemunSystem;
import org.junit.jupiter.api.Test;

/**
 *
 * @author lautenti
 */
public class CertFileHelperTest {

    @Test
    public void getSysDirTest() {
        RemunerationSystem system = new RemunerationSystem();

        system.setRemunerationSystem(RemunSystem.DRG);
        system.setYearSystem(2020);
        system.setYearData(2017);

        Assertions.assertThat(CertFileHelper.getSysDir(system)).isEqualTo("\\\\vFileserver01\\company$\\EDV\\Projekte\\Zertifizierung\\Pruefung\\System 2020\\G-DRG-System 2017_2020");

        system.setYearSystem(2020);
        system.setYearData(2020);

        Assertions.assertThat(CertFileHelper.getSysDir(system)).isEqualTo("\\\\vFileserver01\\company$\\EDV\\Projekte\\Zertifizierung\\Pruefung\\System 2020\\G-DRG-System 2020");

        system.setRemunerationSystem(RemunSystem.PEPP);

        system.setYearSystem(2020);
        system.setYearData(2019);

        Assertions.assertThat(CertFileHelper.getSysDir(system)).isEqualTo("\\\\vFileserver01\\company$\\EDV\\Projekte\\Zertifizierung\\Pruefung\\System 2020\\PEPP-Entgeltsystem 2019_2020");

        system.setYearSystem(2018);
        system.setYearData(2017);

        Assertions.assertThat(CertFileHelper.getSysDir(system)).isNotEqualTo("\\\\vFileserver01\\company$\\EDV\\Projekte\\Zertifizierung\\Pruefung\\System 2020\\PEPP-Entgeltsystem 2020");

        system.setYearSystem(2018);
        system.setYearData(2017);

        Assertions.assertThat(CertFileHelper.getSysDir(system)).isEqualTo("\\\\vFileserver01\\company$\\EDV\\Projekte\\Zertifizierung\\Pruefung\\System 2018\\PEPP-Entgeltsystem 2017_2018");

    }

    @Test
    public void getExtension() {
        Assertions.assertThat(CertFileHelper.getExtension("tzddjhfdjzufukgkiguug.java")).isEqualTo("java");
        Assertions.assertThat(CertFileHelper.getExtension("")).isEqualTo("");
        Assertions.assertThat(CertFileHelper.getExtension("Facebookexe")).isEqualTo("");
        Assertions.assertThat(CertFileHelper.getExtension("Facebook..txt")).isEqualTo("txt");
        Assertions.assertThat(CertFileHelper.getExtension("..Facebook..txt")).isEqualTo("txt");
        Assertions.assertThat(CertFileHelper.getExtension(".")).isEqualTo("");
        Assertions.assertThat(CertFileHelper.getExtension("..Facebook..txt.")).isEqualTo("");
        Assertions.assertThat(CertFileHelper.getExtension("..Facebook.txt")).isEqualTo("txt");
        Assertions.assertThat(CertFileHelper.getExtension(".Facebook.txt")).isEqualTo("txt");
    }
}
