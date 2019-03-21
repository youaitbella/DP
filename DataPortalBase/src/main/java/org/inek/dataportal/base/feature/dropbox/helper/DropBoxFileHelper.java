package org.inek.dataportal.base.feature.dropbox.helper;

import org.inek.dataportal.base.feature.dropbox.entities.DropBox;
import org.inek.dataportal.base.feature.dropbox.entities.DropBoxItem;
import org.inek.dataportal.base.feature.dropbox.facade.DropBoxFacade;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.helper.StreamHelper;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DropBoxFileHelper {

    private static final Logger LOGGER = Logger.getLogger(DropBoxFileHelper.class.getName());

    public static void deleteFileFromDropBox(DropBox dropBox, String fileName) {
        File file = new File(dropBox.getUploadDir(), fileName);
        file.delete();
        LOGGER.log(Level.INFO, "Delete File from DropBox: " + dropBox.getDropBoxId() + " File: " + fileName);
    }

    public static List<DropBoxItem> getUploadedFiles(DropBox dropBox) {
        if (!dropBox.isComplete()) {
            files2items(dropBox);
        }
        return dropBox.getItems();
    }

    private static void files2items(DropBox dropBox) throws IllegalStateException {
        List<DropBoxItem> files = dropBox.getItems();
        if (dropBox.getUploadDir().exists()) {
            files.clear();
            for (File f : dropBox.getUploadDir().listFiles()) {
                DropBoxItem item = new DropBoxItem();
                item.setName(f.getName());
                item.setSize(f.length());
                files.add(item);
            }
        }
    }

    public static File sealDropBox(DropBoxFacade _dropBoxFacade, DropBox dropBox, String uploadRoot, Account account) throws IOException {
        files2items(dropBox);
        dropBox.setComplete(true);
        addEmailInfo(dropBox, account);
        File target = moveFiles2Target(dropBox, uploadRoot);
        dropBox.setName(target.getName());
        dropBox.setSealed(Calendar.getInstance().getTime());
        _dropBoxFacade.updateDropBox(dropBox);
        return target;
    }

    private static File moveFiles2Target(DropBox dropBox, String uploadRoot) throws IOException {
        File sourceDir = dropBox.getUploadDir();
        File workingFile = new File(sourceDir.getAbsolutePath() + ".zip");
        new StreamHelper().compressFiles(sourceDir.listFiles(), workingFile);
        File targetParent = new File(uploadRoot, dropBox.getDropboxType().getFolder());
        targetParent.mkdirs();
        File target;
        do {
            target = new File(targetParent, "Box" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) + "_"
                    + dropBox.getIK() + ".zip");
        } while (target.exists());
        workingFile.renameTo(target);
        if (!deleteDir(sourceDir)) {
            throw new IOException("Could not delete " + sourceDir.getAbsolutePath());
        }
        return target;
    }

    private static void addEmailInfo(DropBox dropBox, Account account) {
        PrintWriter pw = null;
        try {
            Date ts = Calendar.getInstance().getTime();
            String fileName = "EMailInfo" + new SimpleDateFormat("ddMMyyyyHHmmss").format(ts) + ".txt";
            File file = new File(dropBox.getUploadDir(), fileName);
            pw = new PrintWriter(new FileOutputStream(file));
            pw.println("From=" + account.getEmail());
            pw.println("Accept=Dropbox");
            pw.println("Received=" + new SimpleDateFormat("dd.MM.yyyy HH:mm").format(ts));
            pw.println("Subject=Dropbox_" + dropBox.getIK());
            pw.flush();
        } catch (FileNotFoundException ex) {
            throw new IllegalStateException(ex);
        } finally {
            pw.close();
        }
    }


    private static boolean deleteDir(File dir) {
        boolean isDeleted = true;
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                isDeleted &= deleteDir(file);
            } else {
                isDeleted &= file.delete();
            }
        }
        isDeleted &= dir.delete();
        return isDeleted;
    }

    public static Boolean dropBoxContainsDecryptedFiles(DropBox dropBox) {
        List<String> encryptedExtensions = new ArrayList<>();
        encryptedExtensions.add("zip");
        encryptedExtensions.add("rar");
        encryptedExtensions.add("gpg");
        encryptedExtensions.add("pgp");
        encryptedExtensions.add("7z");
        encryptedExtensions.add("inek");

        for (DropBoxItem item : dropBox.getItems()) {
            Boolean hasMatch = false;
            for (String extension : encryptedExtensions) {
                if (item.getName().endsWith(extension)) {
                    hasMatch = true;
                }
            }
            if (!hasMatch) {
                return true;
            }
        }
        return false;
    }
}
