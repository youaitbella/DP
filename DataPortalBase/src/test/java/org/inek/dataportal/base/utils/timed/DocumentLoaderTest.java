package org.inek.dataportal.base.utils.timed;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author muellermi
 */
public class DocumentLoaderTest {

    public DocumentLoaderTest() {
    }

    @Test
    public void moveFileSuccess() throws IOException {
        File homeDir = getUserHomeDir();

        File file = new File(homeDir, UUID.randomUUID().toString());
        file.createNewFile();
        assertTrue("File created", file.exists());

        String targetRelativePath = UUID.randomUUID().toString();
        File targetDir = new File(file.getParent(), targetRelativePath);
        File targetFile = new File(targetDir, file.getName());


        boolean result = DocumentLoader.moveFile(targetRelativePath, file);
        assertTrue(result);
        assertTrue("File moved", targetFile.exists());
        
        
        assertFalse("File removed",file.exists());
        
        targetFile.delete();
        targetDir.delete();
    }

    @Test
    public void moveFileFails() throws IOException {
        File homeDir = getUserHomeDir();

        File file = new File(homeDir, UUID.randomUUID().toString());
        file.createNewFile();
        assertTrue("File created", file.exists());

        String targetRelativePath = UUID.randomUUID().toString();
        File targetDir = new File(file.getParent(), targetRelativePath);
        targetDir.mkdir();

        File targetFile = new File(targetDir, file.getName());
        targetFile.createNewFile();
        assertTrue("targetFile created", targetFile.exists());

        boolean result = DocumentLoader.moveFile(targetRelativePath, file);
        assertFalse(result);
        assertTrue("File moved", targetFile.exists());
        
        
        assertFalse("File removed",file.exists());
        
        targetFile.delete();
        targetDir.delete();
    }

    public File getUserHomeDir() {
        File dir = new File(System.getProperty("user.home"));
        return dir;
    }

}
