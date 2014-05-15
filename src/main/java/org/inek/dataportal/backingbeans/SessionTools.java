package org.inek.dataportal.backingbeans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.datatype.XMLGregorianCalendar;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.ContactRole;
import org.inek.dataportal.entities.CustomerType;
import org.inek.dataportal.entities.DropBoxType;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.ContactRoleFacade;
import org.inek.dataportal.facades.CustomerTypeFacade;
import org.inek.dataportal.facades.DropBoxTypeFacade;
import org.inek.dataportal.helper.Utils;

/**
 *
 * @author muellermi
 */
@Named
@SessionScoped
public class SessionTools implements Serializable {
    @Inject private SessionController _sesssionController;
    private List<SelectItem> _roleItems;
    private List<SelectItem> _customerTypeItems;
    private List<Integer> _hospitals;
    private int _result;
    private Map<String, String> _pages;
    @Inject private ContactRoleFacade _contactRoleFacade;
    @Inject private CustomerTypeFacade _typeFacade;
    @Inject private DropBoxTypeFacade _dropBoxTypeFacade;
    
    
    
    public int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public Map<String, String> getPages() {
        if (_pages == null) {
            _pages = new HashMap<>();
            for (Pages page : Pages.values()) {
                _pages.put(page.name(), page.URL());
            }
        }
        return _pages;
    }

    public List<SelectItem> getContactRoleItems() {
        if (_roleItems == null) {
            _roleItems = new ArrayList<>();
            List<ContactRole> roles = _contactRoleFacade.findAll();
            _roleItems.add(new SelectItem(null, Utils.getMessage("lblChooseEntry")));
            for (ContactRole role : roles) {
                _roleItems.add(new SelectItem(role.getId(), role.getText()));
            }
        }
        return _roleItems;
    }

    public List<SelectItem> getCustomerTypeItems() {
        if (_customerTypeItems == null) {
            _customerTypeItems = new ArrayList<>();
            _hospitals = new ArrayList<>();
            List<CustomerType> types = _typeFacade.findAll();
            _customerTypeItems.add(new SelectItem("", Utils.getMessage("lblChooseEntry")));
            for (CustomerType type : types) {
                _customerTypeItems.add(new SelectItem(type.getId(), type.getText()));
                if (type.isHospital()) {
                    _hospitals.add(type.getId());
                }
            }
        }
        return _customerTypeItems;
    }

    private void initDropBoxTypes() {
        if (_dropBoxTypes == null) {
            _dropBoxTypes = new HashMap<>();
            for (DropBoxType type : _dropBoxTypeFacade.findAll()) {
                _dropBoxTypes.put(type.getId(), type);
            }
        }
    }
    private Map<Integer, DropBoxType> _dropBoxTypes;

    public DropBoxType getDropBoxType(int id) {
        initDropBoxTypes();
        return _dropBoxTypes.get(id);
    }

    public List<SelectItem> getDropBoxTypeItems() {
        initDropBoxTypes();
        List<SelectItem> dropboxTypeItems;
        dropboxTypeItems = new ArrayList<>();
        dropboxTypeItems.add(new SelectItem(null, Utils.getMessage("lblChooseEntry")));
        for (DropBoxType type : _dropBoxTypes.values()) {
            dropboxTypeItems.add(new SelectItem(type.getId(), type.getName()));
        }
        return dropboxTypeItems;
    }

    public boolean isHospital(Integer typeId) {
        if (_customerTypeItems == null) {
            getCustomerTypeItems();
        }
        return _hospitals.contains(typeId);
    }

    public String getSecurityQuestion() {
        String msg = Utils.getMessage("lblMathQuestion");
        int number1 = (int) Math.round(10 + 80 * Math.random());
        int number2 = (int) Math.round(10 * Math.random());
        _result = number1 + number2;
        String challenge = number1 + " plus " + number2;
        return String.format(msg, challenge);
    }

    public void checkSecurityQuestion(FacesContext context, UIComponent component, Object value) {
        int result;
        try {
            result = Integer.parseInt("" + value);
        } catch (Exception e) {
            result = Integer.MIN_VALUE;
        }
        if (result != _result) {
            HtmlInputText field = (HtmlInputText) component;
            field.setSubmittedValue("");
            String msg = Utils.getMessage("msgWrongResult");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    public SelectItem[] getGenderItems() {
        SelectItem[] items = new SelectItem[3];
        items[0] = new SelectItem("", "");
        items[1] = new SelectItem("1", Utils.getMessage("salutationFemale"));
        items[2] = new SelectItem("2", Utils.getMessage("salutationMale"));
        return items;
    }
    
    public String formatDate(XMLGregorianCalendar calendar, String format) {
        return new SimpleDateFormat(format).format(calendar.toGregorianCalendar().getTime());
    }
    
    public boolean isPeppProposalDisabled(){
        return false;
    }
    
    public boolean isNubProposalDisabled(){
        return false;
    }
    
    public boolean isModelIntentionDisabled() {
        return false;
    }    
}
