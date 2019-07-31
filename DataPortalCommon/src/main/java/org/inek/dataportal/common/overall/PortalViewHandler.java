package org.inek.dataportal.common.overall;

import javax.faces.application.ViewExpiredException;
import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.util.logging.Logger;

// add this to faces-config:
// <view-handler>org.inek.dataportal.common.overall.PortalViewHandler</view-handler>
public class PortalViewHandler extends ViewHandlerWrapper {
    private ViewHandler wrapped;
    private static final Logger LOGGER = Logger.getLogger(PortalViewHandler.class.getName());

    public PortalViewHandler(ViewHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public UIViewRoot restoreView(FacesContext context, String viewId) {
        try {
            return wrapped.restoreView(context, viewId);
        } catch (ViewExpiredException ex) {
            LOGGER.info("PortalViewHandler ViewExpiredException: " + viewId + wrapped.getClass());
            return wrapped.createView(context, viewId);
        } catch (Exception ex) {
            LOGGER.info("PortalViewHandler ViewExpiredException: " + viewId + wrapped.getClass());
            return wrapped.createView(context, viewId);
        }
    }

    @Override
    public ViewHandler getWrapped() {
        return wrapped;
    }

}





