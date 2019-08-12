package org.inek.psyEvaluationService.timed;

import org.eclipse.persistence.exceptions.OptimisticLockException;
import org.inek.dataportal.common.controller.ReportController;
import org.inek.dataportal.common.data.KhComparison.entities.HospitalComparisonEvaluation;
import org.inek.dataportal.common.data.KhComparison.entities.HospitalComparisonHospitals;
import org.inek.dataportal.common.data.KhComparison.entities.HospitalComparisonJob;
import org.inek.dataportal.common.data.KhComparison.enums.PsyHosptalComparisonStatus;
import org.inek.dataportal.common.data.KhComparison.facade.AEBFacade;
import org.inek.dataportal.common.data.access.ConfigFacade;

import javax.annotation.Resource;
import javax.ejb.Timer;
import javax.ejb.*;
import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Singleton
@Startup
public class ScannerTimer {

    private static final Logger LOGGER = Logger.getLogger(ScannerTimer.class.toString());
    private static String SAVE_PATH = "//vFileserver01/company$/EDV/Datenportal.dev/kh-vergleich/auswertungen";
    private static String REPORT_SERVER_URL = "http://vreportserver01:8080/InekReportServer/report/hcHospital/{0}/{1}/{2}";
    @Inject
    private AEBFacade _aebFacade;
    @Inject
    private ConfigFacade _config;
    @Inject
    private ReportController _reportController;

    @Resource
    private TimerService _timerService;
    private HospitalComparisonJob _currentJob;
    private File _jobSaveFile;

