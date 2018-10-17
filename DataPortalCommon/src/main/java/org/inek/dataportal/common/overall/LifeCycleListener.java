package org.inek.dataportal.common.overall;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.*;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author mmueller
 */
public class LifeCycleListener implements PhaseListener {

    private static final String STYLE = " required ";
    public static final Logger LOGGER = Logger.getLogger(LifeCycleListener.class.getName());

    @Override
    public void beforePhase(PhaseEvent event) {
        try {
            UIViewRoot root = event.getFacesContext().getViewRoot();
            if (root != null && event.getPhaseId() == PhaseId.RENDER_RESPONSE) {
                markRequired(root, root);
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
        }
    }

    @Override
    public void afterPhase(PhaseEvent event) {
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }

    private void markRequired(UIComponent root, UIComponent parent) {
        for (UIComponent child : parent.getChildren()) {
            if (child instanceof HtmlOutputLabel) {
                HtmlOutputLabel label = (HtmlOutputLabel) child;
                String targetId = label.getNamingContainer().getClientId() + ":" + label.getFor();
                UIComponent target = parent.findComponent(targetId);
                if (target instanceof UIInput) {
                    UIInput input = (UIInput) target;
                    markRequired(label, input.isRequired());
                    // markRequired(input, input.isRequired());  // do not handle here, because there might be elements without label
                }
            } else if (child instanceof UIInput) {
                // there might be some required elements without label, thus we handle them here
                UIInput input = (UIInput) child;
                markRequired(input, input.isRequired());

            } else {
                markRequired(root, child);
            }
        }
        UIComponent composition = parent.getFacet("javax.faces.component.COMPOSITE_FACET_NAME");
        if (composition != null) {
// sadly it seems, that the composition contains an "empty" template of the component only.
// thus, marking required dos not work properly 
//            markRequired(root, composition);
        }
    }

    @SuppressWarnings("CyclomaticComplexity")
    private void markRequired(UIComponent field, boolean required) {
        // This methods looks a bit ugly, doing the same for different components.
        // Sadly, these components do not have the styleClass property in a parent class. 
        // Thus polymorphism is not available :(
        if (field instanceof HtmlOutputLabel) {
            HtmlOutputLabel element = (HtmlOutputLabel) field;
            String oldStyle = element.getStyleClass();
            if (!required && (oldStyle == null || oldStyle.isEmpty())) {
                return;
            }
            element.setStyleClass(updateStyle(oldStyle, required));
        } else if (field instanceof HtmlInputText) {
            HtmlInputText element = (HtmlInputText) field;
            String oldStyle = element.getStyleClass();
            if (!required && (oldStyle == null || oldStyle.isEmpty())) {
                return;
            }
            element.setStyleClass(updateStyle(oldStyle, required));
        } else if (field instanceof HtmlInputTextarea) {
            HtmlInputTextarea element = (HtmlInputTextarea) field;
            String oldStyle = element.getStyleClass();
            if (!required && (oldStyle == null || oldStyle.isEmpty())) {
                return;
            }
            element.setStyleClass(updateStyle(oldStyle, required));
        } else if (field instanceof HtmlInputSecret) {
            HtmlInputSecret element = (HtmlInputSecret) field;
            String oldStyle = element.getStyleClass();
            if (!required && (oldStyle == null || oldStyle.isEmpty())) {
                return;
            }
            element.setStyleClass(updateStyle(oldStyle, required));
        } else if (field instanceof HtmlSelectOneMenu) {
            HtmlSelectOneMenu element = (HtmlSelectOneMenu) field;
            String oldStyle = element.getStyleClass();
            if (!required && (oldStyle == null || oldStyle.isEmpty())) {
                return;
            }
            element.setStyleClass(updateStyle(oldStyle, required));
        } else if (field instanceof HtmlSelectOneRadio) {
            HtmlSelectOneRadio element = (HtmlSelectOneRadio) field;
            String oldStyle = element.getStyleClass();
            if (!required && (oldStyle == null || oldStyle.isEmpty())) {
                return;
            }
            element.setStyleClass(updateStyle(oldStyle, required));
        }
    }

    private String updateStyle(String oldStyle, boolean required) {
        if (oldStyle == null) {
            oldStyle = "";
        }
        String newStyle = oldStyle.replace(STYLE, "") + (required ? STYLE : "");
        return newStyle;
    }

}
