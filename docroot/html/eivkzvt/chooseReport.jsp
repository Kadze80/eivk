<%@page pageEncoding="UTF-8"%>
<%@ include file="/html/init.jsp" %>
<%
	Calendar calendar = Calendar.getInstance();
	int reportDay = ParamUtil.getInteger(request, "reportDay",calendar.get(Calendar.DAY_OF_MONTH));
	int reportMonth = ParamUtil.getInteger(request, "reportMonth",calendar.get(Calendar.MONTH));
	int reportYear = ParamUtil.getInteger(request, "reportYear",calendar.get(Calendar.YEAR));
	int reportDay1 = ParamUtil.getInteger(request, "reportDay1",calendar.get(Calendar.DAY_OF_MONTH));
	int reportMonth1 = ParamUtil.getInteger(request, "reportMonth1",calendar.get(Calendar.MONTH));
	int reportYear1 = ParamUtil.getInteger(request, "reportYear1",calendar.get(Calendar.YEAR));
	int reportDayTo = ParamUtil.getInteger(request, "reportDayTo",calendar.get(Calendar.DAY_OF_MONTH));
	int reportMonthTo = ParamUtil.getInteger(request, "reportMonthTo",calendar.get(Calendar.MONTH));
	int reportYearTo = ParamUtil.getInteger(request, "reportYearTo",calendar.get(Calendar.YEAR));
	int reportDayReg = ParamUtil.getInteger(request, "reportDayReg",calendar.get(Calendar.DAY_OF_MONTH));
	int reportMonthReg = ParamUtil.getInteger(request, "reportMonthReg",calendar.get(Calendar.MONTH));
	int reportYearReg = ParamUtil.getInteger(request, "reportYearReg",calendar.get(Calendar.YEAR));
  	String reportType = ParamUtil.getString(request, "reportType","/html/eivkzvt/empty.jsp");
  	String request1Num = ParamUtil.getString(request, "request1Num");
  	String request1Date = ParamUtil.getString(request, "request1Date");
  	String request1IsExpImp = ParamUtil.getString(request, "request1IsExpImp");
  	
  	String organizationBIN = (String) renderRequest.getPortletSession().getAttribute("ORGANIZATION_BIN");
    Boolean showToDate = ParamUtil.getBoolean(request, "showToDate",false);
    Boolean showRegDate = ParamUtil.getBoolean(request, "showRegDate",false);
  	
%>

<portlet:renderURL var="showReportURL">
<portlet:param name="jspPage" value="/html/eivkzvt/view.jsp"/>
</portlet:renderURL>
<portlet:resourceURL var="serveResourceURL"/>
<br>
<aui:form name="frmShowReport" method="post" action="<%=showReportURL %>">

<aui:select name="reportType" label="reportType" onChange="showAddtParams()">
<option value="/html/eivkzvt/empty.jsp" <%if(reportType.equals("/html/eivkzvt/empty.jsp")) out.print("selected"); %> >&nbsp; </option>
<option value="/html/eivkzvt/Request.jsp" <%if(reportType.equals("/html/eivkzvt/Request.jsp")) out.print("selected"); %>><liferay-ui:message key="Request"/></option>
<option value="/html/eivkzvt/Request1.jsp" <%if(reportType.equals("/html/eivkzvt/Request1.jsp")) out.print("selected"); %>><liferay-ui:message key="Request1"/></option>
<option value="/html/eivkzvt/Request2.jsp" <%if(reportType.equals("/html/eivkzvt/Request2.jsp")) out.print("selected"); %>><liferay-ui:message key="Request2"/></option>
<option value="/html/eivkzvt/Request3.jsp" <%if(reportType.equals("/html/eivkzvt/Request3.jsp")) out.print("selected"); %>><liferay-ui:message key="Request3"/></option>
<option value="/html/eivkzvt/Request4.jsp" <%if(reportType.equals("/html/eivkzvt/Request4.jsp")) out.print("selected"); %>><liferay-ui:message key="Request4"/></option>
</aui:select>
<br>

<div id="<portlet:namespace/>dRequest" style="display:none">
<aui:input name="requestNum" label="requestNum" size="100" />
<aui:input name="requestRegNum" label="requestRegNum" size="100" />
<aui:input name="requestRezID" label="requestRezID" size="100"/>
<aui:input name="requestBIN" label="requestBIN" size="100"/>
</div>

<div id="<portlet:namespace/>dRequest1" style="display:none">
<aui:input name="request1Num" label="requestNum" size="100" />
<br>
<div id="<portlet:namespace/>rDate" style="display:none">
<label id="<portlet:namespace/>request1Date"><liferay-ui:message key="requestDate"/></label>
<br>
 <liferay-ui:input-date dayParam="reportDay1" monthParam="reportMonth1" yearParam="reportYear1" 
   yearRangeStart="1980" yearRangeEnd="2100"
   yearValue="<%=reportYear1%>" monthValue="<%=reportMonth1%>" dayValue="<%=reportDay1%>"
   />
