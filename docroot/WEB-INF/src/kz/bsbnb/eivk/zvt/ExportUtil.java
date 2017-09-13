package kz.bsbnb.eivk.zvt;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Date;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;

import org.apache.log4j.Logger;

import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.util.ParamUtil;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

public class ExportUtil {

	private static Logger _log = Logger.getLogger(ExportUtil.class);

	private static void exportReport(JasperPrint jasperPrint, ByteArrayOutputStream outputStream) throws JRException {		
		_log.trace("ExportUtil.exportReport input");
		JRXlsxExporter exporter = new JRXlsxExporter();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
		SimpleXlsxReportConfiguration xls_config = new SimpleXlsxReportConfiguration();
		xls_config.setOnePagePerSheet(false);
		xls_config.setDetectCellType(true);
		xls_config.setWhitePageBackground(false);
		xls_config.setIgnoreGraphics(false);
		xls_config.setRemoveEmptySpaceBetweenRows(true);
		exporter.setConfiguration(xls_config);
		exporter.exportReport();
		_log.trace("ExportUtil.exportReport end");
	}

	public static ByteArrayOutputStream exportRequest(ResourceRequest resourceRequest, ResourceResponse response) throws Exception {
		_log.trace("exportRequest input");
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		String requestNum = ParamUtil.getString(resourceRequest, "pRequestNum");
		Integer requestDateDay = ParamUtil.getInteger(resourceRequest,"reportDay");
		Integer requestDateMonth = ParamUtil.getInteger(resourceRequest,"reportMonth") + 1;
		Integer requestDateYear = ParamUtil.getInteger(resourceRequest,"reportYear");
		Integer requestDateDayTo = ParamUtil.getInteger(resourceRequest,"reportDayTo");
		Integer requestDateMonthTo = ParamUtil.getInteger(resourceRequest,"reportMonthTo") + 1;
		Integer requestDateYearto = ParamUtil.getInteger(resourceRequest,"reportYearTo");
		String requestRegNum = ParamUtil.getString(resourceRequest, "pRequestRegNum");
		String requestRezID = ParamUtil.getString(resourceRequest, "pRequestRezID");
		String requestBIN = ParamUtil.getString(resourceRequest, "pRequestBIN");
		Boolean pShowToDate = ParamUtil.getBoolean(resourceRequest, "pShowToDate");

		String bin = (String) resourceRequest.getPortletSession().getAttribute("ORGANIZATION_BIN");
		
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		Date date_of_reg = df.parse(requestDateDay+"."+requestDateMonth+"."+requestDateYear);
		Date date_of_reg_to = df.parse(requestDateDayTo+"."+requestDateMonthTo+"."+requestDateYearto);

		JasperPrint jasperPrint;
		Connection conn = null;
		try {
			conn = DataAccess.getConnection(COMMON.DATABASE_POOL_NAME);
			HashMap<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_BANK_BIN_", bin);	

			if(pShowToDate){
				parameters.put("P_FROM_DATE_", new java.sql.Timestamp(date_of_reg.getTime()));
				parameters.put("P_TO_DATE_", new java.sql.Timestamp(date_of_reg_to.getTime()));
			}else{
				parameters.put("P_FROM_DATE_", null);
				parameters.put("P_TO_DATE_", null);
			}	
			parameters.put("P_PS_NUMBER_", requestNum);
			parameters.put("P_DECLARATION_NUMBER_", requestRegNum);	
			parameters.put("P_BIN_OR_IIN_", requestBIN);	
			parameters.put("P_NON_RESIDENT_ID_", requestRezID);	

			String path = resourceRequest.getPortletSession().getPortletContext().getRealPath("");
			String reportFileName = path + "\\jaspers\\RequestToExcell.jasper";
			DefaultJasperReportsContext context = DefaultJasperReportsContext.getInstance();
			JRPropertiesUtil.getInstance(context).setProperty(
					"net.sf.jasperreports.query.executer.factory.plsql",
					"com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
			_log.trace("fillReport");
			jasperPrint = JasperFillManager.fillReport(reportFileName, parameters, conn);
			exportReport(jasperPrint, outputStream);
		} catch (JRException e) {
			_log.error(e.getMessage());
			throw new Exception(e);
		} finally {
			DataAccess.cleanUp(conn);
		}
		_log.trace("exportRequest return");
		return outputStream;
	}
	
	public static ByteArrayOutputStream exportRequest2(ResourceRequest resourceRequest, ResourceResponse response) throws Exception {
		_log.trace("exportRequest2 input");
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		String requestNum = ParamUtil.getString(resourceRequest, "pRequest2Num");
		Integer requestDateDay = ParamUtil.getInteger(resourceRequest,"reportDay");
		Integer requestDateMonth = ParamUtil.getInteger(resourceRequest,"reportMonth") + 1;
		Integer requestDateYear = ParamUtil.getInteger(resourceRequest,"reportYear");
		Integer requestDateDayTo = ParamUtil.getInteger(resourceRequest,"reportDayTo");
		Integer requestDateMonthTo = ParamUtil.getInteger(resourceRequest,"reportMonthTo") + 1;
		Integer requestDateYearto = ParamUtil.getInteger(resourceRequest,"reportYearTo");
		String request2BIN = ParamUtil.getString(resourceRequest, "pRequest2BIN");
		Boolean pShowToDate = ParamUtil.getBoolean(resourceRequest, "pShowToDate");

		String bin = (String) resourceRequest.getPortletSession().getAttribute("ORGANIZATION_BIN");
		
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		Date date_of_reg = df.parse(requestDateDay+"."+requestDateMonth+"."+requestDateYear);
		Date date_of_reg_to = df.parse(requestDateDayTo+"."+requestDateMonthTo+"."+requestDateYearto);

		JasperPrint jasperPrint;
		Connection conn = null;
		try {
			conn = DataAccess.getConnection(COMMON.DATABASE_POOL_NAME);
			HashMap<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("p_bank_bin_", bin);			
			if(pShowToDate){
				parameters.put("p_from_date_", new java.sql.Timestamp(date_of_reg.getTime()));
				parameters.put("p_to_date_", new java.sql.Timestamp(date_of_reg_to.getTime()));
			}else{
				parameters.put("p_from_date_", null);
				parameters.put("p_to_date_", null);
			}	
			parameters.put("p_declaration_number_", requestNum);
			parameters.put("p_person_bin_iin_", request2BIN);		

			String path = resourceRequest.getPortletSession().getPortletContext().getRealPath("");
			String reportFileName = path + "\\jaspers\\EivkZvtPer.jasper";
			DefaultJasperReportsContext context = DefaultJasperReportsContext.getInstance();
			JRPropertiesUtil.getInstance(context).setProperty(
					"net.sf.jasperreports.query.executer.factory.plsql",
					"com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
			_log.trace("fillReport");
			jasperPrint = JasperFillManager.fillReport(reportFileName, parameters, conn);
			exportReport(jasperPrint, outputStream);
		} catch (JRException e) {
			_log.error(e.getMessage());
			throw new Exception(e);
		} finally {
			DataAccess.cleanUp(conn);
		}
		_log.trace("exportRequest2 return");
		return outputStream;
	}
	
	public static ByteArrayOutputStream exportRequest3(ResourceRequest resourceRequest, ResourceResponse response) throws Exception {
		_log.trace("exportRequest3 input");
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		String requestNum = ParamUtil.getString(resourceRequest, "pRequest1Num");
		Integer requestDateDay = ParamUtil.getInteger(resourceRequest,"reportDay");
		Integer requestDateMonth = ParamUtil.getInteger(resourceRequest,"reportMonth") + 1;
		Integer requestDateYear = ParamUtil.getInteger(resourceRequest,"reportYear");
		Boolean pShowToDate = ParamUtil.getBoolean(resourceRequest, "pShowRegDate");
		String bin = (String) resourceRequest.getPortletSession().getAttribute("ORGANIZATION_BIN");
		
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		Date date_of_reg = df.parse(requestDateDay+"."+requestDateMonth+"."+requestDateYear);
		_log.trace("exportRequest3 date_of_reg "+requestDateDay+"."+requestDateMonth+"."+requestDateYear);
		_log.trace("exportRequest3 requestNum "+requestNum);
		_log.trace("exportRequest3 bin "+bin);
		JasperPrint jasperPrint;
		Connection conn = null;
		try {
			conn = DataAccess.getConnection(COMMON.DATABASE_POOL_NAME);
			HashMap<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_BANK_BIN_", bin);
			parameters.put("P_DECLARATION_NUMBER_", requestNum);
			if(pShowToDate){
				parameters.put("P_DECLARATION_DATE_", new java.sql.Timestamp(date_of_reg.getTime()));
			}else{
				parameters.put("P_DECLARATION_DATE_", null);
			}			
			String path = resourceRequest.getPortletSession().getPortletContext().getRealPath("");
			String reportFileName = path + "\\jaspers\\EivkZvtChg.jasper";
			DefaultJasperReportsContext context = DefaultJasperReportsContext.getInstance();
			JRPropertiesUtil.getInstance(context).setProperty(
					"net.sf.jasperreports.query.executer.factory.plsql",
					"com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
			_log.trace("fillReport");
			jasperPrint = JasperFillManager.fillReport(reportFileName, parameters, conn);
			exportReport(jasperPrint, outputStream);
		} catch (JRException e) {
			_log.error(e.getMessage());
			throw new Exception(e);
		} finally {
			DataAccess.cleanUp(conn);
		}
		_log.trace("exportRequest3 return");
		return outputStream;
	}
	
	public static ByteArrayOutputStream exportRequest4(ResourceRequest resourceRequest, ResourceResponse response) throws Exception {
		_log.trace("exportRequest4 input");
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		String requestNum = ParamUtil.getString(resourceRequest, "pRequest1Num");
		Integer requestDateDay = ParamUtil.getInteger(resourceRequest,"reportDay");
		Integer requestDateMonth = ParamUtil.getInteger(resourceRequest,"reportMonth") + 1;
		Integer requestDateYear = ParamUtil.getInteger(resourceRequest,"reportYear");
		Boolean pShowToDate = ParamUtil.getBoolean(resourceRequest, "pShowRegDate");
		String bin = (String) resourceRequest.getPortletSession().getAttribute("ORGANIZATION_BIN");
		
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		Date date_of_reg = df.parse(requestDateDay+"."+requestDateMonth+"."+requestDateYear);
		_log.trace("exportRequest4 bin "+bin);
		_log.trace("exportRequest4 date_of_reg "+date_of_reg);
		_log.trace("exportRequest4 requestNum "+requestNum);
		_log.trace("exportRequest4 pShowToDate "+pShowToDate);
		
		JasperPrint jasperPrint;
		Connection conn = null;
		try {
			conn = DataAccess.getConnection(COMMON.DATABASE_POOL_NAME);
			HashMap<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("P_BANK_BIN_", bin);
			parameters.put("P_DECLARATION_NUMBER_", requestNum);
			if(pShowToDate){
				parameters.put("P_DECLARATION_DATE_", new java.sql.Timestamp(date_of_reg.getTime()));
			}else{
				parameters.put("P_DECLARATION_DATE_", null);
			}			
			String path = resourceRequest.getPortletSession().getPortletContext().getRealPath("");
			String reportFileName = path + "\\jaspers\\EivkZvt.jasper";
			DefaultJasperReportsContext context = DefaultJasperReportsContext.getInstance();
			JRPropertiesUtil.getInstance(context).setProperty(
					"net.sf.jasperreports.query.executer.factory.plsql",
					"com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
			_log.trace("fillReport");
			jasperPrint = JasperFillManager.fillReport(reportFileName, parameters, conn);
			exportReport(jasperPrint, outputStream);
		} catch (JRException e) {
			_log.error(e.getMessage());
			throw new Exception(e);
		} finally {
			DataAccess.cleanUp(conn);
		}
		_log.trace("exportRequest4 return");
		return outputStream;
	}


}
