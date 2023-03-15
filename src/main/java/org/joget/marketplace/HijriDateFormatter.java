package org.joget.marketplace;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.chrono.HijrahChronology;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import org.joget.apps.app.service.AppUtil;
import org.joget.apps.datalist.model.DataList;
import org.joget.apps.datalist.model.DataListColumn;
import org.joget.apps.datalist.model.DataListColumnFormatDefault;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joget.commons.util.LogUtil;

public class HijriDateFormatter extends DataListColumnFormatDefault {

    private static final String FORMAT_HIJRI = "hijri";
    private static final String FORMAT_GREGORIAN = "gregorian";

    @Override
    public String format(DataList dataList, DataListColumn column, Object row, Object value) {
        String colVal = (String) value;
        if (colVal != null && !colVal.isEmpty()) {
            String formatting = getPropertyString("formatting");
            String format = getPropertyString("format");
            boolean isValidTimestamp = isValidTimestamp(colVal);
            if (isValidTimestamp) {
                if (format != null && !format.isEmpty()) {
                    if (FORMAT_HIJRI.equals(formatting)) {
                        DateTime dt = new DateTime(Long.parseLong(colVal));
                        int calYear = dt.get(DateTimeFieldType.year());
                        int calMonth = dt.get(DateTimeFieldType.monthOfYear());
                        int calDay = dt.get(DateTimeFieldType.dayOfMonth());
                        LocalDate date = LocalDate.of(calYear, calMonth, calDay);
                        try {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
                            value = HijrahChronology.INSTANCE.date(date).format(formatter);
                        } catch (Exception e) {
                            LogUtil.error(getClassName(), e, e.getMessage());
                        }
                    }
                    if (FORMAT_GREGORIAN.equals(formatting)) {
                        Date simpleDate = new Date(Long.parseLong(colVal));
                        SimpleDateFormat sdf = new SimpleDateFormat(format);
                        try {
                            value = sdf.format(simpleDate);
                        } catch (Exception e) {
                            LogUtil.error(getClassName(), e, e.getMessage());
                        }
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
