package org.inek.dataportal.common.data.account.iface;

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
