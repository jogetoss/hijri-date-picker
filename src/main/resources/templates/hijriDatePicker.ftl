<div class="form-cell" ${elementMetaData!}>

    <#if !(request.getAttribute("org.joget.marketplace.hijripicker.HijriDatePicker_EDITABLE")??)>
        <link rel="stylesheet" href="${request.contextPath}/plugin/org.joget.marketplace.hijripicker.HijriDatePicker/others/bootstrap-datetimepicker.css"/>
        <script type="text/javascript" src="${request.contextPath}/plugin/org.joget.apps.form.lib.DatePicker/js/jquery.placeholder.min.js"></script>
        <script src="${request.contextPath}/plugin/org.joget.marketplace.hijripicker.HijriDatePicker/others/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
        <script src="${request.contextPath}/plugin/org.joget.marketplace.hijripicker.HijriDatePicker/others/moment-hijri.min.js"></script>
        <script src="${request.contextPath}/plugin/org.joget.marketplace.hijripicker.HijriDatePicker/others/bootstrap-hijri-datetimepicker.js"></script>
    </#if>

    <label field-tooltip="${elementParamName!}" class="label" for="${elementParamName!}">${element.properties.label} <span class="form-cell-validator">${decoration}</span><#if error??> <span class="form-error-message">${error}</span></#if></label>
    <#if (element.properties.readonly! == 'true' && element.properties.readonlyLabel! == 'true') >
        <div class="form-cell-value"><span>${valueLabel!?html}</span></div>
        <input id="${elementParamName!}" name="${elementParamName!}" class="textfield_${element.properties.elementUniqueKey!}" type="hidden" value="${value!?html}" />
    <#else>
        <input id="${elementParamName!}_${element.properties.elementUniqueKey!}" name="${elementParamName!}" autocomplete="off" class="textfield_${element.properties.elementUniqueKey!}" type="text" size="" value="${value!?html}" maxlength="100" <#if error??>class="form-error-cell"</#if> <#if element.properties.readonly! == 'true'>readonly</#if> placeholder="<#if (element.properties.placeholder! != '')>${element.properties.placeholder!?html}<#else>${displayFormat!?html}</#if>" />
    </#if>

    <script type="text/javascript">
        $("#${elementParamName!}_${element.properties.elementUniqueKey!}").hijriDatePicker({
            showSwitcher: false,
            allowInputToggle: false,
            showTodayButton: false,
            useCurrent: true,
            viewMode:'days',
            keepOpen: false,
            hijri: true,
            debug: false,
            showClear: false,
            showTodayButton: true,
            showClose: true,
            hijri: true,
            isRTL: true,
            locale: "ar-sa",
            format: "YYYY-MM-DD",
            hijriFormat: "${displayFormat}",
            dayViewHeaderFormat: "MMMM YYYY",
            hijriDayViewHeaderFormat: "iMMMM iYYYY"
        });
    </script>     
</div>


