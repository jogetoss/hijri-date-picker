package org.joget.marketplace;

import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.*;
import org.joget.apps.form.service.FormUtil;
import org.joget.commons.util.LogUtil;

import java.time.chrono.HijrahChronology;
import java.time.chrono.HijrahDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HijriDatePicker extends Element implements FormBuilderPaletteElement {

    private static final String DEFAULT_DISPLAY_FORMAT = "dd-MM-yy";

    @Override
    public String renderTemplate(FormData formData, Map dataModel) {
        String template = "hijriDatePicker.ftl";
        String value = FormUtil.getElementPropertyValue(this, formData);
        String displayFormat = getPropertyString("format");
        String resultDate = null;
        String saveDate = getPropertyString("format");


        if (value != null && !value.isEmpty()) {
            if (displayFormat == null || displayFormat.isEmpty()) {
                displayFormat = DEFAULT_DISPLAY_FORMAT;
            }

        }

        String hijriDisplayFormat = "iDD-iMM-iYYYY";
        if (displayFormat != null && !displayFormat.isEmpty()) {
            //reverse back
            hijriDisplayFormat = hijriDisplayFormat(displayFormat);
            if (value != null && !value.isEmpty()) {
                if (saveDate != null && !saveDate.isEmpty()) {
                    for (int x = 0; x <= 2; x++) {
                        // 0 = find date
                        // 1 = find month
                        // 2 = find year
                        resultDate = getDateByFormat(value, x, false);
                        if (resultDate.equals("break")) {
                            //if for some reason the date format isn't readable
                            //we save as shown in display which is the value right now
                            //and we stop
                            break;
                        } else {
                            saveDate = checkContains(saveDate,resultDate,x);
                        }
                    }
                }
            } else {
                hijriDisplayFormat = hijriDisplayFormat(displayFormat);
            }
        }
        if(resultDate == null && value.isEmpty()) {
            dataModel.put("value", value);
            dataModel.put("displayFormat", hijriDisplayFormat);
        } else if(resultDate.equals("break")){
            dataModel.put("value", value);
            dataModel.put("displayFormat", hijriDisplayFormat);
        } else {
            dataModel.put("value", saveDate);
            dataModel.put("displayFormat", hijriDisplayFormat(displayFormat));
        }

        String html = FormUtil.generateElementHtml(this, formData, template, dataModel);
        return html;
    }

    private String checkContains(String saveDate, String resultDate, int x) {
        if (saveDate.contains("dd") && x == 0) {
            saveDate = saveDate.replace("dd", resultDate);
        } else if (saveDate.contains("d") && x == 0) {
            saveDate = saveDate.replace("dd", resultDate);
        }

        if (saveDate.contains("MM") && x == 1) {
            saveDate = saveDate.replace("MM", resultDate);
        } else if (saveDate.contains("M") && x == 1) {
            saveDate = saveDate.replace("M", resultDate);
        }

        if (saveDate.contains("yyyy") && x == 2) {
            saveDate = saveDate.replace("yyyy", resultDate);
        } else if (saveDate.contains("yy") && x == 2) {
            saveDate = saveDate.replace("yy", resultDate);
        }
        return saveDate;
    }

    @Override
    public FormRowSet formatData(FormData formData) {
        String dataFormat = getPropertyString("dataFormat");
        String displayFormat = getPropertyString("format");
        FormRowSet rowSet = null;
        String id = getPropertyString(FormUtil.PROPERTY_ID);
        String resultDate = null;

        if (id != null) {
            String value = FormUtil.getElementPropertyValue(this, formData);
            String saveDate = getPropertyString("dataFormat");
            String pattern = "[^dMy.\\/\\\\\\\\-]";
            saveDate = saveDate.replaceAll(pattern, "");
            DateTimeFormatter hijrahFormatter = DateTimeFormatter.ofPattern(displayFormat)
                    .withChronology(HijrahChronology.INSTANCE);

            //check if valid date if value is not empty
            if(!value.isEmpty()) {
                try {
                    HijrahDate date = hijrahFormatter.parse(value, HijrahDate::from);
                } catch (DateTimeParseException ex) {
                    LogUtil.error(getClassName(), ex, ex.getMessage());
                    return rowSet;
                }
            }


            if (dataFormat != null && !dataFormat.isEmpty() && value != null && !value.isEmpty() ) {
                for (int x = 0; x <= 2; x++) {
                    // 0 = find date
                    // 1 = find month
                    // 2 = find year
                    resultDate = getDateByFormat(value, x, true);
                    if (resultDate.equals("break")) {
                        //if for some reason the date format isn't readable
                        //we save as shown in display which is the value right now
                        //and we stop
                        break;
                    } else {
                        saveDate = checkContains(saveDate,resultDate,x);
                    }
                }
            } else {
                resultDate = "break";
            }

            //String binderValue = formData.getLoadBinderDataProperty(this, id);
            if (!FormUtil.isReadonly(this, formData)) {
                if (value != null) {
                    FormRow result = new FormRow();
                    if (resultDate.equals("break") || value.isEmpty()) {
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

    public String getDateByFormat(String value, int x, boolean save) {
        String displayFormat = getPropertyString("format");
        String saveDate = getPropertyString("dataFormat");

        if (saveDate.contains("dd") && displayFormat.contains("dd") && x == 0) {
            return getDatefromValue(value, 2, "d", save);
        } else if (saveDate.contains("d") && displayFormat.contains("d") && x == 0) {
            return getDatefromValue(value, 2, "d", save);
        }

        if (saveDate.contains("MM") && displayFormat.contains("MM") && x == 1) {
            return getDatefromValue(value, 2, "M", save);
        } else if (saveDate.contains("M") && displayFormat.contains("M") && x == 1) {
            return getDatefromValue(value, 2, "M", save);
        }

        if (saveDate.contains("yyyy") && displayFormat.contains("yyyy") && x == 2) {
            return getDatefromValue(value, 4, "y", save);
        } else if (displayFormat.contains("yy") && displayFormat.contains("yy") && x == 2) {
            return getDatefromValue(value, 2, "y", save);
        }

        return "break";

    }

    private String getDatefromValue(String value, int i, String partial, boolean save) {
        String displayFormat = save ? getPropertyString("format") : getPropertyString("dataFormat");
        int position = displayFormat.indexOf(partial);
        if (position != -1) {
            if (i == 2) {
                return value.substring(position, position + 2);
            }
            if (i == 4) {
                return value.substring(position, position + 4);
            }
        }
        return "break";

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

    @Override
    public String getName() {
        return "Hijri Date Picker";
    }

    @Override
    public String getVersion() {
        return "7.0.5";
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
        return "Marketplace";
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
