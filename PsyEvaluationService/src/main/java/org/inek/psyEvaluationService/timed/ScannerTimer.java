package org.inek.psyEvaluationService.timed;

import org.eclipse.persistence.exceptions.OptimisticLockException;
import org.inek.dataportal.common.controller.ReportController;
import org.inek.dataportal.common.data.KhComparison.entities.HospitalComparisonEvaluation;
import org.inek.dataportal.common.data.KhComparison.entities.HospitalComparisonHospitals;
import org.inek.dataportal.common.data.KhComparison.entities.HospitalComparisonJob;
import org.inek.dataportal.common.data.KhComparison.entities.InekComparisonJob;
import org.inek.dataportal.common.data.KhComparison.enums.PsyHosptalComparisonStatus;
import org.inek.dataportal.common.data.KhComparison.facade.AEBFacade;
import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.mail.Mailer;
import org.inek.psyEvaluationService.backingBean.MessageProvider;

import javax.annotation.Resource;
import javax.ejb.Timer;
import javax.ejb.*;
import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

import static org.inek.dataportal.common.data.KhComparison.enums.PsyHosptalComparisonStatus.WORKING;

@Singleton
@Startup
public class ScannerTimer {

    private static final Logger LOGGER = Logger.getLogger(ScannerTimer.class.toString());

    //@Inject
    private AEBFacade _aebFacade;
    //@Inject
    private ConfigFacade _config;
    //@Inject
    private ReportController _reportController;
    //@Inject
    private Mailer _mailer;
    //@Inject
    private MessageProvider _messageProvider;

    @Resource
    private TimerService _timerService;
    private HospitalComparisonJob _currentJob;
    private File _jobSaveFile;

    public ScannerTimer() {
    }

    @Inject
    public ScannerTimer(AEBFacade _aebFacade, ConfigFacade _config, ReportController _reportController, Mailer _mailer, MessageProvider _messageProvider) {
        this._aebFacade = _aebFacade;
        this._config = _config;
        this._reportController = _reportController;
        this._mailer = _mailer;
        this._messageProvider = _messageProvider;
    }

    @Timeout
    public void execute() {
        try {
            Optional<HospitalComparisonJob> oldestNewJob = _aebFacade.getOldestNewJob();
            oldestNewJob.ifPresent(this::startProcessingJob);
            Optional<InekComparisonJob> oldestNewInekJob = _aebFacade.getOldestNewInekJob();
            oldestNewInekJob.ifPresent(this::startProcessingInekJob);
        } catch (Exception ex) {
//            _mailer.sendMail("PortalAdmin@inek-drg.de", "PsyEvaluationService error: " + ex.getMessage(),
//                    ex.getStackTrace().toString());
//            updateJobStatus(_currentJob, PsyHosptalComparisonStatus.ERROR);
//            saveJob();
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            _currentJob = null;
            _jobSaveFile = null;
        }
    }

    private void startProcessingInekJob(InekComparisonJob inekComparisonJob) {
         try {
             InekComparisonJob job = tryToLockInekJob(inekComparisonJob);
             logJobInfo(job, "start processing");
             createFolderForInekJob(job);
             processingInekEvaluations(job);

             String evaluationsZipPath = zipInekEvaluations(job);
             //createAccountDocument(evaluationsZipPath); wichtig für download?
             updateInekJobStatus(job, PsyHosptalComparisonStatus.DONE);
             saveInekJob(job);
             logJobInfo(job, "end processing");
         } catch (OptimisticLockException ex) {
             logJobInfo(inekComparisonJob, "was already locked by another process");
         }
    }

    private void startProcessingJob(HospitalComparisonJob job) {
        if (tryToLockJob(job)) {
            logJobInfo(job, "start processing");
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
            logJobInfo(job, "end processing");
        } else {
            logJobInfo(job, "was already locked by another process");
        }
    }

    private String zipEvaluations() {
        String zipFileName = buildZipFileName();
        zipFiles(zipFileName, getAllExcelFilePaths());
        return zipFileName;
    }

    private String zipInekEvaluations(InekComparisonJob job) {
        String zipFileName = buildInekZipFileName(job);
        zipFiles(job, zipFileName, getAllExcelFilePaths(job));
        return zipFileName;
    }

