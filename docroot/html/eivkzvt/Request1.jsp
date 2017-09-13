<%@page pageEncoding="UTF-8"%>
<%@page import="kz.bsbnb.eivk.zvt.Bean.Request1Utility"%>
<%@page import="kz.bsbnb.eivk.zvt.Bean.Request1Bean"%>
<%@page import="java.util.Calendar" %>
<%@ include file="/html/eivkzvt/init.jsp" %>

<%
  String pRequest1Num = ParamUtil.getString(request, "request1Num");
  int reportYear1 = ParamUtil.getInteger(request, "reportYear1");
  int reportMonth1 = ParamUtil.getInteger(request, "reportMonth1");
  int reportDay1 = ParamUtil.getInteger(request, "reportDay1");
  String pRequest1BIN = ParamUtil.getString(request, "request1BIN");
  String pRequest1RezID = ParamUtil.getString(request, "request1RezID");
  String pRequest1RegNum = ParamUtil.getString(request, "request1RegNum");
  String request1IsExpImp = ParamUtil.getString(request, "request1IsExpImp");
  Calendar pDateFrom = Calendar.getInstance();
  pDateFrom.set(reportYear1, reportMonth1, reportDay1);

  PortletURL portletURL = renderResponse.createRenderURL();
  portletURL.setParameter("jspPage", "/html/eivkzvt/view.jsp");
  portletURL.setParameter("backURL", portletURL.toString());
  portletURL.setParameter("request1Num", pRequest1Num);
  portletURL.setParameter("request1BIN", pRequest1BIN);
  portletURL.setParameter("request1RezID", pRequest1RezID);
  portletURL.setParameter("request1RegNum", pRequest1RegNum);
  portletURL.setParameter("request1IsExpImp", request1IsExpImp);
  portletURL.setParameter("reportYear1", String.valueOf(reportYear1));
  portletURL.setParameter("reportMonth1", String.valueOf(reportMonth1));
  portletURL.setParameter("reportDay1", String.valueOf(reportDay1));
  
  String organizationBIN = (String) renderRequest.getPortletSession().getAttribute("ORGANIZATION_BIN");
  try {
    List<Request1Bean> datas = Request1Utility.getDatas(organizationBIN, pRequest1Num, pDateFrom.getTime(), request1IsExpImp, pRequest1BIN, 
    		                                            pRequest1RezID, pRequest1RegNum);
%>
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
			className="kz.bsbnb.eivk.zvt.Bean.Request1Bean"
			keyProperty="err_code"
			modelVar="model_var"
		>
		<liferay-ui:search-container-column-text name="err_code" property="err_code"/>
		<liferay-ui:search-container-column-text name="err_msg" property="err_msg" />
		<liferay-ui:search-container-column-text name="extended_err_msg" value="<%= (model_var.getExtended_err_msg() != null)?model_var.getExtended_err_msg():"" %>" />

	</liferay-ui:search-container-row>
		<liferay-ui:search-iterator />
	</liferay-ui:search-container >

<%
  } catch(Exception e) {
	  out.print("<p calss=\"error-header\" > ОШИБКИ ПРИ ПОЛУЧЕНИИ ДАННЫХ:</p>");
	  out.print("<p class=\"show-error\">" + e.getMessage() + "</p>");
  }
%>