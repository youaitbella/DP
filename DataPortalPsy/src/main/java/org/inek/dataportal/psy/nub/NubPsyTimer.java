package org.inek.dataportal.psy.nub;

import org.inek.dataportal.psy.nub.facade.PsyNubFacade;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

@Singleton
public class NubPsyTimer {

    @Inject
    private PsyNubFacade _psyNubFacade;

    //@Schedule(hour = "0", info = "once a day")
    @Schedule(second = "30", info = "once a day")
    private void check4NubOrphantCorrections() {
        _psyNubFacade.check4NubOrphantCorrections();
    }
}
