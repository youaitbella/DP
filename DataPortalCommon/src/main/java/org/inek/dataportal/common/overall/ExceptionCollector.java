package org.inek.dataportal.common.overall;

import static org.inek.dataportal.api.helper.PortalConstants.END_PARAGRAPH;
import static org.inek.dataportal.api.helper.PortalConstants.MESSAGE_SEPERATOR;

public class ExceptionCollector {

    private ExceptionCollector() {
    }

    public static StringBuilder collect(String head, Throwable exception) {
        return new ExceptionCollector().collectException(head, exception);
    }

    private StringBuilder collectException(String head, Throwable exception) {
        StringBuilder collector = new StringBuilder();
        collectException(collector, head, exception, 0);
        return collector;
    }

    private void collectException(StringBuilder collector, String head, Throwable exception, int level) {
        if (collector.length() > 0) {
            collector.append(MESSAGE_SEPERATOR);
        }
        collector.append("Level: ").append(level).append(END_PARAGRAPH);
        collector.append(head).append(END_PARAGRAPH);
        collector.append(exception.getMessage()).append(END_PARAGRAPH);
        for (StackTraceElement element : exception.getStackTrace()) {
            collector.append(element.toString()).append("\r\n");
        }
        Throwable cause = exception.getCause();
        if (cause != null && level < 9) {
            collectException(collector, head, cause, level + 1);
        }
    }

}
