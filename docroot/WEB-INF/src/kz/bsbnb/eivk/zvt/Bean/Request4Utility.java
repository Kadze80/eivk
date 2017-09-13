package kz.bsbnb.eivk.zvt.Bean;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kz.bsbnb.eivk.zvt.COMMON;
import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;

import com.liferay.portal.kernel.dao.jdbc.DataAccess;

public class Request4Utility {
	
private static Logger _log = Logger.getLogger(Request4Utility.class);
	
	public static List<Request4Bean> getDatas(String organizationBIN, String requestNum, Date DateReg, Boolean pShowRegDate) throws Exception {
		
		_log.info("Start Request4Utility...");
		Connection conn = null;
		CallableStatement stmt = null;
		List<Request4Bean> datas = new ArrayList<Request4Bean>();
		try {
			conn = DataAccess.getConnection(COMMON.DATABASE_POOL_NAME);
			/*
  -- Call the procedure
  PROCEDURE READ_PRTL_EEU_MW_HIS_LIST (P_BANK_BIN_                   IN VCON.BANK.BIN%TYPE, --  Ó‰ ¡»Õ ”¡
                                      P_DECLARATION_NUMBER_          IN VCON.EIVK_EEU_MOVEMENT_WARE.EEMW_DECLARATION_NUMBER%TYPE, -- ÕÓÏÂ «¬“
                                      P_DECLARATION_DATE_            IN VCON.EIVK_EEU_MOVEMENT_WARE.EEMW_DECLARATION_DATE%TYPE, -- ƒ‡Ú‡ «¬“
                                      CUR                            OUT SYS_REFCURSOR);                     
			 */
			stmt = conn.prepareCall("{ call READ_PRTL_EEU_MW_HIS_LIST  ( ?, ?, ?, ? ) }",
                    					ResultSet.TYPE_SCROLL_INSENSITIVE,
                    					ResultSet.CONCUR_READ_ONLY);
			
			stmt.setString(1, organizationBIN);
			stmt.setString(2, requestNum);
			if (pShowRegDate){
				stmt.setDate(3, new java.sql.Date(DateReg.getTime()));	
			}else{
				stmt.setNull(3, OracleTypes.DATE);
			}	
			stmt.registerOutParameter(4, OracleTypes.CURSOR);
			stmt.execute();

			ResultSet cursor = (ResultSet) stmt.getObject(4);			
			while (cursor.next()) {
				Request4Bean bean = new Request4Bean();				
				bean.setRow(cursor.getRow());				
				bean.setEEMW_DECLARATION_NUMBER(cursor.getString("EEMW_DECLARATION_NUMBER"));
				bean.setEEMW_DECLARATION_DATE(cursor.getString("EEMW_DECLARATION_DATE"));				
				bean.setT_eisign_str(cursor.getString("t_eisign_str"));				
				bean.setEEMW_INVOICE_NUMBER(cursor.getString("EEMW_INVOICE_NUMBER"));				
				bean.setEEMW_INVOICE_DATE(cursor.getString("EEMW_INVOICE_DATE"));				
				bean.setEEMW_COST_OF_GOODS(cursor.getString("EEMW_COST_OF_GOODS"));
				bean.setEEMW_CURRENCY_CODE(cursor.getString("EEMW_CURRENCY_CODE"));
				bean.setEEMW_DATE_OF_REGISTRATION(cursor.getString("EEMW_DATE_OF_REGISTRATION"));
				bean.setEEMW_CONTRACT_NUMBER(cursor.getString("EEMW_CONTRACT_NUMBER"));
				bean.setEEMW_CONTRACT_DATE(cursor.getString("EEMW_CONTRACT_DATE"));
				bean.setEEMW_RESIDENT_BIN_OR_IIN(cursor.getString("EEMW_RESIDENT_BIN_OR_IIN"));
				bean.setEEMW_TAX_AUTH_DATE_STAMP(cursor.getString("EEMW_TAX_AUTH_DATE_STAMP"));
				bean.setEEMW_NON_RESIDENT_NAME(cursor.getString("EEMW_NON_RESIDENT_NAME"));
				bean.setEEMW_NON_RESIDENT_ID(cursor.getString("EEMW_NON_RESIDENT_ID"));
				bean.setEEMW_NON_RESIDENT_COUNTRY_3N(cursor.getString("EEMW_NON_RESIDENT_COUNTRY_3N"));
				bean.setEEMWS_NAME(cursor.getString("EEMWS_NAME"));
				bean.setEEMWC_NEW_DECLARATION_NUMBER(cursor.getString("EEMWC_NEW_DECLARATION_NUMBER"));
				bean.setEEMWC_NEW_DECLARATION_DATE(cursor.getString("EEMWC_NEW_DECLARATION_DATE"));
				
				bean.setGECR_NAME(cursor.getString("GECR_NAME"));
				bean.setGEC_NAME(cursor.getString("GEC_NAME"));
				bean.setEEMWS_CODE(cursor.getString("EEMWS_CODE"));
				
				datas.add(bean);
			}
            
		} catch(Exception e) {
			_log.error(e);
			throw e;
	    } finally {
			DataAccess.cleanUp(conn, stmt);
		}
		return datas;
	}
}


