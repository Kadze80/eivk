<%@page pageEncoding="UTF-8"%>
<%@page import="kz.bsbnb.eivk.zvt.Bean.Request3Utility"%>
<%@page import="kz.bsbnb.eivk.zvt.Bean.Request3Bean"%>
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
  //portletURL.setParameter("pShowRegDate", String.valueOf(pShowRegDate));
    
  String organizationBIN = (String) renderRequest.getPortletSession().getAttribute("ORGANIZATION_BIN");
  
  try {
    List<Request3Bean> datas = Request3Utility.getDatas(organizationBIN, pRequestNum, DateReg.getTime(), pShowRegDate);
%>
<liferay-ui:header title="Request3" />
<p>БИН организации: <%=organizationBIN %></p>
<portlet:resourceURL var="exportURL" >
<portlet:param name="reportType" value="Request3"/>
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
			className="kz.bsbnb.eivk.zvt.Bean.Request3Bean"
			keyProperty="row"
			modelVar="model_var"
		>
		<liferay-ui:search-container-column-text name="row" property="row"/>
		<liferay-ui:search-container-column-text name="EEMWC_DECLARATION_NUMBER" property="EEMWC_DECLARATION_NUMBER" />
		<liferay-ui:search-container-column-text name="EEMWC_DECLARATION_DATE" property="EEMWC_DECLARATION_DATE" />
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