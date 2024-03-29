<div class="form-cell" ${elementMetaData!}>
    
    <#if !(request.getAttribute("org.joget.marketplace.hijripicker.HijriDatePicker_EDITABLE")??)>
        <link rel="stylesheet" href="${request.contextPath}/plugin/org.joget.marketplace.HijriDatePicker/others/bootstrap-datetimepicker.css"/>
        <script type="text/javascript" src="${request.contextPath}/plugin/org.joget.apps.form.lib.DatePicker/js/jquery.placeholder.min.js"></script>
        <script src="${request.contextPath}/plugin/org.joget.marketplace.HijriDatePicker/others/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>
        <script src="${request.contextPath}/plugin/org.joget.marketplace.HijriDatePicker/others/moment-hijri.min.js"></script>
        <script src="${request.contextPath}/plugin/org.joget.marketplace.HijriDatePicker/others/bootstrap-hijri-datetimepicker.js"></script>
    </#if>

    <label field-tooltip="${elementParamName!}" class="label" for="${elementParamName!}_${element.properties.elementUniqueKey!}">${element.properties.label} <span class="form-cell-validator">${decoration}</span><#if error??> <span class="form-error-message">${error}</span></#if></label>
    <#if (element.properties.readonly! == 'true' && element.properties.readonlyLabel! == 'true') >
        <div class="form-cell-value"><span>${value!?html}</span></div>
        <input id="${elementParamName!}" name="${elementParamName!}" class="textfield_${element.properties.elementUniqueKey!}" type="hidden" value="${value!?html}" />
    <#else>
        <div style="position:relative;">
            <input id="${elementParamName!}_${element.properties.elementUniqueKey!}" name="${elementParamName!}" autocomplete="off" <#if (element.properties.readonly! != 'true' || element.properties.readonlyLabel! != 'true') > style="background-image: url('/jw/css/images/calendar.png'); background-position: right 5px center; background-origin: content-box; background-repeat: no-repeat; padding-right: 0px;"</#if> class="textfield_${element.properties.elementUniqueKey!}" type="text" size="" value="${value!?html}" maxlength="100" <#if error??>class="form-error-cell"</#if> <#if element.properties.readonly! == 'true'>readonly</#if> placeholder="<#if (element.properties.placeholder! != '')>${element.properties.placeholder!?html}<#else>${displayFormat!?html}</#if>" />
        </div>
    </#if>

    <script type="text/javascript">
        $("#${elementParamName!}_${element.properties.elementUniqueKey!}").hijriDatePicker({
            showSwitcher: false,
            allowInputToggle: true,
            showTodayButton: false,
            useCurrent: false,
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
            <#if element.properties.currentDateAs! != ''>
                ,minMaxFieldId: "${element.properties.minMaxFieldId}"
                ,minMaxIndicator: "${element.properties.currentDateAs}"
            </#if>
        });
    </script>     
</div>