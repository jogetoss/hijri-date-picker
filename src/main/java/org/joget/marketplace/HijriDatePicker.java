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
        String dataFormat = getPropertyString("dataFormat");
        FormRowSet rowSet = null;
        String id = getPropertyString(FormUtil.PROPERTY_ID);
        String resultDate = null;
        if (id != null) {
            String value = FormUtil.getElementPropertyValue(this, formData);
            String saveDate = getPropertyString("dataFormat").toLowerCase();
            String pattern = "[^dmy.\\/\\\\\\\\-]";
            saveDate = saveDate.replaceAll(pattern, "");
            if (dataFormat != null && !dataFormat.isEmpty()) {
                for (int x = 0; x <=2; x++) {
                    // 0 = find date
                    // 1 = find month
                    // 2 = find year
                    resultDate = getDateByFormat(value,x);
                    if(resultDate.equals("break")) {
                        //if for some reason the date format isn't readable
                        //we save as shown in display which is the value right now
                        //and we stop
                        break;
                    } else {
                        if(saveDate.contains("dd") && x == 0){
                            saveDate = saveDate.replace("dd", resultDate);
                        } else if (saveDate.contains("d")  && x == 0) {
                            saveDate = saveDate.replace("dd", resultDate);
                        }

                        if(saveDate.contains("mm")  && x == 1){
                            saveDate = saveDate.replace("mm", resultDate);
                        } else if (saveDate.contains("m")  && x == 1) {
                            saveDate = saveDate.replace("m", resultDate);
                        }

                        if(saveDate.contains("yyyy") && x == 2){
                            saveDate = saveDate.replace("yyyy", resultDate);
                        } else if (saveDate.contains("yy") && x == 2) {
                            saveDate = saveDate.replace("yy", resultDate);
                        }
                    }
                }
            }

            //String binderValue = formData.getLoadBinderDataProperty(this, id);
            if (!FormUtil.isReadonly(this, formData)) {
                if (value != null && !value.isEmpty()) {
                    FormRow result = new FormRow();
                    if(resultDate.equals("break")) {
                        result.setProperty(id, value);
                    } else {
                        result.setProperty(id, saveDate);
                    }
                    rowSet = new FormRowSet();
                    rowSet.add(result);
                }
            }
        }
        return rowSet;
    }

    public String getDateByFormat(String value, int x) {
        String displayFormat = getPropertyString("format").toLowerCase();
        String saveDate = getPropertyString("dataFormat").toLowerCase();

        if(saveDate.contains("dd") &&displayFormat.contains("dd") && x == 0){
            return getDatefromValue(value,2, "d");
        } else if (saveDate.contains("d") && displayFormat.contains("d")  && x == 0) {
            return getDatefromValue(value,2, "d");
        }

        if(saveDate.contains("mm") && displayFormat.contains("mm")  && x == 1){
            return getDatefromValue(value,2, "m");
        } else if (saveDate.contains("m") && displayFormat.contains("m")  && x == 1) {
            return getDatefromValue(value,2, "m");
        }

        if(saveDate.contains("yyyy") && displayFormat.contains("yyyy") && x == 2){
            return getDatefromValue(value,4, "y");
        } else if ( displayFormat.contains("yy") && displayFormat.contains("yy")  && x == 2) {
            return getDatefromValue(value,2, "y");
        }

        return "break";

    }

    private String getDatefromValue(String value, int i, String partial) {
        String displayFormat = getPropertyString("format");
        int position = displayFormat.indexOf(partial);
        if (position != -1) {
            if (i == 2) {
                return value.substring(position, position+2);
            }
            if (i == 4) {
                return value.substring(position, position+4);
            }
        }
        return "break";

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

    private String hijriDisplayFormat(String hijriDisplayFormat) {
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