</div>
<br>
<br>
<aui:input name="request1BIN" label="requestBIN" size="100"/>
<aui:input name="request1RezID" label="requestRezID" size="100"/>
<aui:input name="request1RegNum" label="requestRegNum" size="100"/>
<aui:select name="request1IsExpImp" label="requestIsExpImp">
<option value="1" "selected"><liferay-ui:message key="export"/></option>
<option value="2"><liferay-ui:message key="import"/></option>
</aui:select>
</div>

<br>
<div id="<portlet:namespace/>dRequest2" style="display:none">
<aui:input name="request2Num" label="request2Num" size="100"/>
<aui:input name="request2BIN" label="requestBIN" size="100"/>
</div>

<br>
<div id="<portlet:namespace/>dShowDate" style="display:none">
<aui:input name="showToDate" type="checkbox" value="<%=showToDate %>" onClick="ShowToDate(this)" label="showToDate"></aui:input>
</div>

<br>
<div id="<portlet:namespace/>DateFrom" style="display:none">
<label id="<portlet:namespace/>DateFromlbl"><liferay-ui:message key="from"/></label>
<br>
 <liferay-ui:input-date dayParam="reportDay" monthParam="reportMonth" yearParam="reportYear" 
   yearRangeStart="1980" yearRangeEnd="2100"
   yearValue="<%=reportYear%>" monthValue="<%=reportMonth%>" dayValue="<%=reportDay%>"
   />
</div>
<br>
<br>
<div id="<portlet:namespace/>DateTo" style="display:none">
<label><liferay-ui:message key="to"/></label>
<br>
<liferay-ui:input-date dayParam="reportDayTo" monthParam="reportMonthTo" yearParam="reportYearTo" 
   yearRangeStart="1980" yearRangeEnd="2100"
   yearValue="<%=reportYearTo%>" monthValue="<%=reportMonthTo%>" dayValue="<%=reportDayTo%>"
   />
</div>
<br>
<br>

<div id="<portlet:namespace/>dRequest3_4_5" style="display:none">
<aui:input name="request345Num" label="requestRegNum" size="100" />
<br>
<aui:input name="showRegDate" type="checkbox" value="<%=showRegDate %>" onClick="ShowRegDate(this)" label="showRegDate"></aui:input>
<br>
<div id="<portlet:namespace/>DateReg" style="display:none">
<label><liferay-ui:message key="requestRegDate"/></label>
<br>
<liferay-ui:input-date dayParam="reportDayReg" monthParam="reportMonthReg" yearParam="reportYearReg" 
   yearRangeStart="1900" yearRangeEnd="2100"
   yearValue="<%=reportYearReg %>" monthValue="<%= reportMonthReg%>" dayValue="<%=reportDayReg %>"
   />
</div>
</div>

<br>
<br>
<aui:button type="submit" value="showReport" />

</aui:form>

<script>
function ShowRegDate() {
	if(document.getElementById("<portlet:namespace/>showRegDate").value == 'true'){
        document.getElementById("<portlet:namespace/>DateReg").style.display='';
    }  else {
    	document.getElementById("<portlet:namespace/>DateReg").style.display='none';
    }
}

function ShowToDate() {
	if (document.getElementById("<portlet:namespace/>showToDate").value == 'true') {
			document.getElementById("<portlet:namespace/>DateFrom").style.display = '';
			document.getElementById("<portlet:namespace/>DateTo").style.display = '';
		} else {
			document.getElementById("<portlet:namespace/>DateFrom").style.display = 'none';
			document.getElementById("<portlet:namespace/>DateTo").style.display = 'none';
		}
	}

	function showAddtParams() {
		var rType = document.getElementById("<portlet:namespace/>reportType").value;
		document.getElementById("<portlet:namespace/>DateFrom").style.display = 'none';
		document.getElementById("<portlet:namespace/>DateTo").style.display = 'none';
		document.getElementById("<portlet:namespace/>dShowDate").style.display = 'none';
		document.getElementById("<portlet:namespace/>rDate").style.display = 'none';
		document.getElementById("<portlet:namespace/>dRequest").style.display = 'none';
		document.getElementById("<portlet:namespace/>dRequest1").style.display = 'none';
		document.getElementById("<portlet:namespace/>dRequest2").style.display = 'none';
		document.getElementById("<portlet:namespace/>dRequest3_4_5").style.display = 'none';

		switch (rType) {
		case "/html/eivkzvt/Request.jsp":
			document.getElementById("<portlet:namespace/>dRequest").style.display = '';
			document.getElementById("<portlet:namespace/>dShowDate").style.display = '';
			break;
		case "/html/eivkzvt/Request1.jsp":
			document.getElementById("<portlet:namespace/>dRequest1").style.display = '';
			document.getElementById("<portlet:namespace/>rDate").style.display = '';
			break;
		case "/html/eivkzvt/Request2.jsp":
			document.getElementById("<portlet:namespace/>dRequest2").style.display = '';
			document.getElementById("<portlet:namespace/>dShowDate").style.display = '';
			break;
		case "/html/eivkzvt/Request3.jsp":
			document.getElementById("<portlet:namespace/>dRequest3_4_5").style.display = '';
			break;
		case "/html/eivkzvt/Request4.jsp":
			document.getElementById("<portlet:namespace/>dRequest3_4_5").style.display = '';
			break;
		}

	}
	ShowToDate();
	ShowRegDate();
	showAddtParams();	
</script>
<br>
