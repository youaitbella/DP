package org.inek.dataportal.utils.timed;

import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.account.AccountDocument;
import org.inek.dataportal.entities.account.DocumentDomain;
import org.inek.dataportal.entities.account.WaitingDocument;
import org.inek.dataportal.entities.admin.MailTemplate;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.facades.account.AccountDocumentFacade;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.account.DocumentDomainFacade;
import org.inek.dataportal.facades.account.WaitingDocumentFacade;
import org.inek.dataportal.facades.admin.ConfigFacade;
import org.inek.dataportal.mail.Mailer;

/**
 *
 * @author muellermi
 */
@Singleton
@Startup
public class DocumentLoader {

    private static final Logger _logger = Logger.getLogger("DocumentLoader");
    private final static String ImportDir = "Import.Dataportal";

    @Inject private ConfigFacade _config;
    @Inject private AccountFacade _accountFacade;
    @Inject private AccountDocumentFacade _accountDocFacade;
    @Inject private WaitingDocumentFacade _waitingDocFacade;
    @Inject private DocumentDomainFacade _docDomain;
    @Inject private Mailer _mailer;

    @Schedule(hour = "*", minute = "*/5", info = "every 5 minutes")
    private void monitorDocumentRoot() {
        File baseDir = new File(_config.read(ConfigKey.DocumentScanBase));
        for (File dir : baseDir.listFiles()) {
            if (dir.isDirectory()) {
                checkDocumentFolder(dir);
            }
        }
    }

    private void checkDocumentFolder(File dir) {
        if (!_config.readBool(ConfigKey.DocumentScanDir, dir.getName())) {
            return;
        }
        File importDir = new File(dir, ImportDir);
        importDir.mkdir();
        for (File file : dir.listFiles(f -> f.isFile() && f.getName().toLowerCase().endsWith(".zip"))) {
            prepareImport(file);
        }
        for (File file : importDir.listFiles(f -> f.isFile() && f.getName().toLowerCase().endsWith(".zip"))) {
            handleContainer(file);
        }
    }

    private void prepareImport(File file) {
        try {
            File importDir = new File(file.getParent(), ImportDir);
            file.renameTo(new File(importDir, file.getName()));
        } catch (Exception ex) {
            _logger.log(Level.INFO, "Could not rename {0}", file.getName());
        }
    }

    private void handleContainer(File file) {
        DocumentImportInfo importInfo = new DocumentImportInfo(file, _accountFacade);
        if (!importInfo.isValid()) {
            _logger.log(Level.WARNING, "Could not import {0}", importInfo.getError());
            return;
        }
        if (importInfo.getFiles().isEmpty()){
            _logger.log(Level.WARNING, "No files found to import for container {0}", file.getName());
            return;
        }

        if (importInfo.getApprovalAccount().getId() > 0) {
            createWaitingDocuments(importInfo);
        } else {
            createDocuments(importInfo);
        }
        File importedDir = new File(file.getPath(), "Imported");
        if(!importedDir.exists())
            importedDir.mkdir();
        try {
            Files.move(file, importedDir);
        } catch(IOException ex) {
            file.delete();
            _logger.log(Level.WARNING, "Moving file {1} to Imported folder failed: {0}", new Object[]{ex.getMessage(), file.getName()});
        }
    }

    private void createDocuments(DocumentImportInfo importInfo) {
        int validity = _config.readInt(ConfigKey.ReportValidity);
        Map<String, byte[]> files = importInfo.getFiles();
        for (Account account : importInfo.getAccounts()) {
            String subject = importInfo.getSubject();
            String body = importInfo.getBody();
            String bcc = "fehlerverfahren@inek-drg.de";

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
        _logger.log(Level.INFO, "Document created: {0} for account {1}", new Object[]{name, account.getId()});
    }

    private void createWaitingDocuments(DocumentImportInfo importInfo) {
        int validity = _config.readInt(ConfigKey.ReportValidity);
        Map<String, byte[]> files = importInfo.getFiles();

        Account agent = importInfo.getApprovalAccount();
        String subject = importInfo.getSubject();
        String body = importInfo.getBody();
        String bcc = "fehlerverfahren@inek-drg.de";

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
        String salutation = _mailer.getFormalSalutation(agent);
        body = body.replace("{formalSalutation}", salutation);
        _mailer.sendMailFrom(importInfo.getSender(), agent.getEmail(), bcc, subject, body);
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
        _logger.log(Level.INFO, "Document created for approval: {0} for account {1}", new Object[]{name, importInfo.getApprovalAccount().getId()});
    }

}
