package org.inek.dataportal.admin.system;

import org.inek.dataportal.common.scope.FeatureScopedContextHolder;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author muellermi
 */
// todo: This session counter only counts within the local app. 
// After creating independent apps, we need to collect data from every app and send it to a central place
// possible solution:
// table: server, app, count
// reset on app start
// increment and decrement here
@WebListener
public class SessionCounter implements HttpSessionListener {

    private static final Logger LOGGER = Logger.getLogger("SessionCounter");
    @SuppressWarnings("ConstantName")
    private static final AtomicInteger _count = new AtomicInteger(0);

    public static int getCount() {
        return _count.get();
    }

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        LOGGER.log(Level.INFO, "Session created");
        _count.incrementAndGet();
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        @SuppressWarnings("unchecked") Map<String, FeatureScopedContextHolder.FeatureScopedInstance> map
                = (Map<String, FeatureScopedContextHolder.FeatureScopedInstance>) session.getAttribute("FeatureScoped");
        FeatureScopedContextHolder.Instance.destroyAllBeansExcept(map, "");
        LOGGER.log(Level.INFO, "Session destroyed");
        _count.decrementAndGet();
    }

}
