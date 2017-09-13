<%@page pageEncoding="UTF-8"%>
<%@page import="kz.bsbnb.eivk.zvt.Bean.RequestUtility"%>
<%@page import="kz.bsbnb.eivk.zvt.Bean.RequestBean"%>
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
  String pRequestNum = ParamUtil.getString(request, "requestNum");
  String pRequestRegNum = ParamUtil.getString(request, "requestRegNum");
  String pRequestRezID = ParamUtil.getString(request, "requestRezID");
  String pRequestBIN = ParamUtil.getString(request, "requestBIN");
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
  portletURL.setParameter("pRequestNum", pRequestNum);
  portletURL.setParameter("pRequestRegNum", pRequestRegNum);
  portletURL.setParameter("pRequestRezID", pRequestRezID);
  portletURL.setParameter("pRequestBIN", pRequestBIN);
  portletURL.setParameter("pShowToDate", String.valueOf(pShowToDate));
  
    
  String organizationBIN = (String) renderRequest.getPortletSession().getAttribute("ORGANIZATION_BIN");
  try {
    List<RequestBean> datas = RequestUtility.getDatas(organizationBIN, pDateFrom.getTime(), pDateTo.getTime(), pRequestNum, pRequestRegNum, pRequestRezID, pRequestBIN, pShowToDate);
%>
<liferay-ui:header title="Request2" />
<p>БИН организации: <%=organizationBIN %></p>
<portlet:resourceURL var="exportURL" >
<portlet:param name="reportType" value="Request"/>
<portlet:param name="pRequestNum" value="<%=pRequestNum %>"/>
<portlet:param name="reportYear" value="<%=String.valueOf(reportYear) %>"/>
<portlet:param name="reportMonth" value="<%=String.valueOf(reportMonth) %>"/>
<portlet:param name="reportDay" value="<%=String.valueOf(reportDay) %>"/>
<portlet:param name="reportYearTo" value="<%=String.valueOf(reportYearTo) %>"/>
<portlet:param name="reportMonthTo" value="<%=String.valueOf(reportMonthTo) %>"/>
<portlet:param name="reportDayTo" value="<%=String.valueOf(reportDayTo) %>"/>
<portlet:param name="pRequestRegNum" value="<%=pRequestRegNum %>"/>
<portlet:param name="pRequestRezID" value="<%=pRequestRezID %>"/>
<portlet:param name="pRequestBIN" value="<%=pRequestBIN %>"/>
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
			className="kz.bsbnb.eivk.zvt.Bean.RequestBean"
			keyProperty="row"
			modelVar="model_var"
		>
		<liferay-ui:search-container-column-text name="row" property="row"/>
		<liferay-ui:search-container-column-text name="EEMWR_PS_NUMBER" property="EEMWR_PS_NUMBER" />
		<liferay-ui:search-container-column-text name="EEMWR_PS_DATE" property="EEMWR_PS_DATE" />
		<liferay-ui:search-container-column-text name="EEMWR_EISIGN_STR" property="EEMWR_EISIGN_STR" />
		<liferay-ui:search-container-column-text name="EEMWR_BIN_OR_IIN" property="EEMWR_BIN_OR_IIN" />
		<liferay-ui:search-container-column-text name="EEMWR_NON_RESIDENT_ID" property="EEMWR_NON_RESIDENT_ID" />
		<liferay-ui:search-container-column-text name="EEMWR_DECLARATION_NUMBER" property="EEMWR_DECLARATION_NUMBER" />
		<liferay-ui:search-container-column-text name="WSPS_NAME" property="WSPS_NAME" />
		<liferay-ui:search-container-column-text name="OW_SEND_DATE_TIME" property="OW_SEND_DATE_TIME" />
		<liferay-ui:search-container-column-text name="OW_ERROR_CODE" value="<%= (model_var.getOW_ERROR_CODE() != null)?model_var.getOW_ERROR_CODE():""%>" />
		<liferay-ui:search-container-column-text name="OW_ERROR_MESSAGE" value="<%= (model_var.getOW_ERROR_MESSAGE() != null)?model_var.getOW_ERROR_MESSAGE():""%>" />	
		<liferay-ui:search-container-column-text name="OW_REFERENCE_NUMBER" property="OW_REFERENCE_NUMBER" />	
		<liferay-ui:search-container-column-text name="OW_RESPONSE_ERROR_CODE" value="<%= (model_var.getOW_RESPONSE_ERROR_CODE() != null)?model_var.getOW_RESPONSE_ERROR_CODE():""%>" />	
		<liferay-ui:search-container-column-text name="OW_RESPONSE_ERROR_MESSAGE" value="<%= (model_var.getOW_RESPONSE_ERROR_MESSAGE() != null)?model_var.getOW_RESPONSE_ERROR_MESSAGE():""%>" />													
	</liferay-ui:search-container-row>
		<liferay-ui:search-iterator />
	</liferay-ui:search-container >
<%
  } catch(Exception e) {
	  out.print("<p calss=\"error-header\" > ОШИБКИ ПРИ ПОЛУЧЕНИИ ДАННЫХ:</p>");
	  out.print("<p class=\"show-error\">" + e.getMessage() + "</p>");
  }
%>