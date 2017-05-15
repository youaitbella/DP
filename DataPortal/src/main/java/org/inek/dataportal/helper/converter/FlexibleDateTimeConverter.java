package org.inek.dataportal.helper.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.FacesConverter;

/**
 * The {@code FlexibleDateTimeConverter} is used in JSPs to flexibly convert
 * date and time values.  The class should circumvent the deficit in flexiblity
 * accepting input of dates in short or medium formats.
 * @author Stefan Wich, InEK GmbH
 */
@FacesConverter(value="FlexibleDateTimeConverter")
public class FlexibleDateTimeConverter extends DateTimeConverter {

    private SimpleDateFormat dfShort;   // mm 2010-01-08 DateFormat replaced by SimpleDateFormat (medium had displayed name of month, is now allways number)
    private SimpleDateFormat dfMedium;

    public FlexibleDateTimeConverter() {
        super();
        setDateFormattersLocale();
        setTimeZone(null);
        setDateStyle("short");
    }

    @Override
    public Object getAsObject(FacesContext facesCtxt, UIComponent uiComponent, String value) {
        if (facesCtxt == null || uiComponent == null) {
            throw new NullPointerException("FacesContext and Component are necessary to perform transform.");
        }
        Date d = null;
        if (value != null && !value.isEmpty()) {
            setLocale(facesCtxt.getViewRoot().getLocale());
            try {
                d = dfShort.parse(value.trim());
            } catch (ParseException anon) {
                try {
                    d = dfMedium.parse(value.trim());
                } catch (ParseException anon2) {
                    Logger.getLogger(FlexibleDateTimeConverter.class.getName()).log(Level.INFO, 
                            "Caught 2nd ParseException during parsing of {0} in <MEDIUM> and Locale {1}.", 
                            new Object[]{value, getLocale().getDisplayName()});
                }
            } catch (Exception ex) {
                Logger.getLogger(FlexibleDateTimeConverter.class.getName()).log(Level.WARNING, "Caught other exception during parsing of " + value, ex);
            }
            Calendar c = Calendar.getInstance();
            if (d != null) {
                c.setTime(d);
                if (c.get(Calendar.YEAR) < 100) {
                    c.roll(Calendar.YEAR, 2000);
                    if (c.after(Calendar.getInstance())) {//future?
                        c.roll(Calendar.YEAR, -100);
                    }
                }
                d = c.getTime();
            }
            if (d == null) {
                ResourceBundle resourceBundle = facesCtxt.getApplication().getResourceBundle(facesCtxt, "msg");
                throw new ConverterException(new FacesMessage(resourceBundle.getString("msgWrongDate")));
            }
        }
        return d;
    }

    @Override
    public String getAsString(FacesContext facesCtxt, UIComponent arg1, Object arg2) {
        setLocale(facesCtxt.getViewRoot().getLocale());
        return dfMedium.format(arg2);
    }

    @Override
    public void setLocale(Locale locale) {
        if (!locale.equals(getLocale())) {
            super.setLocale(locale);
            setDateFormattersLocale();
        }
    }

    private void setDateFormattersLocale() {
        Locale l = getLocale();
        dfShort = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.SHORT, l);
        dfMedium = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.MEDIUM, l);
        dfMedium.applyPattern(dfShort.toPattern().replace("yy", "yyyy"));
    }
}
