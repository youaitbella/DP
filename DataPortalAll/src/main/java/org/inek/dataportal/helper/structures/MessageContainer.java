package org.inek.dataportal.helper.structures;

import java.util.Objects;

/**
 * Container to hold error message and position (page and element) where the error occurs.
 * 
 * @author muellermi
 */
public class MessageContainer {

    private String _message;
    private String _topic;
    private String _elementId;

    public MessageContainer() {
        this("", "", "");
    }

    public MessageContainer(final String value1, final String value2, final String value3) {
        _message = value1;
        _topic = value2;
        _elementId = value3;
    }

    public String getMessage() {
        return _message;
    }

    public void setMessage(String message) {
        _message = message;
    }

    public String getTopic() {
        return _topic;
    }

    public void setTopic(String topic) {
        _topic = topic;
    }

    public String getElementId() {
        return _elementId;
    }

    public void setElementId(String elementId) {
        _elementId = elementId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(_message);
        hash = 37 * hash + Objects.hashCode(_topic);
        hash = 37 * hash + Objects.hashCode(_elementId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MessageContainer other = (MessageContainer) obj;
        if (!Objects.equals(_message, other._message)) {
            return false;
        }
        if (!Objects.equals(_topic, other._topic)) {
            return false;
        }
        if (!Objects.equals(_elementId, other._elementId)) {
            return false;
        }
        return true;
    }

    public boolean containsMessage() {
        return _message != null && !_message.isEmpty();
    }
    
}
