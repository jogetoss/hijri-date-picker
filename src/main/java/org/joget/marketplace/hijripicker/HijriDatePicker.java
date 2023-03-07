package org.joget.marketplace.hijripicker;

import java.util.Map;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.form.model.Element;
import org.joget.apps.form.model.FormBuilderPalette;
import org.joget.apps.form.model.FormBuilderPaletteElement;
import org.joget.apps.form.model.FormData;
import org.joget.apps.form.model.FormRowSet;
import org.joget.apps.form.service.FormUtil;

public class HijriDatePicker extends Element implements FormBuilderPaletteElement {

    @Override
    public String renderTemplate(FormData formData, Map dataModel) {
        String template = "hijriDatePicker.ftl";
        String displayFormat = getPropertyString("format");
        if (displayFormat !=null && !displayFormat.isEmpty()) {
            displayFormat = getHijriDateFormat(displayFormat);
        } else {
            displayFormat = "iDD-iMM-iYYYY";
        }
        String value = FormUtil.getElementPropertyValue(this, formData);
        dataModel.put("value", value);
        dataModel.put("displayFormat", displayFormat);
        String html = FormUtil.generateElementHtml(this, formData, template, dataModel);
        return html;
    }

    protected String getHijriDateFormat(String format) {
        format = format.toUpperCase();
        if (format.contains("DD")) {
            format = format.replaceAll("DD", "iDD");
        }
        if (format.contains("MM")) {
            format = format.replaceAll("MM", "iMM");
        }
        if (format.contains("YY")) {
            format = format.replaceAll("YY", "iYYYY");
        }
        return format;
    }

    @Override
    public String getName() {
        return "Hijri Date Picker";
    }

    @Override
    public String getVersion() {
        return "7.0.0";
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
