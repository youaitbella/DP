package org.inek.psyEvaluationService.timed;

import org.eclipse.persistence.exceptions.OptimisticLockException;
import org.inek.dataportal.common.controller.ReportController;
import org.inek.dataportal.common.data.KhComparison.entities.HospitalComparisonEvaluation;
import org.inek.dataportal.common.data.KhComparison.entities.HospitalComparisonHospitals;
import org.inek.dataportal.common.data.KhComparison.entities.HospitalComparisonJob;
import org.inek.dataportal.common.data.KhComparison.enums.PsyHosptalComparisonStatus;
import org.inek.dataportal.common.data.KhComparison.facade.AEBFacade;
import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.mail.Mailer;

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
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
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

    @Inject
    private AEBFacade _aebFacade;
    @Inject
    private ConfigFacade _config;
    @Inject
    private ReportController _reportController;
    @Inject
    private Mailer _mailer;

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
            _mailer.sendMail("PortalAdmin@inek-drg.de", "PsyEvaluationService error: " + ex.getMessage(),
                    ex.getStackTrace().toString());
            updateJobStatus(_currentJob, PsyHosptalComparisonStatus.ERROR);
            saveJob();
            LOGGER.log(Level.SEVERE, ex.getMessage());
            ex.printStackTrace();
        } finally {
            _currentJob = null;
            _jobSaveFile = null;
        }
    }

    private void startProcessingJob(HospitalComparisonJob job) {
        logJobInfo(job, "start processing");
        if (tryToLockJob(job)) {
            if (job.getHosptalComparisonInfo().getHospitalComparisonEvaluation().isEmpty()) {
                logJobInfo("no evaluations found in DB");
                updateJobStatus(_currentJob, PsyHosptalComparisonStatus.NO_EVALUATIONS);
                saveJob();
            } else {
                createFolderForJob();
                processingEvaluations();
                String evaluationsZipPath = zipEvaluations();
                createAccountDocument(evaluationsZipPath);
                updateJobStatus(_currentJob, PsyHosptalComparisonStatus.DONE);
                saveJob();
            }
        } else {
            logJobInfo(job, "was already locked by another process");
        }
        logJobInfo(job, "end processing");
    }

    private String zipEvaluations() {
        String zipFileName = buildZipFileName();
        zipFiles(zipFileName, getAllExcelFilePaths());
        return zipFileName;
    }

    private void createFolderForJob() {
        _jobSaveFile = new File(_currentJob.getJobFolder(_config.readConfig(ConfigKey.KhComparisonJobSavePath)));
        try {
            if (_jobSaveFile.exists()) {
                Files.walk(_jobSaveFile.toPath())
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
            logJobInfo("try to create job folder: " + _jobSaveFile.getPath());
            Files.createDirectory(_jobSaveFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Error creating directory for job: " + _currentJob.getId());
        }
    }

    private void processingEvaluations() {
        for (HospitalComparisonEvaluation evaluation : _currentJob.getHosptalComparisonInfo().getHospitalComparisonEvaluation()) {
            logJobInfo("start evaluation [" + evaluation.getId() + "]");
            int aebIdHospital = evaluation.getHospitalComparisonHospitalsHospital().getAebBaseInformationId();
            String aebIdsGroupe = concatAebIds(evaluation.getHospitalComparisonHospitals());
            String reportUrl = buildUrlWithParameter(evaluation.getId(), aebIdHospital, aebIdsGroupe);
            String fileName = buildExcelFileName(evaluation);

            logJobInfo("request document from " + reportUrl);
            byte[] singleDocument = _reportController.getSingleDocument(reportUrl);

            try (FileOutputStream out = new FileOutputStream(fileName)) {
                logJobInfo("write data to file " + fileName);
                out.write(singleDocument);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Job: " + _currentJob.getId() + " error writing file: "
                        + fileName);
            }
            logJobInfo("end evaluation [" + evaluation.getId() + "]");
        }
    }

    private void createAccountDocument(String zipExcelFilesPath) {
        String accountDocumentZipFile = buildAccountDokZipFileName();
        List<String> filesForZip = new ArrayList<>();
        filesForZip.add(zipExcelFilesPath);
        filesForZip.add(createDocumentInfoFile());

        zipFiles(accountDocumentZipFile, filesForZip);
        copyAccountDocumentZipToDataPortal(accountDocumentZipFile);
    }

    private void copyAccountDocumentZipToDataPortal(String zipFilePath) {
        Path destination = Paths.get(_config.readConfig(ConfigKey.KhComparisonUploadPath));
        Path source = Paths.get(zipFilePath);

        try {
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String createDocumentInfoFile() {
        String documentInfoFileName = buildDocumentInfoFileName();
        StringBuilder docInfo = new StringBuilder();
        docInfo.append("Version=1.0\n");
        docInfo.append("Account.Id=" + _currentJob.getHosptalComparisonInfo().getAccountId() + " \n");
        docInfo.append("Document.Domain=Sonstige");
        File docFile = new File(documentInfoFileName);
        try {
            logJobInfo("try to write documentinfo [" + docInfo.toString() + "] to file: [" + documentInfoFileName + "]");
            Files.write(docFile.toPath(), docInfo.toString().getBytes());
        } catch (IOException ex) {
            throw new IllegalArgumentException("Job " + _currentJob.getId() + " error write documeninfo file " + ex.getMessage());
        }
        return documentInfoFileName;
    }

    private void zipFiles(String zipFilePath, List<String> filesForZip) {
        try {
            logJobInfo("try to zip files: [" + filesForZip + "]");
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
            logJobInfo("end zip files");
        } catch (Exception ex) {
            throw new IllegalArgumentException("Job " + _currentJob.getId() + " error during zip files [" + filesForZip + "]: " + ex.getMessage());
        }
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
        return _currentJob.getEvaluationFilePath(_config.readConfig(ConfigKey.KhComparisonJobSavePath));
    }

    private String buildAccountDokZipFileName() {
        String fileNamePattern = "KH-Vergleich_%s_%s.zip";
        return _jobSaveFile + "\\" + String.format(fileNamePattern, _currentJob.getHosptalComparisonInfo().getHospitalIk(),
                new SimpleDateFormat("ddMMyyyyHHmmss").format(Calendar.getInstance().getTime()));
    }

    private String buildExcelFileName(HospitalComparisonEvaluation evaluation) {
        String fileNamePattern = "%s_%s_PSY-KH_Vergleich_Auswertung.xlsx";
        return _jobSaveFile + "\\" + String.format(fileNamePattern, evaluation.getHospitalComparisonInfo().getHospitalIk(),
                evaluation.getEvalutationHcId());
    }

    private String buildDocumentInfoFileName() {
        String fileNamePattern = "DocInfo_%s_%s.DataportalDocumentInfo";
        return _jobSaveFile + "\\" + String.format(fileNamePattern, _currentJob.getId(),
                new SimpleDateFormat("ddMMyyyyHHmmss").format(Calendar.getInstance().getTime()));
    }

    private String buildUrlWithParameter(int hcId, int aebIdHospital, String evaluationGroupeIds) {
        String result = _reportController.getReportTemplateByName("KH-Vergleich Auswertung").getAddress();
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
            logJobInfo(job, "trying to lock job");
            _currentJob = _aebFacade.save(job);
            logJobInfo(job, "locked job");
            return true;
        } catch (OptimisticLockException ex) {
            return false;
        }
    }

    private void updateJobStatus(HospitalComparisonJob job, PsyHosptalComparisonStatus status) {
        switch (status) {
            case WORKING:
                job.setStartWorking(new Timestamp(new Date().getTime()));
                break;
            case DONE:
            case ERROR:
            case NO_EVALUATIONS:
                job.setEndWorking(new Timestamp(new Date().getTime()));
                break;
            default:
        }
        job.setStatus(status);
    }

    private void saveJob() {
        _currentJob = _aebFacade.save(_currentJob);
    }

    private void logJobInfo(String message) {
        logJobInfo(_currentJob, message);
    }

    private void logJobInfo(HospitalComparisonJob job, String message) {
        LOGGER.log(Level.INFO, "Job [" + job.getId() + "] " + message);
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
