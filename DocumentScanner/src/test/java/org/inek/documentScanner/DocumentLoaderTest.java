package org.inek.documentScanner;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.inek.documentScanner.business.DocumentLoader;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
        assertTrue(file.exists(), "File created");

        String targetRelativePath = UUID.randomUUID().toString();
        File targetDir = new File(file.getParent(), targetRelativePath);
        File targetFile = new File(targetDir, file.getName());


        boolean result = DocumentLoader.moveFile(targetRelativePath, file);
        assertTrue(result);
        assertTrue(targetFile.exists(), "File moved");
        
        
        assertFalse(file.exists(), "File removed");
        
        targetFile.delete();
        targetDir.delete();
    }

    @Test
    public void moveFileFails() throws IOException {
        File homeDir = getUserHomeDir();

        File file = new File(homeDir, UUID.randomUUID().toString());
        file.createNewFile();
        assertTrue(file.exists(), "File created");

        String targetRelativePath = UUID.randomUUID().toString();
        File targetDir = new File(file.getParent(), targetRelativePath);
        targetDir.mkdir();

        File targetFile = new File(targetDir, file.getName());
        targetFile.createNewFile();
        assertTrue(targetFile.exists(), "targetFile created");

        boolean result = DocumentLoader.moveFile(targetRelativePath, file);
        if (isWindows()){
        assertFalse(result);
        }else{
        assertTrue(result);
        }
        assertTrue(targetFile.exists(), "File moved");
        
        
        assertFalse(file.exists(), "File removed");
        
        targetFile.delete();
        targetDir.delete();
    }

    public File getUserHomeDir() {
        File dir = new File(System.getProperty("user.home"));
        return dir;
    }

    private boolean isWindows() {
        String os = System.getProperty("os.name");
        return os.toLowerCase().startsWith("windows");
    }

}