    @Timeout
    public void execute() {
        try {
            Optional<HospitalComparisonJob> oldestNewJob = _aebFacade.getOldestNewJob();
            oldestNewJob.ifPresent(this::startProcessingJob);
        } catch (Exception ex) {
            _currentJob = null;
            LOGGER.log(Level.SEVERE, ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void startProcessingJob(HospitalComparisonJob job) {
        LOGGER.log(Level.INFO, "Start processing job " + job.getId());
        if (tryToLockJob(job)) {
            if (job.getHosptalComparisonInfo().getHospitalComparisonEvaluation().isEmpty()) {
                LOGGER.log(Level.INFO, "Job " + _currentJob.getId() + " no evaluations found in DB");
                updateJobStatus(_currentJob, PsyHosptalComparisonStatus.NO_EVALUATIONS);
                saveJob();
            } else {
                createFolderForJob();
                processingEvaluations();
            }
        } else {
            LOGGER.log(Level.INFO, "Job " + job.getId() + " was already locked by another process");
        }
        LOGGER.log(Level.INFO, "End processing job " + job.getId());
    }

    private void createFolderForJob() {
        _jobSaveFile = new File(SAVE_PATH + "/" + _currentJob.getId());
        try {
            if (_jobSaveFile.exists()) {
                Files.walk(_jobSaveFile.toPath())
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
            LOGGER.log(Level.INFO, "Job " + _currentJob.getId() + " try to create folder " + _jobSaveFile.getPath());
            Files.createDirectory(_jobSaveFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Error creating directory for job: " + _currentJob.getId());
        }
    }

    private void processingEvaluations() {
        int hceId = _currentJob.getHosptalComparisonInfo().getId();
        for (HospitalComparisonEvaluation evaluation : _currentJob.getHosptalComparisonInfo().getHospitalComparisonEvaluation()) {
            LOGGER.log(Level.INFO, "Job " + _currentJob.getId() + " start evaluation " + evaluation.getId());
            int aebIdHospital = evaluation.getHospitalComparisonHospitalsHospital().getAebBaseInformationId();
            String aebIdsGroupe = concatAebIds(evaluation.getHospitalComparisonHospitals());
            String reportUrl = buildUrlWithParameter(hceId, aebIdHospital, aebIdsGroupe);
            String fileName = buildFileName(evaluation);
            LOGGER.log(Level.INFO, "Job " + _currentJob.getId() + " request document from " + reportUrl);
            byte[] singleDocument = _reportController.getSingleDocument(reportUrl);

            try (FileOutputStream out = new FileOutputStream(fileName)) {
                LOGGER.log(Level.INFO, "Job " + _currentJob.getId() + " write data to file " + fileName);
                out.write(singleDocument);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Job: " + _currentJob.getId() + " error writing file: "
                        + fileName);
            }
            LOGGER.log(Level.INFO, "Job " + _currentJob.getId() + " end evaluation " + evaluation.getId());
        }
        String zipFileName = zipExcelFiles();
        createAccountDocument(zipFileName);
    }

    private void createAccountDocument(String zipExcelFilesPath) {
        String accountDocumentZipFile = buildAccountDokZipFileName();
        List<String> filesForZip = new ArrayList<>();
        filesForZip.add(zipExcelFilesPath);
        filesForZip.add(createDocumentInfoFile());
        zipFiles(accountDocumentZipFile, filesForZip);
    }

    private String createDocumentInfoFile() {
        String documentInfoFileName = createDocumentInfoFileName();
        StringBuilder docInfo = new StringBuilder();
        docInfo.append("Version=1.0\n");
        docInfo.append("Account.Id=" + _currentJob.getHosptalComparisonInfo().getAccountId() + " \n");
        docInfo.append("Document.Domain=Sonstige");
        File docFile = new File(documentInfoFileName);
        try {
            //TODO Log
            Files.write(docFile.toPath(), docInfo.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return documentInfoFileName;
    }

    private void zipFiles(String zipFilePath, List<String> filesForZip) {
        try {
            LOGGER.log(Level.INFO, "Job " + _currentJob.getId() + " try to zip files: " + filesForZip);
            FileOutputStream fos = new FileOutputStream(zipFilePath);
            ZipOutputStream zipOut = new ZipOutputStream(fos);
            for (String srcFile : filesForZip) {
                File fileToZip = new File(srcFile);
                FileInputStream fis = new FileInputStream(fileToZip);
                ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                zipOut.putNextEntry(zipEntry);

                byte[] bytes = new byte[1024];
                int length;
                while ((length = fis.read(bytes)) >= 0) {
                    zipOut.write(bytes, 0, length);
                }
                fis.close();
            }
            zipOut.close();
            fos.close();
            LOGGER.log(Level.INFO, "Job " + _currentJob.getId() + " end zip files");
        } catch (Exception ex) {
            throw new IllegalArgumentException("Job " + _currentJob.getId() + " error during zip files [" + filesForZip + "]: " + ex.getMessage());
        }
    }

    private String zipExcelFiles() {
        String zipFileName = buildZipFileName();
        zipFiles(zipFileName, getAllExcelFilePaths());
        return zipFileName;
    }

    private List<String> getAllExcelFilePaths() {
        try {
            return Files.walk(_jobSaveFile.toPath())
                    .map(c -> c.toString())
                    .filter(c -> c.endsWith(".xlsx"))
                    .collect(Collectors.toList());
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new IllegalArgumentException("Job " + _currentJob.getId() + " error during collect excel filesnames for zip: " + ex.getMessage());
        }
    }

    private String buildZipFileName() {
        String fileNamePattern = "%s_%s_Auswertungen_KH_Vergleich.zip";
        return _jobSaveFile + "\\" + String.format(fileNamePattern, _currentJob.getId(), _currentJob.getHosptalComparisonInfo().getHospitalIk());
    }

    private String buildAccountDokZipFileName() {
        String fileNamePattern = "KH-Vergleich_%s_%s.zip";
        return _jobSaveFile + "\\" + String.format(fileNamePattern, _currentJob.getHosptalComparisonInfo().getHospitalIk(),
                new SimpleDateFormat("ddMMyyyyHHmmss").format(Calendar.getInstance().getTime()));
    }

    private String buildFileName(HospitalComparisonEvaluation evaluation) {
        String fileNamePattern = "%s_%s_PSY-KH_Vergleich_Auswertung.xlsx";
        return _jobSaveFile + "\\" + String.format(fileNamePattern, evaluation.getHospitalComparisonInfo().getHospitalIk(),
                evaluation.getEvalutationHcId());
    }

    private String createDocumentInfoFileName() {
        String fileNamePattern = "DocInfo_%s_%s.DataportalDocumentInfo";
        return _jobSaveFile + "\\" + String.format(fileNamePattern, _currentJob.getId(),
                new SimpleDateFormat("ddMMyyyyHHmmss").format(Calendar.getInstance().getTime()));
    }

    private String buildUrlWithParameter(int hcId, int aebIdHospital, String evaluationGroupeIds) {
        String result = REPORT_SERVER_URL;
        result = result.replace("{0}", String.valueOf(hcId));
        result = result.replace("{1}", String.valueOf(aebIdHospital));
        result = result.replace("{2}", evaluationGroupeIds);
        return result;
    }

    private String concatAebIds(List<HospitalComparisonHospitals> hospitals) {
        StringBuilder result = new StringBuilder();
        for (HospitalComparisonHospitals hospital : hospitals) {
            result.append(hospital.getAebBaseInformationId()).append(",");
        }
        if (!"".contentEquals(result)) {
            result.deleteCharAt(result.length() - 1);
        }
        return result.toString();
    }

    private boolean tryToLockJob(HospitalComparisonJob job) {
        updateJobStatus(job, PsyHosptalComparisonStatus.WORKING);

        try {
            LOGGER.log(Level.INFO, "Trying to lock job " + job.getId());
            _currentJob = _aebFacade.save(job);
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

    private void saveJob() {
        _currentJob = _aebFacade.save(_currentJob);
    }

    public void startTimer() {
        ScheduleExpression expression = new ScheduleExpression();
        expression.second("*/30").minute("*").hour("*");
        _timerService.createCalendarTimer(expression);
        LOGGER.log(Level.INFO, "Timer started");
    }

    public void stopTimer() {
        for (Timer allTimer : _timerService.getAllTimers()) {
            allTimer.cancel();
        }
        LOGGER.log(Level.INFO, "Timer stopped");
    }
}
