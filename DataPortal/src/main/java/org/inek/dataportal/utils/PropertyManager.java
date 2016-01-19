package org.inek.dataportal.utils;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author muellermi
 */
public enum PropertyManager {

    INSTANCE;
    private Properties _prop;
    private static final Logger _logger = Logger.getLogger("PropertyManager");

    private PropertyManager() {
        initProperties();
    }


    private void initProperties() {
        readProperties();

        boolean isChanged = false;
        for (PropertyKey key : PropertyKey.values()){
            isChanged |= ensureProperty(key.name(), key.getDefault());
        }

        if (isChanged) {
            storeProperties();
        }
    }

    public File getUserHomeDir(){
        File dir = new File (System.getProperty("user.home"), "DataPortal");
        if (!dir.exists() && !dir.mkdirs()){
            throw new RuntimeException("Can't create directory 'DataPortal' in user's home directory");
        }
        return dir;
    }
    
    private File getPropertyFile() {
        return new File(getUserHomeDir(), "DataPortal.properties");
        //return new File ( System.getProperty("user.dir"), "RequestServices.properties");
    }

    private void readProperties() {
        _prop = new Properties();
        try {
            _prop.load(new FileInputStream(getPropertyFile()));
        } catch (FileNotFoundException e) {
            // at first access, this file is not present. 
            // ignore exception
        } catch (IOException e) {
            _logger.log(Level.WARNING, "Unexpected IOException whilst reading properties. Will use defaults.", e);
        }
    }

    /**
     * stores propertiy file in the user dir
     */
    public void storeProperties() {
        try {
            // store. this will create property file if non-existant
            _prop.store(new FileOutputStream(getPropertyFile()), null);
        } catch (IOException e) {
            _logger.log(Level.WARNING, "Unexpected IOException whilst writing properties. Will use defaults.", e);
        }
    }

    private boolean ensureProperty(String name, String defaultValue) {
        boolean isNew = false;
        if (!_prop.containsKey(name)) {
            isNew = true;
            _prop.setProperty(name, defaultValue);
        }
        return isNew;
    }

    public String getProperty(PropertyKey propKey) {
        return _prop.getProperty(propKey.name());
    }

   /**
     * returns property as int if the value cannot be converted to int, an
     * unchecked NumberFormatException is thrown
     *
     * @param propKey
     * @return
     */
    public int getPropertyAsInt(PropertyKey propKey) {
        return Integer.parseInt(_prop.getProperty(propKey.name()));
    }

    public boolean  getPropertyAsBool(PropertyKey propKey) {
        return _prop.getProperty(propKey.name()).equals("true");
    }

    public void setProperty(PropertyKey propKey, int value) {
        setProperty(propKey, "" + value);
    }

    public void setProperty(PropertyKey propKey, boolean  value) {
        setProperty(propKey, value ? "true" : "false");
    }

    public void setProperty(PropertyKey propKey, String value) {
        _prop.setProperty(propKey.name(), value);
    }
}
