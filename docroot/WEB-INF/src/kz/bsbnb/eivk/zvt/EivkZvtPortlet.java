package kz.bsbnb.eivk.zvt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.ProcessAction;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.security.cert.CertificateException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kz.gamma.asn1.ASN1InputStream;
import kz.gamma.asn1.DEROctetString;
import kz.gamma.asn1.DERSequence;
import kz.gamma.asn1.DERTaggedObject;
import kz.gamma.cms.Pkcs7Data;
import kz.gamma.jce.X509Principal;
import kz.gamma.util.encoders.Base64;
import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;

public class EivkZvtPortlet extends GenericPortlet {

	public void init() {
		
	}

	public void doView(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException {
		
		PropertyConfigurator.configure(renderRequest.getPortletSession().getPortletContext().getRealPath("") + path);
		String jspPage = ParamUtil.getString(renderRequest, "jspPage", "/html/eivkzvt/ecpAuthorize.jsp");

		if (renderRequest.getRemoteUser() == null) {
			jspPage = "/html/needAuth.jsp";
		}
		_log.debug("jspPage " + jspPage);
		include(jspPage, renderRequest, renderResponse);
	}

	protected void include(String path, RenderRequest renderRequest,
			RenderResponse renderResponse) throws IOException, PortletException {

		PortletRequestDispatcher portletRequestDispatcher = getPortletContext()
				.getRequestDispatcher(path);

		if (portletRequestDispatcher == null) {
			_log.error(path + " is not a valid include");
		} else {
			portletRequestDispatcher.include(renderRequest, renderResponse);
		}
	}

	private X509Certificate convertToX509Certificate(String pem) throws CertificateException, IOException {
		
		CertificateFactory certFactory = null;
		X509Certificate cert = null;
		String cerHead = "-----BEGIN CERTIFICATE-----\n";
		String cerEnd = "\n-----END CERTIFICATE-----";
		String cert_str = cerHead.concat(pem).concat(cerEnd);

		try {
			certFactory = CertificateFactory.getInstance("X.509");
			InputStream in = new ByteArrayInputStream(cert_str.getBytes());
			cert = (X509Certificate) certFactory.generateCertificate(in);
		} catch (java.security.cert.CertificateException e) {
			_log.error(e);
		}
		return cert;		
	}

	private static boolean checkCert(X509Certificate p_cert, ActionRequest request, ActionResponse response) {
		
		try {
			p_cert.checkValidity(new Date());
		} catch (CertificateExpiredException ce) {
			_log.debug(ce);
			SessionErrors.add(request, "CertificateExpired");
			return false;
		} catch (CertificateNotYetValidException cnyve) {
			_log.debug(cnyve);
			SessionErrors.add(request, "CertificateNotYetValid");
			return false;
		} catch (Exception e) {
			_log.debug(e);
			SessionErrors.add(request, "getCertificateError");
			response.setRenderParameter("error", e.getMessage());
			return false;
		}
		return true;
	}

	@ProcessAction(name = "CHECK_SIGN")
	public void CHECK_SIGN(ActionRequest request, ActionResponse response) throws PortletException, IOException, SQLException {

		_log.trace("CHECK_SIGN input");
		String certificate = ParamUtil.getString(request, "frmPKCS7");
		String userID = request.getRemoteUser();
		String userScreenName = null;
		try {
			userScreenName = UserLocalServiceUtil.getUser(Long.parseLong(userID)).getScreenName();
			_log.info(userScreenName);
		} catch (Exception e) {
			_log.debug(e);
		}
		if (userScreenName.equals("admin")) {
			String bin = ParamUtil.getString(request, "bin", "1");
			try {
				request.getPortletSession().setAttribute("ORGANIZATION_BIN",bin);
			} catch (Exception e) {
				_log.info(e);
				return;
			}
			response.setRenderParameter("jspPage", "/html/eivkzvt/view.jsp");
		} else {
			if (certificate != null) {
				try {
					X509Certificate cer_ = convertToX509Certificate(certificate);
					if (checkCert(cer_, request, response)) {

						String[] data = getBinIinFromCertificate(cer_);
						if (data == null) {
							_log.info("getDataError");
							return;
						}
						String bin = null;
						if (data.length > 1) {
							bin = data[1];
							try {
								request.getPortletSession().setAttribute("ORGANIZATION_BIN", bin);
								response.setRenderParameter("jspPage","/html/eivkzvt/view.jsp");
							} catch (Exception e) {
								_log.info("BIN Not Found");
								return;
							}
						} else {
							_log.info("getBINError");
							return;
						}
					}
				} catch (Exception e) {
					_log.trace("error on certificate convert");
				}
			} else {
				_log.info("Certificate Is Null");
			}
		}
		_log.trace("CHECK_SIGN end");
	}

	public String[] getBinIinFromCertificate(X509Certificate userCertificate) {
		
		_log.trace("getBinIinFromCertificate input");
		String[] data = null;
		ASN1InputStream extensionStream = null;
		try {
			byte[] extensionBytes = userCertificate.getExtensionValue("2.5.29.17");
			if (extensionBytes != null) {
				extensionStream = new ASN1InputStream(extensionBytes);
				DEROctetString octetString = (DEROctetString) extensionStream.readObject();
				extensionStream.close();
				extensionStream = new ASN1InputStream(octetString.getOctets());
				DERSequence sequence = (DERSequence) extensionStream.readObject();
				extensionStream.close();
				Enumeration subjectAltNames = sequence.getObjects();
				int counter = 0;
				String str1 = null;
				String str2 = null;
				while (subjectAltNames.hasMoreElements()) {
					DERTaggedObject nextElement = (DERTaggedObject) subjectAltNames.nextElement();
					X509Principal x509Principal = new X509Principal(nextElement.getObject().getEncoded());
					if (counter == 1) {
						String extensionData = x509Principal.toString();
						int index_of_equal = extensionData.lastIndexOf("=");
						str1 = extensionData.substring(index_of_equal + 1);
					} else if (counter == 2) {
						String extensionData = x509Principal.toString();
						int index_of_equal = extensionData.lastIndexOf("=");
						str2 = extensionData.substring(index_of_equal + 1);
					}
					counter++;
				}
				_log.trace("get iin bin");
				if (str2 != null && str1 != null) { // est' i bin i iin
					_log.debug("str1 :" + str1);
					_log.debug("str2 :" + str2);
					data = new String[2];
					data[0] = str2;
					data[1] = str1;
				} else if (str1 != null) { // tol'ko iin
					_log.debug("str1 :" + str1);
					data = new String[1];
					data[0] = str1;
				}
			}
		} catch (Exception e) {
			_log.error(e);
		} finally {
			if (extensionStream != null) {
				try {
					extensionStream.close();
				} catch (IOException e) {
					_log.error(e);
				}
			}
		}
		_log.trace("getBinIinFromCertificate return");
		return data;
	}

	@Override
	public void serveResource(ResourceRequest request, ResourceResponse response) throws PortletException, IOException {
		try {
		_log.trace("serveResource");
		String reportType = ParamUtil.getString(request, "reportType");
		ByteArrayOutputStream  out = null;
		String fileName = null;
		
		switch(reportType){
		  case "Request" : out =  ExportUtil.exportRequest(request, response);
			 fileName = "Request.xls";
         break;
		  case "Request2" : out =  ExportUtil.exportRequest2(request, response);
			 fileName = "Request2.xls";
           break;
		  case "Request3" : out =  ExportUtil.exportRequest3(request, response);
			 fileName = "Request3.xls";
           break;
		  case "Request4" : out =  ExportUtil.exportRequest4(request, response);
			 fileName = "Request4.xls";
           break;
		}
		if (fileName == null) throw new Exception("Can not export file");
		byte[] bytes = out.toByteArray();
		out.close();

		HttpServletRequest _request = PortalUtil.getHttpServletRequest(request);
		HttpServletResponse _response = PortalUtil.getHttpServletResponse(response);
		_log.debug("send file:");
		ServletResponseUtil.sendFile(_request, _response, fileName, bytes, ContentTypes.APPLICATION_VND_MS_EXCEL);
	} catch (Exception e) {
		response.setContentType("text/plain");
		response.getWriter().write("");
		_log.error(e,e);
	}
	_log.trace("serveResource end");
	}

	private static String getKCMROCSPURL() {
		_log.trace("getKCMROCSPURL input");
		Connection conn = null;
		Statement stmt = null;
		String result = null;
		try {
			_log.debug("get Connection");
			conn = DataAccess.getConnection("jdbc/PortalPool");
			stmt = conn.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT CONST_VALUE FROM CONST_CONFIG WHERE CONST_NAME = 'KCMR_OCSP_REQUEST_ADDRESS'");
			while (rs.next()) {
				result = rs.getString("CONST_VALUE");
			}
		} catch (Exception e) {
			_log.error(e);
		} finally {
			DataAccess.cleanUp(conn, stmt);
		}
		_log.trace("getKCMROCSPURL return");
		_log.debug("result = " + result);
		return result;
	}

	private static Logger _log = Logger.getLogger(EivkZvtPortlet.class);
	static final String path = "/WEB-INF/classes/log4j.properties"; // ../WEB-INF/classes/

}
