package org.inek.dataportal.entities;

/**
 *
 * @author muellermi
 */
public interface Document {

    byte[] getContent();

    void setContent(byte[] content);

    String getName();

    void setName(String name);

}
