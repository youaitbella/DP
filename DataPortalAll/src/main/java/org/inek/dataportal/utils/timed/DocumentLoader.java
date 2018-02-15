package org.inek.dataportal.utils.timed;

import java.io.File;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Asynchronous;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.account.AccountDocument;
import org.inek.dataportal.entities.account.DocumentDomain;
import org.inek.dataportal.entities.account.WaitingDocument;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.facades.account.AccountDocumentFacade;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.account.DocumentDomainFacade;
import org.inek.dataportal.facades.account.WaitingDocumentFacade;
import org.inek.dataportal.common.data.adm.MailTemplate;
import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.mail.Mailer;

/**
 *
 * @author muellermi
 */
public class DocumentLoader {

    private static final Logger LOGGER = Logger.getLogger("DocumentLoader");
    private static final String IMPORT_DIR = "Import.Dataportal";
    private static final String ARCHIV_DIR = "Imported";
    private static final String FAILED_DIR = "Failed";

    @Inject private ConfigFacade _config;
    @Inject private AccountFacade _accountFacade;
    @Inject private AccountDocumentFacade _accountDocFacade;
    @Inject private WaitingDocumentFacade _waitingDocFacade;
    @Inject private DocumentDomainFacade _docDomain;
    @Inject private Mailer _mailer;

    private volatile int _waitCounter = 0;

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
    public void monitorDocumentRoot() {
        try {
            if (waitAndDecrementCounter()) {
                return;
            }
            setWaitCounter(100);
            File baseDir = new File(_config.read(ConfigKey.FolderRoot), _config.read(ConfigKey.FolderDocumentScanBase));
            if (!baseDir.exists()) {
                baseDir.mkdirs();
            }
            for (File dir : baseDir.listFiles()) {
                if (dir.isDirectory()) {
                    LOGGER.log(Level.INFO, "Check document folder ({0})", dir);
                    checkDocumentFolder(dir);
                }
            }
            setWaitCounter(0);
        } catch (Exception ex) {
            // baseDir.listFiles() might become null if the drive is not available
            // log (to detect possible other faults) and ignore 
            LOGGER.log(Level.SEVERE, ex.getMessage());
        }
    }

    private synchronized void checkDocumentFolder(File dir) {
        if (!_config.readBool(ConfigKey.DocumentScanDir, dir.getName())) {
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

    private void moveFile(String targetFolder, File file) {
        try {
            File targetDir = new File(file.getParent(), targetFolder);
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }
            file.renameTo(new File(targetDir, file.getName()));
        } catch (Exception ex) {
            LOGGER.log(Level.INFO, "Could not rename {0}", file.getName());
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
        int validity = _config.readInt(ConfigKey.ReportValidity);
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

    private void createAccountDocument(Account account, Map<String, byte[]> files, String name, int validity, DocumentImportInfo importInfo) {
        AccountDocument doc = new AccountDocument();
        doc.setAccountId(account.getId());
        doc.setContent(files.get(name));
        doc.setName(name);
        doc.setValidity(validity);
        DocumentDomain domain = _docDomain.findOrCreateForName(importInfo.getDomain(name));
        doc.setDomain(domain);
        doc.setAgentAccountId(importInfo.getUploadAccount().getId());
        _accountDocFacade.save(doc);
        _accountDocFacade.clearCache();
        LOGGER.log(Level.INFO, "Document created: {0} for account {1}", new Object[]{name, account.getId()});
    }

    private synchronized void createWaitingDocuments(DocumentImportInfo importInfo) {
        int validity = _config.readInt(ConfigKey.ReportValidity);
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

    private void createWaitingDocument(String name, byte[] content, int validity, DocumentImportInfo importInfo, JsonObject jsonMail) {
        WaitingDocument doc = new WaitingDocument();
        //doc.setAccountId(account.getId());
        doc.getAccounts().addAll(importInfo.getAccounts());
        doc.setContent(content);
        doc.setName(name);
        doc.setValidity(validity);
        DocumentDomain domain = _docDomain.findOrCreateForName(importInfo.getDomain(name));
        doc.setDomain(domain);
        doc.setAgentAccountId(importInfo.getApprovalAccount().getId());
        doc.setIk(importInfo.getIk());
        doc.setJsonMail(jsonMail.toString());
        _waitingDocFacade.save(doc);
        LOGGER.log(Level.INFO, "Document created for approval: {0} for account {1}", new Object[]{name, importInfo.getApprovalAccount().getId()});
    }

}
