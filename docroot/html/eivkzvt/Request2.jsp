<%@page pageEncoding="UTF-8"%>
<%@page import="kz.bsbnb.eivk.zvt.Bean.Request2Utility"%>
<%@page import="kz.bsbnb.eivk.zvt.Bean.Request2Bean"%>
<%@page import="java.util.Calendar" %>
<%@ include file="/html/eivkzvt/init.jsp" %>

<%
  int reportYear = ParamUtil.getInteger(request, "reportYear");
  int reportMonth = ParamUtil.getInteger(request, "reportMonth");
  int reportDay = ParamUtil.getInteger(request, "reportDay");
  int reportYearTo = ParamUtil.getInteger(request, "reportYearTo");
  int reportMonthTo = ParamUtil.getInteger(request, "reportMonthTo");
  int reportDayTo = ParamUtil.getInteger(request, "reportDayTo");
  Calendar pDateFrom = Calendar.getInstance();
  Calendar pDateTo = Calendar.getInstance();
  pDateFrom.set(reportYear, reportMonth, reportDay);
  pDateTo.set(reportYearTo, reportMonthTo, reportDayTo);
  String pRequest2Num = ParamUtil.getString(request, "request2Num");
  String pRequest2BIN = ParamUtil.getString(request, "request2BIN");
  Boolean pShowToDate = ParamUtil.getBoolean(request, "showToDate");

  PortletURL portletURL = renderResponse.createRenderURL();
  portletURL.setParameter("jspPage", "/html/eivkzvt/view.jsp");
  portletURL.setParameter("backURL", portletURL.toString());
  portletURL.setParameter("reportYear", String.valueOf(reportYear));
  portletURL.setParameter("reportMonth", String.valueOf(reportMonth));
  portletURL.setParameter("reportDay", String.valueOf(reportDay));
  portletURL.setParameter("reportYearTo", String.valueOf(reportYearTo));
  portletURL.setParameter("reportMonthTo", String.valueOf(reportMonthTo));
  portletURL.setParameter("reportDayTo", String.valueOf(reportDayTo));
  portletURL.setParameter("pRequest2Num", pRequest2Num);
  portletURL.setParameter("pRequest2BIN", pRequest2BIN);
  portletURL.setParameter("pShowToDate", String.valueOf(pShowToDate));
    
  String organizationBIN = (String) renderRequest.getPortletSession().getAttribute("ORGANIZATION_BIN");
  try {
    List<Request2Bean> datas = Request2Utility.getDatas(organizationBIN, pDateFrom.getTime(), pDateTo.getTime(), pRequest2Num, pRequest2BIN, pShowToDate);
%>
<liferay-ui:header title="Request2" />
<p>БИН организации: <%=organizationBIN %></p>
<portlet:resourceURL var="exportURL" >
<portlet:param name="reportType" value="Request2"/>
<portlet:param name="pRequest2Num" value="<%=pRequest2Num %>"/>
<portlet:param name="reportYear" value="<%=String.valueOf(reportYear) %>"/>
<portlet:param name="reportMonth" value="<%=String.valueOf(reportMonth) %>"/>
<portlet:param name="reportDay" value="<%=String.valueOf(reportDay) %>"/>
<portlet:param name="reportYearTo" value="<%=String.valueOf(reportYearTo) %>"/>
<portlet:param name="reportMonthTo" value="<%=String.valueOf(reportMonthTo) %>"/>
<portlet:param name="reportDayTo" value="<%=String.valueOf(reportDayTo) %>"/>
<portlet:param name="pRequest2BIN" value="<%=pRequest2BIN %>"/>
<portlet:param name="pShowToDate" value="<%=String.valueOf(pShowToDate) %>"/>
</portlet:resourceURL>
<a href="<%=exportURL %>" target="_blank" ><liferay-ui:message key="ExportToExcell" /> </a>
<liferay-ui:search-container
        emptyResultsMessage="no-data-found"
        delta="100"
        iteratorURL="<%= portletURL %>"
>
<liferay-ui:search-container-results>
			<%
              if (datas != null) {
                  results = ListUtil.subList(datas, 0,datas.size());
                  total = datas.size();
                  pageContext.setAttribute("results", results);
                  pageContext.setAttribute("total", total);
                  pageContext.setAttribute("tempResults", datas);
              }
            %>
		</liferay-ui:search-container-results>

		<liferay-ui:search-container-row
			className="kz.bsbnb.eivk.zvt.Bean.Request2Bean"
			keyProperty="row"
			modelVar="model_var"
		>
		<liferay-ui:search-container-column-text name="row" property="row"/>
		<liferay-ui:search-container-column-text name="DECLARATION_NUMBER" property="DECLARATION_NUMBER" />
		<liferay-ui:search-container-column-text name="DECLARATION_DATE" property="DECLARATION_DATE" />
		<liferay-ui:search-container-column-text name="EISIGN_STR" property="EISIGN_STR" />
		<liferay-ui:search-container-column-text name="INVOICE_NUMBER" property="INVOICE_NUMBER" />
		<liferay-ui:search-container-column-text name="INVOICE_DATE" property="INVOICE_DATE" />
		<liferay-ui:search-container-column-text name="COST_OF_GOODS" property="COST_OF_GOODS" />
		<liferay-ui:search-container-column-text name="CURRENCY_CODE" property="CURRENCY_CODE" />
		<liferay-ui:search-container-column-text name="DATE_OF_REGISTRATION" property="DATE_OF_REGISTRATION" />
		<liferay-ui:search-container-column-text name="CONTRACT_NUMBER" property="CONTRACT_NUMBER" />
		<liferay-ui:search-container-column-text name="CONTRACT_DATE" property="CONTRACT_DATE" />	
		<liferay-ui:search-container-column-text name="RESIDENT_BIN_OR_IIN" property="RESIDENT_BIN_OR_IIN" />	
		<liferay-ui:search-container-column-text name="TAX_AUTH_DATE_STAMP" property="TAX_AUTH_DATE_STAMP" />	
		<liferay-ui:search-container-column-text name="NON_RESIDENT_NAME" property="NON_RESIDENT_NAME" />	
		<liferay-ui:search-container-column-text name="NON_RESIDENT_ID" property="NON_RESIDENT_ID" />	
		<liferay-ui:search-container-column-text name="NON_RESIDENT_COUNTRY_3N" property="NON_RESIDENT_COUNTRY_3N" />																	
	</liferay-ui:search-container-row>
		<liferay-ui:search-iterator />
	</liferay-ui:search-container >
<%
  } catch(Exception e) {
	  out.print("<p calss=\"error-header\" > ОШИБКИ ПРИ ПОЛУЧЕНИИ ДАННЫХ:</p>");
	  out.print("<p class=\"show-error\">" + e.getMessage() + "</p>");
  }
%>