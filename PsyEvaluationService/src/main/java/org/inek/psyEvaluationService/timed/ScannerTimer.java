package org.inek.psyEvaluationService.timed;

import org.eclipse.persistence.exceptions.OptimisticLockException;
import org.inek.dataportal.common.data.KhComparison.entities.HospitalComparisonJob;
import org.inek.dataportal.common.data.KhComparison.enums.PsyHosptalComparisonStatus;
import org.inek.dataportal.common.data.KhComparison.facade.AEBFacade;
import org.inek.dataportal.common.data.access.ConfigFacade;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
@Startup
public class ScannerTimer {

    private static final Logger LOGGER = Logger.getLogger(ScannerTimer.class.toString());
    @Inject
    private AEBFacade _aebFacade;
    @Inject
    private ConfigFacade _config;
    @Resource
    private TimerService _timerService;

    @Timeout
    public void execute() {
        try {
            Optional<HospitalComparisonJob> oldestNewJob = _aebFacade.getOldestNewJob();
            oldestNewJob.ifPresent(this::startProcessingJob);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void startProcessingJob(HospitalComparisonJob job) {
        LOGGER.log(Level.INFO, "Start processing job " + job.getId());
        if (tryToLockJob(job)) {
            if (job.getHosptalComparisonInfo().getHospitalComparisonEvaluation().isEmpty()) {
                LOGGER.log(Level.INFO, "Job " + job.getId() + " no evaluations found in DB");
                updateJobStatus(job, PsyHosptalComparisonStatus.NO_EVALUATIONS);
            }
        } else {
            LOGGER.log(Level.INFO, "Job " + job.getId() + " was already locked by another process");
        }
    }

    private boolean tryToLockJob(HospitalComparisonJob job) {
        updateJobStatus(job, PsyHosptalComparisonStatus.WORKING);

        try {
            LOGGER.log(Level.INFO, "Trying to lock job " + job.getId());
            //job = _aebFacade.save(job);
            _aebFacade.save(job);
            LOGGER.log(Level.INFO, "locked job " + job.getId());
            return true;
        } catch (OptimisticLockException ex) {
            return false;
        }
    }

    private void updateJobStatus(HospitalComparisonJob job, PsyHosptalComparisonStatus status) {
        switch (status) {
            case WORKING:
                job.setStartWorking(new Date());
                break;
            case DONE:
            case NO_EVALUATIONS:
                job.setEndWorking(new Date());
                break;
            default:
        }
        job.setStatus(status);
    }

    private void saveJob(HospitalComparisonJob job) {
        //job = _aebFacade.save(job);
    }


    public void startTimer() {
        ScheduleExpression expression = new ScheduleExpression();
        expression.second("*/30").minute("*").hour("*");
        _timerService.createCalendarTimer(expression);
    }

    public void stopTimer() {
        for (Timer allTimer : _timerService.getAllTimers()) {
            allTimer.cancel();
        }

    }
}
