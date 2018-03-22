package org.inek.dataportal.feature.modelintention;

import java.util.Iterator;
import java.util.List;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import org.inek.dataportal.feature.modelintention.entities.ModelIntention;

public abstract class DynamicTable<T> {

    public DynamicTable(ModelIntention modelIntention, List<T> list) {
        _modelIntention = modelIntention;
        _list = list;
    }

    private final List<T> _list;

    public List<T> getList() {
        return _list;
    }

    private final ModelIntention _modelIntention;

    public ModelIntention getModelIntention() {
        return _modelIntention;
    }

    public boolean ensureEmptyEntry() {
        if (needEmptyCode()) {
            addNewEntry();
            return true;
        }
        return false;
    }

    private boolean needEmptyCode() {
        if (_list.isEmpty()) {
            return true;
        }
        T entry = _list.get(_list.size() - 1);
        return !isEmptyEntry(entry);
    }

    public void addEntry(T entry) {
        _list.add(entry);
    }

    protected abstract void addNewEntry();

    public void removeEmptyEntries() {
        for (Iterator<T> itr = _list.iterator(); itr.hasNext();) {
            T entry = itr.next();
            if (isEmptyEntry(entry)) {
                itr.remove();
            }
        }
    }

    protected abstract boolean isEmptyEntry(T entry);

    public String deleteEntry(T entry) {
        _list.remove(entry);
        //ensureEmptyEntry();
        return "";
    }

    private String _script = "";

    public void checkDynamicListener(AjaxBehaviorEvent event) {
        if (false && ensureEmptyEntry() && event.getSource() instanceof HtmlInputText) {
            HtmlInputText t = (HtmlInputText) event.getSource();
            if (t.getValue().toString().length() <= 0) {
                return;
            }
            String currentId = t.getClientId();
            _script = "setCaretPosition('" + currentId + "', " + t.getValue().toString().length() + ");";
        } else {
            _script = "";
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    public void setMessage(String msg) {
        _script = "alert('" + msg + "');";
    }

    public String getScript() {
        String script = _script;
        _script = "";
        return script;
    }
}
