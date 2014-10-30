package org.inek.dataportal.system;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.inek.dataportal.helper.scope.FeatureScopedContextHolder;

/**
 *
 * @author muellermi
 */
@WebListener
public class SessionCounter implements HttpSessionListener {

    private static final Logger _logger = Logger.getLogger("SessionCounter");
    private static final AtomicInteger _count = new AtomicInteger(0);

    public static int getCount() {
        return _count.get();
    }

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        _logger.log(Level.INFO, "Session created");
        _count.incrementAndGet();
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        Map<String, FeatureScopedContextHolder.FeatureScopedInstance> map
                = (Map<String, FeatureScopedContextHolder.FeatureScopedInstance>) session.getAttribute("FeatureScoped");
        FeatureScopedContextHolder.Instance.destroyAllBeansExcept(map, "");
        _logger.log(Level.INFO, "Session destroyed");
        _count.decrementAndGet();
    }

}
