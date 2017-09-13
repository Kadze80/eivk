<%@page pageEncoding="UTF-8"%>
<%@page import="com.liferay.portal.service.UserLocalServiceUtil"%>
<%@ include file="/html/init.jsp" %>
<portlet:defineObjects />
<applet width="0" height="0"
     codebase="."
     code="kz.gamma.TumarCSP"
     archive="<%= renderRequest.getContextPath()%>/lib/commons-logging.jar,<%= renderRequest.getContextPath()%>/lib/xmlsec-1.3.0.jar,<%= renderRequest.getContextPath()%>/lib/crypto.gammaprov.jar,<%= renderRequest.getContextPath()%>/lib/crypto-common.jar,<%= renderRequest.getContextPath()%>/lib/sign-applet.jar"
     type="application/x-java-applet"
     mayscript="true"
     id="testApp" name="testApp">
    <param name="code" value="kz.gamma.TumarCSP">
    <param name="archive" value="<%= renderRequest.getContextPath()%>/lib/commons-logging.jar,<%= renderRequest.getContextPath()%>/lib/xmlsec-1.3.0.jar,<%= renderRequest.getContextPath()%>/lib/crypto.gammaprov.jar,<%= renderRequest.getContextPath()%>/lib/crypto-common.jar,<%= renderRequest.getContextPath()%>/lib/sign-applet.jar">
    <param name="mayscript" value="true">
    <param name="scriptable" value="true">
    <param name="language" value="ru">
    <param name="separate_jvm" value="true">
	JAVA PLUGIN NOT WORK
    </applet>
    
<link rel="stylesheet" type="text/css" href="<%= renderResponse.encodeURL(renderRequest.getContextPath() + "/css/TumarCSP.css")%>" >
<liferay-ui:error key="CertificateExpired" message="CertificateExpired">ExpiredError</liferay-ui:error>
<liferay-ui:error key="CertificateNotYetValid" message="CertificateNotYetValid"/>
<liferay-ui:error key="getBINError" message="getBINError"/>
<liferay-ui:error key="getCertificateError" message="<%=ParamUtil.getString(request, "error"," ") %>"/>
<liferay-ui:error key="CertificateIsNull" message="CertificateIsNull"/>
<liferay-ui:error key="checkUserinPortalError" message="checkUserinPortalError"/>
<liferay-ui:error key="checkOrgBINError" message="checkOrgBINError"/>
<liferay-ui:error key="OCSPRequestError" message="<%=ParamUtil.getString(request, "error"," ") %>"/>
<liferay-ui:error key="getDataError" message="getDataError"/>
<liferay-ui:error key="AgencyNotFound" message="AgencyNotFound"/>

