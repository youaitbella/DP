/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.utils.timed;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.utils.StreamUtils;
import org.inek.portallib.util.Helper;

/**
 *
 * @author muellermi
 */
public class DocumentImportInfo {

    private static final Logger _logger = Logger.getLogger("DocumentLoader");

    List<String> _infoFile = new ArrayList<>();
    private final Set<Account> _accounts = new HashSet<>();
    private final Map<String, String> _fileDomains = new HashMap<>();
    private final Map<String, byte[]> _files = new HashMap<>();
    private String _sender = "datenportal@inek.org";
    private String _bcc = "fehlerverfahren@inek-drg.de";
    private String _subject = "";
    private String _body = "";
    private String _error = "";
    private String _version = "";
    private String _parent = "";
    private Account _uploadAccount;
    private Account _approvalAccount;
    private String _defaultDomain = "";
    private int _ik = -1;

    public DocumentImportInfo(File file, AccountFacade accountFacade) {
        try {
            extractFiles(file);
            extractInfos(accountFacade);
            _parent = file.getParentFile().getParentFile().getName();
        } catch (IOException ex) {
            _error = Helper.collectException(ex, 0);
        }
    }

    private void extractFiles(File file) throws IOException {
        try (
                FileInputStream fis = new FileInputStream(file);
                CheckedInputStream checksum = new CheckedInputStream(fis, new Adler32());
                ZipInputStream zis = new ZipInputStream(new BufferedInputStream(checksum))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().endsWith(".DataportalDocumentInfo")) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(zis, "UTF-8"));
                    _infoFile = reader.lines().collect(Collectors.toList());
                    String firstLine = _infoFile.get(0);
                    if (firstLine.startsWith("\uFEFF")){
                        // ignore BOM if present
                        _infoFile.set(0, firstLine.substring(1));
                    }
                } else {
                    _files.put(entry.getName(), StreamUtils.stream2blob(zis));
                }
            }
        } catch (Exception ex) {
            _logger.log(Level.SEVERE, "Couldn't extract import document. (" + file.getName() + "). " + ex.getMessage());
        }
    }

    private void extractInfos(AccountFacade accountFacade) {
        StringBuilder body = new StringBuilder();

        for (String line : _infoFile) {
            if (body.length() > 0) {
                body.append(line).append("\n");
                continue;
            }
            String[] parts = line.split("=");
            if (parts.length != 2) {
                _error = "Invalid DataportalDocumentInfo";
                break;
            }
            String key = parts[0].trim().toLowerCase();
            String value = parts[1].trim();

            switch (key) {
                case "ik":
                    try {
                        _ik = Integer.parseInt(value);
                    } catch (NumberFormatException ex) {
                        _logger.log(Level.WARNING, "Parse IK, not a number {0}", value);
                    }
                    break;
                case "account.id":
                    try {
                        _accounts.add(accountFacade.find(Integer.parseInt(value)));
                    } catch (Exception ex) {
                        _logger.log(Level.WARNING, "Unknown account id");
                    }
                    break;
                case "account.mail":
                    Account account = accountFacade.findByMail(value);
                    if (account == null) {
                        _logger.log(Level.WARNING, "Unknown account mail {0}", value);
                    } else {
                        _accounts.add(account);
                    }
                    break;
                case "version":
                    _version = value;
                    break;
                case "document.domain":
                    _defaultDomain = value;
                    break;
                case "approval.id":
                    try {
                        _approvalAccount = accountFacade.find(Integer.parseInt(value));
                    } catch (Exception ex) {
                        _logger.log(Level.WARNING, "Unknown account id");
                        getDefaultApprovalAccount(accountFacade);
                    }
                    break;
                case "approval.mail":
                    Account approvalAccount = accountFacade.findByMail(value);
                    if (approvalAccount == null) {
                        _logger.log(Level.WARNING, "Unknown account mail {0}", value);
                        getDefaultApprovalAccount(accountFacade);
                    } else {
                        _approvalAccount = approvalAccount;
                    }
                    break;
                case "upload.id":
                    try {
                        _uploadAccount = accountFacade.find(Integer.parseInt(value));
                    } catch (Exception ex) {
                        _logger.log(Level.WARNING, "Unknown account id");
                    }
                    break;
                case "upload.mail":
                    try {
                        _uploadAccount = accountFacade.findByMail(value);
                    } catch (Exception ex) {
                        _logger.log(Level.WARNING, "Unknown account mail");
                    }
                    break;
                case "mail.sender":
                    if (value.matches("(\\w[a-zA-Z_0-9+-.]*\\w|\\w+)@(\\w(\\w|-|\\.)*\\w|\\w+)\\.[a-zA-Z]+")) {
                        _sender = value;
                    } else {
                        _logger.log(Level.WARNING, "Wrong format sender");
                    }
                    break;
                case "mail.bcc":
                    _bcc = value;
                    break;
                case "mail.subject":
                    _subject = value;
                    break;
                case "mail.body":
                    body.append(value).append("\n");
                    break;
                default:
                    if (key.startsWith("$") && key.endsWith("$")) {
                        String name = key.substring(1, key.length() - 1).toLowerCase();
                        _fileDomains.put(name, value);
                    }
            }
        }
        _body = body.toString();
        if (_uploadAccount == null) {
            _uploadAccount = accountFacade.find(0);
        }
        if (_approvalAccount == null) {
            _approvalAccount = accountFacade.find(0);
        }
    }

    public Set<Account> getAccounts() {
        return _accounts;
    }

    public int getIk() {
        return _ik;
    }

    public Account getUploadAccount() {
        return _uploadAccount;
    }

    public Account getApprovalAccount() {
        return _approvalAccount;
    }

    public Map<String, String> getFileDomains() {
        return _fileDomains;
    }

    public Map<String, byte[]> getFiles() {
        return _files;
    }

    public String getSender() {
        return _sender;
    }

    public String getBcc() {
        return _bcc;
    }

    public String getSubject() {
        return _subject;
    }

    public String getBody() {
        return _body;
    }

    public String getError() {
        return _accounts.isEmpty() ? "[no valid account found] " + _error : _error;
    }

    public String getVersion() {
        return _version;
    }

    public boolean isValid() {
        return _error.isEmpty() && _version.equals("1.0") && !_accounts.isEmpty();
    }

    public String getParent() {
        return _parent;
    }

    String getDomain(String name) {
        String key = name.toLowerCase();
        if (_fileDomains.containsKey(key)) {
            return _fileDomains.get(key);
        }
        if (_defaultDomain.isEmpty()) {
            return _parent;
        }
        return _defaultDomain;
    }

    private void getDefaultApprovalAccount(AccountFacade accountFacade) {
        if (_approvalAccount != null) {
            return;
        }
        _approvalAccount = accountFacade.findByMail("dirk.bauder@inek-drg.de");  // todo: make configurable
        if (_approvalAccount == null) {
            _approvalAccount = accountFacade.findByMail("michael.mueller@inek-drg.de");  // todo: make configurable
        }
    }

}
