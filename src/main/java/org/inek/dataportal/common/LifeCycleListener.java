package org.inek.dataportal.common;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.*;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 *
 * @author mmueller
 */
public class LifeCycleListener implements PhaseListener {

    private static final String STYLE = " required ";

    @Override
    public void beforePhase(PhaseEvent event) {
        UIViewRoot root = event.getFacesContext().getViewRoot();
        if (root != null && event.getPhaseId() == PhaseId.RENDER_RESPONSE) {
            markRequired(root, root);

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
                UIComponent target = root.findComponent(targetId);
                if (target instanceof UIInput) {
                    UIInput input = (UIInput) target;
                    markRequired(label, input.isRequired());
// *** see comment below ***  markRequired(input, input.isRequired());
                    String title = label.getValue().toString().trim();
                    int colonPos = title.lastIndexOf(":");
                    if (colonPos == title.length() - 1){
                        title = title.substring(0, colonPos);
                    }
                    setTitle(input, title);
                }
            }
            if (child instanceof UIInput) {
                // there might be some required elements without label, thus we handle them here
                UIInput input = (UIInput) child;
                markRequired(input, input.isRequired());
                
            }
            markRequired(root, child);
        }
    }

    private void markRequired(UIComponent field, boolean required) {

        if (field instanceof HtmlOutputLabel) {
            HtmlOutputLabel element = (HtmlOutputLabel) field;
            element.setStyleClass(updateStyle(element.getStyleClass(), required));
        } else if (field instanceof HtmlInputText) {
            HtmlInputText element = (HtmlInputText) field;
            element.setStyleClass(updateStyle(element.getStyleClass(), required));
        } else if (field instanceof HtmlInputTextarea) {
            HtmlInputTextarea element = (HtmlInputTextarea) field;
            element.setStyleClass(updateStyle(element.getStyleClass(), required));
        } else if (field instanceof HtmlInputSecret) {
            HtmlInputSecret element = (HtmlInputSecret) field;
            element.setStyleClass(updateStyle(element.getStyleClass(), required));
        } else if (field instanceof HtmlSelectOneMenu) {
            HtmlSelectOneMenu element = (HtmlSelectOneMenu) field;
            element.setStyleClass(updateStyle(element.getStyleClass(), required));
        } else if (field instanceof HtmlSelectOneRadio) {
            HtmlSelectOneRadio element = (HtmlSelectOneRadio) field;
            element.setStyleClass(updateStyle(element.getStyleClass(), required));
        }
    }

    private void setTitle(UIComponent field, String title) {

        if (field instanceof HtmlInputText) {
            HtmlInputText element = (HtmlInputText) field;
            if (element.getTitle() == null){
                element.setTitle(title);
            }
        } else if (field instanceof HtmlInputTextarea) {
            HtmlInputTextarea element = (HtmlInputTextarea) field;
            if (element.getTitle() == null){
                element.setTitle(title);
            }
        } else if (field instanceof HtmlInputSecret) {
            HtmlInputSecret element = (HtmlInputSecret) field;
            if (element.getTitle() == null){
                element.setTitle(title);
            }
        } else if (field instanceof HtmlSelectOneMenu) {
            HtmlSelectOneMenu element = (HtmlSelectOneMenu) field;
            if (element.getTitle() == null){
                element.setTitle(title);
            }
        } else if (field instanceof HtmlSelectOneRadio) {
            HtmlSelectOneRadio element = (HtmlSelectOneRadio) field;
            if (element.getTitle() == null){
                element.setTitle(title);
            }
        }
    }

    private String updateStyle(String oldStyle, boolean required) {
        if (oldStyle == null) {
            oldStyle = "";
        }
        String newStyle = oldStyle.replace(STYLE, "") + (required ? STYLE : "");
        return newStyle;
    }

    private void markLabels4Required(UIComponent root, UIComponent parent) {
        //todo: replace by css approach
        String marker = " *";
        for (UIComponent child : parent.getChildren()) {
            if (child instanceof HtmlOutputLabel) {
                HtmlOutputLabel label = (HtmlOutputLabel) child;
                String targetId = label.getNamingContainer().getClientId() + ":" + label.getFor();
                UIComponent target = root.findComponent(targetId);
                if (target instanceof UIInput) {
                    UIInput input = (UIInput) target;
                    String labelText = label.getValue().toString();
                    if (input.isRequired() && !labelText.endsWith(marker)) {
                        label.setValue(labelText + marker);
                    }
                    if (!input.isRequired() && labelText.endsWith(marker)) {
                        label.setValue(labelText.substring(0, labelText.length() - marker.length()));
                    }
                }
            }
            markLabels4Required(root, child);
        }
    }
}
