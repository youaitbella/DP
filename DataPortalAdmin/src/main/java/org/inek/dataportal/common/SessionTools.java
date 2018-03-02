package org.inek.dataportal.common;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
import org.inek.dataportal.entities.ContactRole;
import org.inek.dataportal.common.data.common.CustomerType;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.facades.ContactRoleFacade;
import org.inek.dataportal.facades.CustomerFacade;
import org.inek.dataportal.facades.CustomerTypeFacade;
import org.inek.dataportal.facades.InfoDataFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.faceletvalidators.EmailValidator;

/**
 *
 * @author muellermi
 */
@Named
@SessionScoped
public class SessionTools implements Serializable {

    private List<SelectItem> _roleItems;
    private List<SelectItem> _customerTypeItems;
    private List<Integer> _hospitals;
    private int _result;
    private Map<String, String> _pages;
    @Inject private transient ContactRoleFacade _contactRoleFacade;
    @Inject private transient CustomerTypeFacade _typeFacade;
    @Inject private transient InfoDataFacade _trashMailfacade;
    @Inject private transient CustomerFacade _customerFacade;

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

    private String _language = "de";

    public String getLanguage() {
        return _language;
    }

    public void putLanguage(String language) {
        _language = language;
        Locale newLocale = new Locale(language);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(newLocale);
    }

    public List<SelectItem> getContactRoleItems() {
        if (_roleItems == null) {
            _roleItems = new ArrayList<>();
            List<ContactRole> roles = _contactRoleFacade.findAllExtern();
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
    
    public SelectItem[] getPerinatalcentreItems() {
        SelectItem[] items = new SelectItem[2];
        items[0] = new SelectItem("1", "1");
        items[1] = new SelectItem("2", "2");
        return items;
    }
    
    public String formatDate(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public boolean isValidNonTrashEmail(String address) {
        if (!EmailValidator.isValidEmail(address)) {
            return false;
        }
        String domain = address.substring(address.indexOf("@") + 1);
        return !_trashMailfacade.isTrashMailDomain(domain);
    }

    public void checkIk(FacesContext context, UIComponent component, Object value) {
        if (value == null || value.toString().isEmpty()) {
            return;
        }
        if (!_customerFacade.isValidIK("" + value)) {
            String msg = Utils.getMessage("errIK");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

}
