package org.inek.dataportal.utils.timed;

import java.io.File;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
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

    @Schedule(hour = "*", minute = "*/1", info = "every minute")
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

        createDocuments(importInfo);
        file.delete();
    }

    private void createDocuments(DocumentImportInfo importInfo) {

        int validity = _config.readInt(ConfigKey.ReportValidity);
        Map<String, byte[]> files = importInfo.getFiles();
        for (Account account : importInfo.getAccounts()) {
            for (String name : files.keySet()) {
                if (importInfo.getApprovalAccount().getId() > 0) {
                    createWaitingDocument(account, files, name, validity, importInfo);
                } else {
                    createAccountDocument(account, files, name, validity, importInfo);
                }
            }
            Account receipient = importInfo.getApprovalAccount().getId() > 0 ? importInfo.getApprovalAccount() : account;
            String subject = importInfo.getSubject();
            String body = importInfo.getBody();
            String bcc = "fehlerverfahren@inek-drg.de";

            if (subject.isEmpty() || body.isEmpty()) {
                MailTemplate template = _mailer.getMailTemplate("Neue Dokumente");
                if (template == null) {
                    // dump fallback
                    subject = "Neue Dokumente im InEK Datenportal";
                    body = "Guten Tag,\n"
                            + "\n"
                            + "im InEK Datenportal sind neue Dokumente für Sie verfügbar.\n"
                            + "\n"
                            + "Freundliche Grüße\n"
                            + "InEK GmbH";

                } else {
                    String salutation = _mailer.getFormalSalutation(receipient);
                    body = template.getBody().replace("{formalSalutation}", salutation);
                    bcc = template.getBcc();
                    subject = template.getSubject();
                }
            }
            _mailer.sendMailFrom(importInfo.getSender(), receipient.getEmail(), bcc, subject, body);
        }

    }

    private void createWaitingDocument(Account account, Map<String, byte[]> files, String name, int validity, DocumentImportInfo importInfo) {
        WaitingDocument doc = new WaitingDocument();
        doc.setAccountId(account.getId());
        doc.setContent(files.get(name));
        doc.setName(name);
        doc.setValidity(validity);
        DocumentDomain domain = _docDomain.findOrCreateForName(importInfo.getDomain(name));
        doc.setDomain(domain);
        doc.setAgentAccountId(importInfo.getApprovalAccount().getId());
        _waitingDocFacade.save(doc);
    }

    private void createAccountDocument(Account account, Map<String, byte[]> files, String name, int validity, DocumentImportInfo importInfo) {
        AccountDocument doc = new AccountDocument();
        doc.setAccountId(account.getId());
        doc.setContent(files.get(name));
        doc.setName(name);
        doc.setValidity(validity);
        DocumentDomain domain = _docDomain.findOrCreateForName(importInfo.getDomain(name));
        doc.setDomain(domain);
        doc.setUploadAccountId(importInfo.getUploadAccount().getId());
        _accountDocFacade.save(doc);
    }
}
