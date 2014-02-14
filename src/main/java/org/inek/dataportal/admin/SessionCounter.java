package org.inek.dataportal.admin;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 *
 * @author muellermi
 */
@WebListener
public class SessionCounter implements HttpSessionListener {

    private static int _count;

    public static int getCount() {
        return _count;
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        _count++;
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        _count--;
    }

}