    private String buildInekZipFileName(InekComparisonJob job) {
        return job.getJobFolder(this.determineInekCompareSaveFolder()) + "/" + job.obtainFileName() + ".zip";
    }

    private void createFolderForJob() {
        _jobSaveFile = new File(_currentJob.getJobFolder(determineSaveFolder()));
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

    private void createFolderForInekJob(InekComparisonJob job) {
        try {
            Files.createDirectories(Paths.get(job.getJobFolder(determineInekCompareSaveFolder())));
        } catch (IOException e) {
            throw new IllegalArgumentException("Error creating directory for job: " + _currentJob.getId(), e);
        }
    }

    private void processingEvaluations() {
        for (HospitalComparisonEvaluation evaluation : _currentJob.getHosptalComparisonInfo().getHospitalComparisonEvaluation()) {
            logJobInfo("start evaluation [" + evaluation.getId() + "]");
            int aebIdHospital = evaluation.getHospitalComparisonHospitalsHospital().getAebBaseInformationId();
            String aebIdsGroupe = concatAebIds(evaluation.getHospitalComparisonHospitals());
            String fileName = buildExcelFileName(evaluation);
            String reportUrl = buildUrlWithParameter(evaluation.getId(), aebIdHospital, aebIdsGroupe, fileName.replace("\\", "/"));

            logJobInfo("request document from " + reportUrl);

            //TODO antwort vom server interpretieren
            byte[] requestAnswer = _reportController.getSingleDocument(reportUrl);

            waitForFile(fileName, 300);

            logJobInfo("end evaluation [" + evaluation.getId() + "]");
        }
    }

    private void processingInekEvaluations(InekComparisonJob job) {
        logJobInfo(job, "start InEK Evaluation ");
        String fileName = determineInekCompareSaveFolder();
        String reportUrl = buildInekVergleichUrlWithParameter(job.getId(), fileName.replace("\\", "/"));

        logJobInfo(job, "request document from " + reportUrl);

        //TODO Antwort vom server interpretieren
        byte[] requestAnswer = _reportController.getSingleDocument(reportUrl);

        logJobInfo(job, "end InEK Evaluation");
    }

    private void waitForFile(String fileName, int maxLoops) {
        for (int i = 0; i < maxLoops; i++) {
            if (Files.exists(Paths.get(fileName))) {
                logJobInfo("file found " + fileName);
                return;
            } else {
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
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
        Path source = Paths.get(zipFilePath);
        Path destination = Paths.get(determineUploadFolder() + "/" + source.getFileName());

        try {
            logJobInfo("copy [" + source + "] to [" + destination + "]");
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
            logJobInfo("successfully copy [" + source + "] to [" + destination + "]");
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
        logJobInfo("try to zip files to [" + zipFilePath + "] files [" + filesForZip + "]");
        try {
            zipFilesOnly(zipFilePath, filesForZip);
            logJobInfo("end zip files");
        } catch (Exception ex) {
            throw new IllegalArgumentException("Job " + _currentJob.getId() + " error during zip file [" + zipFilePath + "] files [" +
                    filesForZip + "]: " + ex.getMessage());
        }
    }

    private void zipFiles(InekComparisonJob job, String zipFilePath, List<String> filesForZip) {
        logJobInfo(job, "try to zip files to [" + zipFilePath + "] files [" + filesForZip + "]");
        try {
            zipFilesOnly(zipFilePath, filesForZip);
            logJobInfo(job, "end zip files");
        } catch (Exception ex) {
            throw new IllegalArgumentException("Job " + job.getId() + " error during zip file [" + zipFilePath + "] files [" +
                    filesForZip + "]: " + ex.getMessage());
        }
    }
    private void zipFilesOnly(String zipFilePath, List<String> filesForZip) throws Exception {
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

    private List<String> getAllExcelFilePaths(InekComparisonJob job) {
        try {
            return Files.walk(Paths.get(job.getJobFolder(determineInekCompareSaveFolder())))
                    .map(c -> c.toString())
                    .filter(c -> c.endsWith(".xlsx"))
                    .collect(Collectors.toList());
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new IllegalArgumentException("InEK Job " + job.getId() + " error during collect excel filesnames for zip: " + ex.getMessage());
        }
    }

    private String buildZipFileName() {
        return _currentJob.getEvaluationFilePath(determineSaveFolder());
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

    private String buildUrlWithParameter(int hcId, int aebIdHospital, String evaluationGroupeIds, String savePath) {
        String result = _reportController.getReportTemplateByName("KH-Vergleich Auswertung").getAddress();
        try {
            result = result.replace("{0}", String.valueOf(hcId));
            result = result.replace("{1}", String.valueOf(aebIdHospital));
            result = result.replace("{2}", evaluationGroupeIds);
            result = result.replace("{3}", URLEncoder.encode(savePath, StandardCharsets.UTF_8.toString()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    private String buildInekVergleichUrlWithParameter(int jobId, String savePath) {
        try {
            return _reportController.getReportTemplateByName("Inek-Vergleich Auswertung").getAddress()
                    .replace("{0}", String.valueOf(jobId))
                    .replace("{1}", URLEncoder.encode(savePath, StandardCharsets.UTF_8.toString()));
        } catch (Exception ex) {
            throw new IllegalArgumentException(
                    "Could not generate URL for Inek-Vergleich Auswertung with jobId" + jobId + " savePath " + savePath, ex);
        }
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

    private InekComparisonJob tryToLockInekJob(InekComparisonJob inekComparisonJob) throws OptimisticLockException {
        updateInekJobStatus(inekComparisonJob, WORKING);
        logJobInfo(inekComparisonJob, "trying to lock job");
        InekComparisonJob comparisonJob = _aebFacade.saveInekComparison(inekComparisonJob);
        logJobInfo(comparisonJob, "locked job");
        return comparisonJob;
    }

    private void updateInekJobStatus(InekComparisonJob inekComparisonJob, PsyHosptalComparisonStatus status) {
        if (WORKING.equals(status)) {
            inekComparisonJob.setStartWorkingToNow();
        } else {
            inekComparisonJob.setEndWorkingToNow();
        }
        inekComparisonJob.setStatus(status);
    }

    private boolean tryToLockJob(HospitalComparisonJob job) {
        updateJobStatus(job, WORKING);

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

    private void saveInekJob(InekComparisonJob job) {
        _aebFacade.saveInekComparison(job);
    }

    private void logJobInfo(String message) {
        logJobInfo(_currentJob, message);
    }

    private void logJobInfo(HospitalComparisonJob job, String message) {
        _messageProvider.addMessage("Job [" + job.getId() + "] " + message);
    }

    private void logJobInfo(InekComparisonJob job, String message) {
        _messageProvider.addMessage("InekComparisonJob [" + job.getId() + "] " + message);
    }

    public void startTimer() {
        ScheduleExpression expression = new ScheduleExpression();
        expression.second("*/30").minute("*").hour("*");
        _timerService.createCalendarTimer(expression);
        _messageProvider.addMessage("Timer started");
    }

    public void stopTimer() {
        for (Timer allTimer : _timerService.getAllTimers()) {
            allTimer.cancel();
        }
        _messageProvider.addMessage("Timer stopped");
    }

    private String determineSaveFolder() {
        String rootFolder = _config.readConfig(ConfigKey.FolderRoot);
        return _config.readConfig(ConfigKey.KhComparisonJobSavePath).replace("{root}", removeTrailingSlash(rootFolder));
    }

    private String determineInekCompareSaveFolder() {
        String rootFolder = _config.readConfig(ConfigKey.FolderRoot);
        return _config.readConfig(ConfigKey.InekComparisonJobSavePath).replace("{root}", removeTrailingSlash(rootFolder));
    }

    private String removeTrailingSlash(String rootFolder) {
        int lastCharIsSlash = ("/".equals(rootFolder.substring(rootFolder.length() - 1)) ? 1 : 0);
        return rootFolder.substring(0, rootFolder.length() - lastCharIsSlash);
    }

    private String determineUploadFolder() {
        String rootFolder = _config.readConfig(ConfigKey.FolderRoot);
        return _config.readConfig(ConfigKey.KhComparisonUploadPath).replace("{root}", removeTrailingSlash(rootFolder));
    }


}
