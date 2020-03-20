package org.inek.dataportal.base.feature.dropbox.helper;

import org.inek.dataportal.base.feature.dropbox.entities.DropBox;
import org.inek.dataportal.base.feature.dropbox.entities.DropBoxItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DropBoxFileHelperTest {

    @Test
    void dropBoxContainsDecryptedFilesReturnsTrueForSingleNonEncryptedFile() {
        DropBox dropBox = new DropBox();
        DropBoxItem dropBoxItem = new DropBoxItem();
        dropBoxItem.setName("DUMMY.XLSX");
        dropBox.getItems().add(dropBoxItem);
        assertTrue(DropBoxFileHelper.dropBoxContainsDecryptedFiles(dropBox));
    }

    @Test
    void dropBoxContainsDecryptedFilesReturnsTrueForMultipleNonEncryptedFiles() {
        DropBox dropBox = new DropBox();
        DropBoxItem dropBoxItem = new DropBoxItem();
        dropBoxItem.setName("DUMMY.XLSX");
        dropBox.getItems().add(dropBoxItem);
        dropBoxItem = new DropBoxItem();
        dropBoxItem.setName("DUMMY.pdf");
        dropBox.getItems().add(dropBoxItem);
        assertTrue(DropBoxFileHelper.dropBoxContainsDecryptedFiles(dropBox));
    }

    @Test
    void dropBoxContainsDecryptedFilesReturnsTrueForMixedFiles() {
        DropBox dropBox = new DropBox();
        DropBoxItem dropBoxItem = new DropBoxItem();
        dropBoxItem.setName("DUMMY.XLSX");
        dropBox.getItems().add(dropBoxItem);
        dropBoxItem = new DropBoxItem();
        dropBoxItem.setName("DUMMY.gpg");
        dropBox.getItems().add(dropBoxItem);
        assertTrue(DropBoxFileHelper.dropBoxContainsDecryptedFiles(dropBox));
    }

    @Test
    void dropBoxContainsDecryptedFilesReturnsFalseForSingleEncryptedFile() {
        DropBox dropBox = new DropBox();
        DropBoxItem dropBoxItem = new DropBoxItem();
        dropBoxItem.setName("DUMMY.zip");
        dropBox.getItems().add(dropBoxItem);
        assertFalse(DropBoxFileHelper.dropBoxContainsDecryptedFiles(dropBox));
    }

    @Test
    void dropBoxContainsDecryptedFilesReturnsFalseForMultipleEncryptedFiles() {
        DropBox dropBox = new DropBox();
        DropBoxItem dropBoxItem = new DropBoxItem();
        dropBoxItem.setName("DUMMY.zip");
        dropBox.getItems().add(dropBoxItem);
        dropBoxItem = new DropBoxItem();
        dropBoxItem.setName("DUMMY.gpg");
        dropBox.getItems().add(dropBoxItem);
        assertFalse(DropBoxFileHelper.dropBoxContainsDecryptedFiles(dropBox));
    }
}