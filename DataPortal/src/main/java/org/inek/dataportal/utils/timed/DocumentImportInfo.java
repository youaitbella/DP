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
import java.util.stream.Collectors;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.inject.Inject;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.utils.StreamUtils;

/**
 *
 * @author muellermi
 */
public class DocumentImportInfo {

    @Inject private AccountFacade _accountFacade;
    List<String> _infoFile = new ArrayList<>();
    private final Set<Integer> _accountIds = new HashSet<>();
    private final Map<String, String> _fileDomains = new HashMap<>();
    private final Map<String, byte[]> _files = new HashMap<>();
    private String _subject = "";
    private String _body = "";
    private String _error = "";
    private String _version = "";

    
    public DocumentImportInfo(File file, AccountFacade accountFacade) {
        try {
            extractFiles(file);
            extractInfos(accountFacade);
        } catch (IOException ex) {
            _error = ex.getMessage();
        }
    }

    private void extractFiles(File file) throws IOException {
        try (
                FileInputStream fis = new FileInputStream(file);
                CheckedInputStream checksum = new CheckedInputStream(fis, new Adler32());
                ZipInputStream zis = new ZipInputStream(new BufferedInputStream(checksum))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().equalsIgnoreCase("DataportalDocument.info")) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(zis));
                    _infoFile = reader.lines().collect(Collectors.toList());
                } else {
                    _files.put(entry.getName(), StreamUtils.stream2blob(zis));
                }
            }
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
                _error = "Invalid DataportalDocument.info";
                break;
            }
            String key = parts[0].trim().toLowerCase();
            String value = parts[1].trim();

            switch (key) {
                case "account.id":
                    _accountIds.add(Integer.parseInt(value));
                    break;
                case "account.mail":
                    try {
                        _accountIds.add(accountFacade.findByMail(value).getId());
                    } catch (Exception ex) {
                    }
                    break;
                case "version":
                    _version = value;
                    break;
                case "approval.id":
                    // todo
                    break;
                case "approval.mail":
                    // todo
                    break;
                case "subject":
                    _subject = value;
                    break;
                case "body":
                    body.append(value).append("\n");
                    break;
                default:
                    if (key.startsWith("$") && key.endsWith("$")) {
                        String name = key.substring(1, key.length() - 1);
                        _fileDomains.put(name, value);
                    }
            }
        }
        _body = body.toString();
    }


    public Set<Integer> getAccountIds() {
        return _accountIds;
    }

    public Map<String, String> getFileDomains() {
        return _fileDomains;
    }

    public Map<String, byte[]> getFiles() {
        return _files;
    }

    public String getSubject() {
        return _subject;
    }

    public String getBody() {
        return _body;
    }

    public String getError() {
        return _error;
    }

    public String getVersion() {
        return _version;
    }

    public boolean isValid (){
        return _error.isEmpty() && _version.equals("1.0");
    }
}