<input type="Password" id="passAutofill" style="display:none" />
<div class="certContainer">
<div class="certRow">
<label><span><liferay-ui:message key="select-profile"/></span><br><select id="certProfiles" onchange="changeProfiles()"> </select></label>
</div>
<div class="certRow">
<label><span><liferay-ui:message key="enter-password"/></span><br><input type="Password" id="profilePassword" value=""/></label>
</div>
<div class="certRow">
<label><span><liferay-ui:message key="select-sertificate"/></span><br><select id="certificates"></select>
</label>
</div>
<br>
<div class="certBtnRow">
<button type="submit" form="frmAuthID" onClick="return sendCertificate();"> <liferay-ui:message key="Authorize"/></button>
</label>
</div>
</div>
<portlet:actionURL var="CHECK_SIGN_URL" name="CHECK_SIGN">
</portlet:actionURL>
<form method="post" action="<%=CHECK_SIGN_URL %>" name="frmAuth" id="frmAuthID">
<input name="frmPKCS7" id="frmPKCS7" hidden/>
<br>
<c:if test="<%= UserLocalServiceUtil.getUser(Long.parseLong(request.getRemoteUser())).getScreenName().equalsIgnoreCase("admin")  %>">
<aui:input name="bin" type="text" value="951140000151"></aui:input>
</c:if>
</form>
<script type="text/javascript">
	console.log("logs");
	
    function AppletIsReady() {
      console.log("app is ready");
    }
	
	var vcertProfiles = document.getElementById("certProfiles");

	function InitApplet(){
	  console.log("==");
	  console.log(testApp.getLastError());
	  console.log("==");
      var profiles = testApp.getProfileNames("|").split("|");
    
	  if (profiles.length == 0) { 
   	    console.log("applet error"+testApp.getLastError()); 
	    return;
	  } 
	  console.log("applet ok");

	  for(var i = 0; i < profiles.length; i++){
        vcertProfiles.options.add(new Option(profiles[i], profiles[i]));
	  }

	  changeProfiles();
	}
	
	function changeProfiles(){
      console.log("certchange");
	  var vProfile = vcertProfiles.value;
	  var vProfilePassword = document.getElementById("profilePassword").value;
	  var vCertificatesSelect = document.getElementById("certificates");
	  vCertificatesSelect.innerHTML = "";
	  
	  var vCertificatesInfo = testApp.getCertificatesInfo(vProfile, vProfilePassword, 0, "", true, false, "|");
	  if (vCertificatesInfo == null) { 
	    alert(testApp.getLastError());
	    return;
	  }
	  
	  var vCertificates = vCertificatesInfo.split("|");

	  for(var i = 0; i < vCertificates.length; i++){
	    vCertificatesSelect.options.add(new Option(vCertificates[i], vCertificates[i]));
      }
	}
	
	function getPKCS7(data){
      var vProfile = vcertProfiles.value;
	  var vProfilePassword = document.getElementById("profilePassword").value;
	  var vSelectedCertificate = document.getElementById("certificates").value;
	  console.log(vProfile + "|"+vSelectedCertificate);
	  var algOid = "1.3.6.1.4.1.6801.1.5.8";
	  var result = null;
	  if (vProfile != null && vProfile != "" && vSelectedCertificate != null && vSelectedCertificate != "") {
	    result = testApp.createPKCS7(data, 0, null, vSelectedCertificate, true, vProfile, vProfilePassword, algOid, true);
	    if(result == null){
          alert(testApp.getLastError());
        }
	  } else {
	    alert("Profile or certificate not selected");
	    return false;
	  }
	  return result;
	}
	
	
	function signText(){
	  var sData = document.getElementById("tPKCS7ID").value;
	  var pkcs7_data = getPKCS7(sData);
	  if (pkcs7_data == null) {
	    alert(getKeyError);
	    return;
	  }
	  document.getElementById("signedTextID").value = pkcs7_data;
	}
	
	function getCertificate(){
	  var vProfile = vcertProfiles.value;
	  var vProfilePassword = document.getElementById("profilePassword").value;
	  var vSelectedCertificate = document.getElementById("certificates").value;
	  console.log(vProfile + "|"+vSelectedCertificate);
	  var algOid = "1.3.6.1.4.1.6801.1.5.8";
	  var userCert = null;
	  if (vProfile != null && vProfile != "" && vSelectedCertificate != null && vSelectedCertificate != "") {
	    userCert = testApp.getCertificate(vProfile, vProfilePassword, vSelectedCertificate, true,algOid);
	    if(userCert == null){
          alert(testApp.getLastError());
        }
	  } else {
	    alert("Profile or certificate not selected");
	  }
	  return userCert;
	}
	
	function sendCertificate(){
	  var userCert = getCertificate();
      document.getElementById("frmPKCS7").value = userCert;
	  return true;
	}
	
    function sigXML(xmlData){
	  var vProfile = vcertProfiles.value;
	  var vProfilePassword = document.getElementById("profilePassword").value;
	  var vSelectedCertificate = document.getElementById("certificates").value;
	  console.log(vProfile + "|"+vSelectedCertificate);
	  var algOid = "1.3.6.1.4.1.6801.1.5.8";
	  var signXml = null;
	  if (vProfile != null && vProfile != "" && vSelectedCertificate != null && vSelectedCertificate != "") {
	    signXml = testApp.signXmlDS(xmlData, vSelectedCertificate, vProfile, vProfilePassword, algOid);
	    if(signXml == null){
          alert(testApp.getLastError());
        }
	  } else {
	    alert("Profile or certificate not selected");
	  }
	  return signXml;
	}
	
	function verifyXML(signedXML){
	  var result = false;
	  if(signedXML != null && signedXML != ""){
	    result = testApp.verifyXmlDS(xmlDs);
	  } else {
	    alert("Нет данных для проверки");
	  }
	  return result.toString();
	}
	
	function verifyPKCS7Data(data, signedData){
	  var result = false;
	  if(data != null && data != "" && signedData != null && signedData != "") {
	    result = testApp.verifyPKCS7(data, signedData, null);
	    if (result == null){
  	      alert(testApp.getLastError());
	      result = false;
	    }
	  } else {
	    alert("Вы не ввели данные или подписанные данные не корректны");
	  }
	  return result.toString();
	}
	
	function verifyClick(){
	  document.getElementById("identifier").textContent = verifyPKCS7Data(document.getElementById("tPKCS7ID").value,document.getElementById("signedTextID").value);
	}
	InitApplet();
  
	</script>