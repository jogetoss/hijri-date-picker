package org.joget.marketplace;

import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.*;
import org.joget.apps.form.service.FormUtil;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HijriDatePicker extends Element implements FormBuilderPaletteElement {

    private static final String DEFAULT_DISPLAY_FORMAT = "dd-mm-yy";

    @Override
    public String renderTemplate(FormData formData, Map dataModel) {
        String template = "hijriDatePicker.ftl";
        String value = FormUtil.getElementPropertyValue(this, formData);
        String displayFormat = getPropertyString("format");

        if (value != null && !value.isEmpty()) {
            if (displayFormat == null || displayFormat.isEmpty()) {
                displayFormat = DEFAULT_DISPLAY_FORMAT;
            }

        }

        String hijriDisplayFormat = "iDD-iMM-iYYYY";
        if (displayFormat != null && !displayFormat.isEmpty()) {
            hijriDisplayFormat = hijriDisplayFormat(displayFormat);
        }
        
        dataModel.put("value", value);
        dataModel.put("displayFormat", hijriDisplayFormat);
        String html = FormUtil.generateElementHtml(this, formData, template, dataModel);
        return html;
    }

    @Override
    public FormRowSet formatData(FormData formData) {
        FormRowSet rowSet = null;
        String id = getPropertyString(FormUtil.PROPERTY_ID);
        if (id != null) {
            String value = FormUtil.getElementPropertyValue(this, formData);
            //String binderValue = formData.getLoadBinderDataProperty(this, id);
            if (!FormUtil.isReadonly(this, formData)) {
                if (value != null && !value.isEmpty()) {
                    FormRow result = new FormRow();
                    result.setProperty(id, value);
                    rowSet = new FormRowSet();
                    rowSet.add(result);
                }
            }
        }
        return rowSet;
    }
    
    public static boolean isValidTimestamp(String str) {
        if (str == null) {
            return false;
        }
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private long processDate(String value, String format) {
        String javaDateFormat = getJavaDateFormat(format);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(javaDateFormat);
        TemporalAccessor ta = formatter.parse(value);
        int day = ta.get(ChronoField.DAY_OF_MONTH);
        int month = ta.get(ChronoField.MONTH_OF_YEAR);
        int year = ta.get(ChronoField.YEAR_OF_ERA);
        Calendar cal = new UmmalquraCalendar(year, month - 1, day);
        return cal.getTime().getTime();
    }

    private String hijriDisplayFormat(String displayFormat) {
        String hijriDisplayFormat = getJavaDateFormat(displayFormat);
        hijriDisplayFormat = hijriDisplayFormat.toUpperCase();
        hijriDisplayFormat = fixHijriFormat(hijriDisplayFormat, "D+");
        hijriDisplayFormat = fixHijriFormat(hijriDisplayFormat, "M+");
        hijriDisplayFormat = fixHijriFormat(hijriDisplayFormat, "Y+");
        return hijriDisplayFormat;
    }

    private String fixHijriFormat(String format, String regEx) {
        String matchedGroup = "";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(format);
        if (matcher.find()) {
            matchedGroup = matcher.group();
        }
        return format.replaceAll(regEx, "i" + matchedGroup);
    }

    protected static String getJavaDateFormat(String format) {
        if (format.contains("DD")) {
            format = format.replaceAll("DD", "EEEE");
        } else {
            format = format.replaceAll("D", "EEE");
        }

        if (format.contains("MM")) {
            format = format.replaceAll("MM", "MMMMM");
        } else {
            format = format.replaceAll("M", "MMM");
        }

        if (format.contains("mm")) {
            format = format.replaceAll("mm", "MM");
        } else {
            format = format.replaceAll("m", "M");
        }

        if (format.contains("yy")) {
            format = format.replaceAll("yy", "yyyy");
        } else {
            format = format.replaceAll("y", "yy");
        }

        if (format.contains("tt") || format.contains("TT")) {
            format = format.replaceAll("tt", "a");
            format = format.replaceAll("TT", "a");
        }

        return format;
    }

    @Override
    public String getName() {
        return "Hijri Date Picker";
    }

    @Override
    public String getVersion() {
        return "7.0.4";
    }

    @Override
    public String getDescription() {
        return "Hijri Date Picker Element";
    }

    @Override
    public String getLabel() {
        return "Hijri Date Picker";
    }

    @Override
    public String getClassName() {
        return getClass().getName();
    }

    @Override
    public String getPropertyOptions() {
        return AppUtil.readPluginResource(getClass().getName(), "/properties/HijriDatePickerElement.json", null, true, "message/HijriDatePickerElement");
    }

    @Override
    public String getFormBuilderCategory() {
        return FormBuilderPalette.CATEGORY_CUSTOM;
    }

    @Override
    public int getFormBuilderPosition() {
        return 500;
    }

    @Override
    public String getFormBuilderIcon() {
        return "<i class=\"fas fa-calendar-alt\"></i>";
    }

    @Override
    public String getFormBuilderTemplate() {
        return "<label class='label'>Hijri Date Picker</label><input type='text' />";
    }
}
