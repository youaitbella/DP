package org.inek.dataportal.feature.modelintention;

import java.util.Iterator;
import java.util.List;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import org.inek.dataportal.entities.modelintention.ModelIntention;

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

    abstract protected void addNewEntry();
    
    public void removeEmptyEntries() {
        for (Iterator<T> itr = _list.iterator(); itr.hasNext();) {
            T entry = itr.next();
            if (isEmptyEntry(entry)) {
                itr.remove();
            }
        }
    }

    abstract protected boolean isEmptyEntry(T entry);
    
    public String deleteEntry(T entry) {
        _list.remove(entry);
        ensureEmptyEntry();
        return "";
    }
   
    
    String _script = "";

    public void checkDynamicListener(AjaxBehaviorEvent event) {
        HtmlInputText t = (HtmlInputText) event.getSource();
        String currentId = t.getClientId();
        if (ensureEmptyEntry()) {
            _script = "setCaretPosition('" + currentId + "', -1);";
        } else {
            _script = "";
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    public String getScript() {
        String script = _script;
        _script = "";
        return script;
    }


}
