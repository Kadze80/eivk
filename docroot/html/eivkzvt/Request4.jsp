<%@page pageEncoding="UTF-8"%>
<%@page import="kz.bsbnb.eivk.zvt.Bean.Request4Utility"%>
<%@page import="kz.bsbnb.eivk.zvt.Bean.Request4Bean"%>
<%@page import="java.util.Calendar" %>
<%@ include file="/html/eivkzvt/init.jsp" %>

<%
  int reportYearReg = ParamUtil.getInteger(request, "reportYearReg");
  int reportMonthReg = ParamUtil.getInteger(request, "reportMonthReg");
  int reportDayReg = ParamUtil.getInteger(request, "reportDayReg");  
  Calendar DateReg = Calendar.getInstance();
  DateReg.set(reportYearReg, reportMonthReg, reportDayReg);
  String pRequestNum = ParamUtil.getString(request, "request345Num");
  Boolean pShowRegDate = ParamUtil.getBoolean(request, "showRegDate");

  PortletURL portletURL = renderResponse.createRenderURL();
  portletURL.setParameter("jspPage", "/html/eivkzvt/view.jsp");
  portletURL.setParameter("backURL", portletURL.toString());
  portletURL.setParameter("reportYearReg", String.valueOf(reportYearReg));
  portletURL.setParameter("reportMonthReg", String.valueOf(reportMonthReg));
  portletURL.setParameter("reportDayReg", String.valueOf(reportDayReg));
  portletURL.setParameter("pRequestNum", pRequestNum);
  portletURL.setParameter("pShowRegDate", String.valueOf(pShowRegDate));
    
  String organizationBIN = (String) renderRequest.getPortletSession().getAttribute("ORGANIZATION_BIN");
  
  try {
    List<Request4Bean> datas = Request4Utility.getDatas(organizationBIN, pRequestNum, DateReg.getTime(), pShowRegDate);
%>
<liferay-ui:header title="Request4" />
<p>БИН организации: <%=organizationBIN %></p>
<portlet:resourceURL var="exportURL" >
<portlet:param name="reportType" value="Request4"/>
<portlet:param name="pRequest1Num" value="<%=pRequestNum %>"/>
<portlet:param name="reportYear" value="<%=String.valueOf(reportYearReg) %>"/>
<portlet:param name="reportMonth" value="<%=String.valueOf(reportMonthReg) %>"/>
<portlet:param name="reportDay" value="<%=String.valueOf(reportDayReg) %>"/>
<portlet:param name="pShowRegDate" value="<%=String.valueOf(pShowRegDate) %>"/>
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
			className="kz.bsbnb.eivk.zvt.Bean.Request4Bean"
			keyProperty="row"
			modelVar="model_var"
		>
		<liferay-ui:search-container-column-text name="row" property="row"/>
		<liferay-ui:search-container-column-text name="EEMW_DECLARATION_NUMBER" property="EEMW_DECLARATION_NUMBER" />
		<liferay-ui:search-container-column-text name="EEMW_DECLARATION_DATE" property="EEMW_DECLARATION_DATE" />
		<liferay-ui:search-container-column-text name="t_eisign_str" property="t_eisign_str" />
		<liferay-ui:search-container-column-text name="EEMW_INVOICE_NUMBER" property="EEMW_INVOICE_NUMBER" />
		<liferay-ui:search-container-column-text name="EEMW_INVOICE_DATE" property="EEMW_INVOICE_DATE" />	
		<liferay-ui:search-container-column-text name="EEMW_COST_OF_GOODS" property="EEMW_COST_OF_GOODS" />		
		<liferay-ui:search-container-column-text name="EEMW_CURRENCY_CODE" property="EEMW_CURRENCY_CODE" />			
		<liferay-ui:search-container-column-text name="GECR_NAME" property="GECR_NAME" />
		<liferay-ui:search-container-column-text name="EEMW_DATE_OF_REGISTRATION" property="EEMW_DATE_OF_REGISTRATION" />
		<liferay-ui:search-container-column-text name="EEMW_CONTRACT_NUMBER" property="EEMW_CONTRACT_NUMBER" />
		<liferay-ui:search-container-column-text name="EEMW_CONTRACT_DATE" property="EEMW_CONTRACT_DATE" />
		<liferay-ui:search-container-column-text name="EEMW_RESIDENT_BIN_OR_IIN" property="EEMW_RESIDENT_BIN_OR_IIN" />
		<liferay-ui:search-container-column-text name="EEMW_TAX_AUTH_DATE_STAMP" property="EEMW_TAX_AUTH_DATE_STAMP" />
		<liferay-ui:search-container-column-text name="EEMW_NON_RESIDENT_NAME" property="EEMW_NON_RESIDENT_NAME" />
		<liferay-ui:search-container-column-text name="EEMW_NON_RESIDENT_ID" property="EEMW_NON_RESIDENT_ID" />
		<liferay-ui:search-container-column-text name="EEMW_NON_RESIDENT_COUNTRY_3N" property="EEMW_NON_RESIDENT_COUNTRY_3N" />
		<liferay-ui:search-container-column-text name="GEC_NAME" property="GEC_NAME" />
		<liferay-ui:search-container-column-text name="EEMWS_NAME" property="EEMWS_NAME" />
		<liferay-ui:search-container-column-text name="EEMWC_NEW_DECLARATION_NUMBER" property="EEMWC_NEW_DECLARATION_NUMBER" />
		<liferay-ui:search-container-column-text name="EEMWC_NEW_DECLARATION_DATE" property="EEMWC_NEW_DECLARATION_DATE" />
	</liferay-ui:search-container-row>
		<liferay-ui:search-iterator />
	</liferay-ui:search-container >
<%
  } catch(Exception e) {
	  out.print("<p calss=\"error-header\" > ОШИБКИ ПРИ ПОЛУЧЕНИИ ДАННЫХ:</p>");
	  out.print("<p class=\"show-error\">" + e.getMessage() + "</p>");
  }
%>