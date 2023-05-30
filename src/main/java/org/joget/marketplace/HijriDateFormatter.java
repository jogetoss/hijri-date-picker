package org.joget.marketplace;

import org.joget.apps.app.service.AppUtil;
import org.joget.apps.datalist.model.DataList;
import org.joget.apps.datalist.model.DataListColumn;
import org.joget.apps.datalist.model.DataListColumnFormatDefault;
import org.joget.commons.util.LogUtil;

import java.time.LocalDate;
import java.time.chrono.HijrahChronology;
import java.time.chrono.HijrahDate;
import java.time.format.DateTimeFormatter;

public class HijriDateFormatter extends DataListColumnFormatDefault {

    private static final String FORMAT_HIJRI = "hijri";
    private static final String FORMAT_GREGORIAN = "gregorian";

    @Override
    public String format(DataList dataList, DataListColumn column, Object row, Object value) {
        String colVal = (String) value;
        if (colVal != null && !colVal.isEmpty()) {

            String formatting = getPropertyString("formatting");
            String format = getPropertyString("format");
            if (format != null && !format.isEmpty()) {
                if (FORMAT_HIJRI.equals(formatting)) {
                    //since saving it as iDD-iMM-iYYYY we can use separator and takes the month year and day
                    String[] dates = colVal.split("-");
                    try {
                        HijrahDate hijriDate = HijrahChronology.INSTANCE.date(Integer.parseInt(dates[2]), Integer.parseInt(dates[1]), Integer.parseInt(dates[0]));
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
                        value = hijriDate.format(formatter);
                    } catch (Exception e) {
                        LogUtil.error(getClassName(), e, e.getMessage());
                    }
                }
                if (FORMAT_GREGORIAN.equals(formatting)) {
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
                        value = LocalDate.parse(colVal).format(formatter);
                    } catch (Exception e) {
                        LogUtil.error(getClassName(), e, e.getMessage());
                    }
                }
            }
        }

        return (String) value;
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

    @Override
    public String getName() {
        return "Hijri Date Formatter";
    }

    @Override
    public String getVersion() {
        return "7.0.2";
    }

    @Override
    public String getDescription() {
        return "To format date into Hijri/Gregorian format";
    }

    @Override
    public String getLabel() {
        return "Hijri Date Formatter";
    }

    @Override
    public String getClassName() {
        return this.getClass().getName();
    }

    @Override
    public String getPropertyOptions() {
        return AppUtil.readPluginResource(getClass().getName(), "/properties/HijriDateFormatter.json", null, true, "message/HijriDateFormatter");
    }

}
