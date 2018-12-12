package org.inek.documentScanner.business;

import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.account.entities.AccountDocument;
import org.inek.dataportal.common.data.account.entities.DocumentDomain;
import org.inek.dataportal.common.data.account.entities.WaitingDocument;
import org.inek.dataportal.common.data.account.facade.AccountFacade;
import org.inek.dataportal.common.data.adm.MailTemplate;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.mail.Mailer;
import org.inek.documentScanner.facade.DocumentScannerFacade;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author muellermi
 */
@Stateless
public class DocumentLoader {

    private static final Logger LOGGER = Logger.getLogger("DocumentLoader");
    private static final String IMPORT_DIR = "Import.Dataportal";
    private static final String ARCHIV_DIR = "Imported";
    private static final String FAILED_DIR = "Failed";


    @Inject
    private ConfigFacade _config;
    @Inject
    private AccountFacade _accountFacade;
    @Inject
    private DocumentScannerFacade _docFacade;
    @Inject
    private Mailer _mailer;

    private volatile int _waitCounter = 0;

    public static boolean moveFile(String targetFolder, File file) {
        File targetDir = new File(file.getParent(), targetFolder);
        try {
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }
            boolean success = file.renameTo(new File(targetDir, file.getName()));
            if (!success) {
                throw new IOException("Rename failed");
            }
            return true;

        } catch (NullPointerException | SecurityException | IOException ex) {
            String msg;
            if (new File(targetDir, file.getName()).exists()) {
                msg = "File {0} exists in target folder";
            } else {
                msg = "Could not rename {0}. Reason: " + ex.getMessage();
            }
            LOGGER.log(Level.INFO, msg, file.getName());
            file.delete();
            return false;
        }
    }

    private synchronized void setWaitCounter(int val) {
        _waitCounter = val;
    }

    private synchronized boolean waitAndDecrementCounter() {
        if (_waitCounter > 0) {
            _waitCounter--;
            return true;
        }
        return false;
    }

    @Asynchronous
    public void scanDocumentRoot() {
        if (waitAndDecrementCounter()) {
            return;
        }
        setWaitCounter(100);
        File baseDir = new File(_config.readConfig(ConfigKey.FolderRoot), _config.
                readConfig(ConfigKey.FolderDocumentScanBase));

        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }

        try {
            File lockFile = new File(baseDir, "DOCUMENT.LOCK");
            if (!lockFile.exists()) {
                lockFile.createNewFile();
            }
            try (FileChannel fileChannel = FileChannel.
                    open(lockFile.toPath(), StandardOpenOption.WRITE, StandardOpenOption.APPEND);
                 FileLock lock = fileChannel.lock()) {
                for (File dir : baseDir.listFiles()) {
                    if (dir.isDirectory()) {
                        LOGGER.log(Level.INFO, "Check document folder ({0})", dir);
                        checkDocumentFolder(dir);
                    }
                }
                setWaitCounter(0);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
            _mailer.sendError("Error Documenscanner", ex);
        }

    }

    private synchronized void checkDocumentFolder(File dir) {
        if (!_config.readConfigBool(ConfigKey.DocumentScanDir, dir.getName())) {
            LOGGER.log(Level.INFO, "Folder ({0}) not scanned (ConfigDisabled)", dir);
            return;
        }
        for (File file : dir.listFiles(f -> f.isFile() && f.getName().toLowerCase().endsWith(".zip"))) {
            moveFile(IMPORT_DIR, file);
        }
        File importDir = new File(dir, IMPORT_DIR);
        for (File file : importDir.listFiles(f -> f.isFile() && f.getName().toLowerCase().endsWith(".zip"))) {
            handleContainer(file);
        }
    }

    private synchronized void handleContainer(File file) {
        DocumentImportInfo importInfo = new DocumentImportInfo(file, _accountFacade);
        if (!importInfo.isValid()) {
            LOGGER.log(Level.WARNING, "Could not import {0}", importInfo.getError());
            moveFile(FAILED_DIR, file);
            return;
        }
        if (importInfo.getFiles().isEmpty()) {
            LOGGER.log(Level.WARNING, "No files found to import for container {0}", file.getName());
            return;
        }

        if (importInfo.getApprovalAccount().getId() > 0) {
            createWaitingDocuments(importInfo);
        } else {
            createDocuments(importInfo);
        }
        moveFile(ARCHIV_DIR, file);
    }

    private void createDocuments(DocumentImportInfo importInfo) {
        int validity = _config.readConfigInt(ConfigKey.ReportValidity);
        Map<String, byte[]> files = importInfo.getFiles();
        for (Account account : importInfo.getAccounts()) {
            String subject = importInfo.getSubject();
            String body = importInfo.getBody();
            String bcc = importInfo.getBcc();

            if (subject.isEmpty() || body.isEmpty()) {
                MailTemplate template = _mailer.getMailTemplate("Neue Dokumente");
                String salutation = _mailer.getFormalSalutation(account);
                body = template.getBody().replace("{formalSalutation}", salutation);
                bcc = template.getBcc();
                subject = template.getSubject();
            }

            for (String name : files.keySet()) {
                createAccountDocument(account, files, name, validity, importInfo);
            }
            _mailer.sendMailFrom(importInfo.getSender(), account.getEmail(), bcc, subject, body);
        }
    }

    private void createAccountDocument(Account account, Map<String, byte[]> files, String name, int validity,
                                       DocumentImportInfo importInfo) {
        AccountDocument doc = new AccountDocument();
        doc.setAccountId(account.getId());
        doc.setContent(files.get(name));
        doc.setName(name);
        doc.setValidity(validity);
        DocumentDomain domain = _docFacade.findOrCreateForName(importInfo.getDomain(name));
        doc.setDomain(domain);
        doc.setAgentAccountId(importInfo.getUploadAccount().getId());
        _docFacade.saveAccountDocument(doc);
        _docFacade.clearCache();
        LOGGER.log(Level.INFO, "Document created: {0} for account {1}", new Object[]{name, account.getId()});
    }

    private synchronized void createWaitingDocuments(DocumentImportInfo importInfo) {
        int validity = _config.readConfigInt(ConfigKey.ReportValidity);
        Map<String, byte[]> files = importInfo.getFiles();

        Account agent = importInfo.getApprovalAccount();
        String subject = importInfo.getSubject();
        String body = importInfo.getBody();
        String bcc = importInfo.getBcc();

        if (subject.isEmpty() || body.isEmpty()) {
            MailTemplate template = _mailer.getMailTemplate("Neue Dokumente");
            body = template.getBody();
            bcc = template.getBcc();
            subject = template.getSubject();
        }

        for (String name : files.keySet()) {
            JsonObject jsonMail = Json.createObjectBuilder()
                    .add("from", importInfo.getSender())
                    .add("bcc", bcc)
                    .add("subject", subject)
                    .add("body", body)
                    .build();
            createWaitingDocument(name, files.get(name), validity, importInfo, jsonMail);
        }

        MailTemplate template = _mailer.getMailTemplate("Neue Dokumente");
        body = template.getBody();
        subject = "Neue Dokumente: " + subject;
        String salutation = _mailer.getFormalSalutation(agent);
        body = body.replace("{formalSalutation}", salutation);
        _mailer.sendMailFrom(importInfo.getSender(), agent.getEmail(), "", subject, body);
    }

    private void createWaitingDocument(String name, byte[] content, int validity, DocumentImportInfo importInfo,
                                       JsonObject jsonMail) {
        WaitingDocument doc = new WaitingDocument();
        doc.getAccounts().addAll(importInfo.getAccounts());
        doc.setContent(content);
        doc.setName(name);
        doc.setValidity(validity);
        DocumentDomain domain = _docFacade.findOrCreateForName(importInfo.getDomain(name));
        doc.setDomain(domain);
        doc.setAgentAccountId(importInfo.getApprovalAccount().getId());
        doc.setIk(importInfo.getIk());
        doc.setJsonMail(jsonMail.toString());
        _docFacade.saveWaitingDocument(doc);
        LOGGER.log(Level.INFO, "Document created for approval: {0} for account {1}",
                new Object[]{name, importInfo.getApprovalAccount().getId()});
    }

}
