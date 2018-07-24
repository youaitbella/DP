/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.psy.khcomparison.aebFacade;

import org.assertj.core.api.Assertions;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.inek.dataportal.common.data.KhComparison.entities.StructureInformation;
import org.inek.dataportal.common.data.KhComparison.facade.AEBFacade;
import org.junit.jupiter.api.Test;

/**
 *
 * @author lautenti
 */
public class AebFacadeTest {

    private AEBFacade _aebFacade;

    public AebFacadeTest() {
        _aebFacade = new AEBFacade();
    }

    @Test
    public void OneEntryTest() {
        List<StructureInformation> infos = new ArrayList<>();
        StructureInformation result;

        infos.add(createNewStructureInformation(0, new GregorianCalendar(2018, Calendar.JANUARY, 1).getTime()));

        Date date = new GregorianCalendar(2018, Calendar.JANUARY, 1).getTime();
        result = _aebFacade.findStructureInformationFromDate(infos, date);

        Assertions.assertThat(result.getId()).isEqualTo(0);

        date = new GregorianCalendar(2017, Calendar.DECEMBER, 1).getTime();
        result = null;
        result = _aebFacade.findStructureInformationFromDate(infos, date);

        Assertions.assertThat(result.getId()).isEqualTo(0);

        date = new GregorianCalendar(2018, Calendar.DECEMBER, 1).getTime();;
        result = null;
        result = _aebFacade.findStructureInformationFromDate(infos, date);

        Assertions.assertThat(result.getId()).isEqualTo(0);
    }

    @Test
    public void EntrysBeforeDateTest() {
        List<StructureInformation> infos = new ArrayList<>();
        StructureInformation result;

        infos.add(createNewStructureInformation(0, new GregorianCalendar(2018, Calendar.FEBRUARY, 1).getTime()));
        infos.add(createNewStructureInformation(1, new GregorianCalendar(2018, Calendar.APRIL, 10).getTime()));

        Date date = new GregorianCalendar(2018, Calendar.FEBRUARY, 1).getTime();
        result = _aebFacade.findStructureInformationFromDate(infos, date);

        Assertions.assertThat(result.getId()).isEqualTo(0);

        date = new GregorianCalendar(2018, Calendar.APRIL, 10).getTime();
        result = null;
        result = _aebFacade.findStructureInformationFromDate(infos, date);

        Assertions.assertThat(result.getId()).isEqualTo(1);

        date = new GregorianCalendar(2018, Calendar.MAY, 1).getTime();
        result = null;
        result = _aebFacade.findStructureInformationFromDate(infos, date);

        Assertions.assertThat(result.getId()).isEqualTo(1);
    }

    @Test
    public void EntrysAfterDateTest() {
        List<StructureInformation> infos = new ArrayList<>();
        StructureInformation result;

        infos.add(createNewStructureInformation(0, new GregorianCalendar(2018, Calendar.FEBRUARY, 1).getTime()));
        infos.add(createNewStructureInformation(1, new GregorianCalendar(2018, Calendar.APRIL, 10).getTime()));
        infos.add(createNewStructureInformation(2, new GregorianCalendar(2018, Calendar.MAY, 10).getTime()));

        Date date = new GregorianCalendar(2018, Calendar.JANUARY, 1).getTime();
        result = _aebFacade.findStructureInformationFromDate(infos, date);

        Assertions.assertThat(result.getId()).isEqualTo(0);

        date = new GregorianCalendar(2018, Calendar.MARCH, 10).getTime();
        result = null;
        result = _aebFacade.findStructureInformationFromDate(infos, date);

        Assertions.assertThat(result.getId()).isEqualTo(0);

        date = new GregorianCalendar(2018, Calendar.MAY, 1).getTime();
        result = null;
        result = _aebFacade.findStructureInformationFromDate(infos, date);

        Assertions.assertThat(result.getId()).isEqualTo(1);

        date = new GregorianCalendar(2018, Calendar.MAY, 15).getTime();
        result = null;
        result = _aebFacade.findStructureInformationFromDate(infos, date);

        Assertions.assertThat(result.getId()).isEqualTo(2);
    }

    private StructureInformation createNewStructureInformation(int id, Date validFrom) {
        StructureInformation info = new StructureInformation();
        info.setId(id);
        info.setValidFrom(validFrom);
        return info;
    }
}
