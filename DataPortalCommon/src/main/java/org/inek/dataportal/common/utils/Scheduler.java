package org.inek.dataportal.common.utils;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import org.inek.dataportal.common.overall.ApplicationTools;

/**
 *
 * @author muellermi
 */
@Singleton
public class Scheduler {
    @Inject private ApplicationTools _appTools;
    
    @Schedule(hour = "0")
    private void cleanHospitalInfoCache() {
        _appTools.cleanHospitalInfoCache();
    }
    
}
