<div class="form-cell" ${elementMetaData!}>

    <#if !(request.getAttribute("org.joget.marketplace.hijripicker.HijriDatePicker_EDITABLE")??)>
        <link rel="stylesheet" href="${request.contextPath}/plugin/org.joget.marketplace.HijriDatePicker/others/bootstrap-datetimepicker.css"/>
        <link rel="stylesheet" href="${request.contextPath}/plugin/org.joget.marketplace.HijriDatePicker/others/all.min.css"/>
        <script type="text/javascript" src="${request.contextPath}/plugin/org.joget.apps.form.lib.DatePicker/js/jquery.placeholder.min.js"></script>
        <script src="${request.contextPath}/plugin/org.joget.marketplace.HijriDatePicker/others/bootstrap.min.js"></script>
        <script src="${request.contextPath}/plugin/org.joget.marketplace.HijriDatePicker/others/moment-hijri.min.js"></script>
        <script src="${request.contextPath}/plugin/org.joget.marketplace.HijriDatePicker/others/bootstrap-hijri-datetimepicker.js"></script>
    </#if>

    <label field-tooltip="${elementParamName!}" class="label" for="${elementParamName!}">${element.properties.label} <span class="form-cell-validator">${decoration}</span><#if error??> <span class="form-error-message">${error}</span></#if></label>
    <#if (element.properties.readonly! == 'true' && element.properties.readonlyLabel! == 'true') >
        <div class="form-cell-value"><span>${value!?html}</span></div>
        <input id="${elementParamName!}" name="${elementParamName!}" class="textfield_${element.properties.elementUniqueKey!}" type="hidden" value="${value!?html}" />
    <#else>
        <div style="position:relative;">
            <div class="input-group">
                <input id="${elementParamName!}_${element.properties.elementUniqueKey!}" name="${elementParamName!}" aria-label="Select date" aria-describedby="calendar-icon" autocomplete="off" class="form-control" type="text" size="" value="${value!?html}" maxlength="100" <#if error??>class="form-error-cell"</#if> <#if element.properties.readonly! == 'true'>readonly</#if> placeholder="<#if (element.properties.placeholder! != '')>${element.properties.placeholder!?html}<#else>${displayFormat!?html}</#if>" />
                <#if (element.properties.readonly! != 'true' || element.properties.readonlyLabel! != 'true') >
                    <span class="input-group-text" id="hijri-calicon-${elementParamName!}_${element.properties.elementUniqueKey!}"><i class="fas fa-calendar-alt"></i></span>
                </#if>
            </div>
        </div>
    </#if>

    <script type="text/javascript">
        $("#${elementParamName!}_${element.properties.elementUniqueKey!}").hijriDatePicker({
            showSwitcher: false,
            allowInputToggle: true,
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
            <#if element.properties.currentDateAs! != ''>
                ,${element.properties.currentDateAs}: "${minMaxDate}"
            </#if>
        });
        $("#hijri-calicon-${elementParamName!}_${element.properties.elementUniqueKey!}").on('click', function() {
            $("#${elementParamName!}_${element.properties.elementUniqueKey!}").focus();
        })
    </script>     
</div>
