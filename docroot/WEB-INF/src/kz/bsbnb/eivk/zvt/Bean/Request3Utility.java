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

public class Request3Utility {
	
private static Logger _log = Logger.getLogger(Request3Utility.class);
	
	public static List<Request3Bean> getDatas(String organizationBIN, String pRequestNum, Date DateReg, Boolean pShowRegDate) throws Exception {
		
		_log.info("Start Request3Utility...");
		Connection conn = null;
		CallableStatement stmt = null;
		List<Request3Bean> datas = new ArrayList<Request3Bean>();
		try {
			conn = DataAccess.getConnection(COMMON.DATABASE_POOL_NAME);
			/*
  -- Call the procedure
  PROCEDURE READ_PRTL_EEU_MW_CHNG_LIST(P_BANK_BIN_                    IN VCON.BANK.BIN%TYPE, --  Ó‰ ¡»Õ ”¡
                                       P_DECLARATION_NUMBER_          IN VCON.EIVK_EEU_MOVEMENT_WARE.EEMW_DECLARATION_NUMBER%TYPE, -- ÕÓÏÂ «¬“
                                       P_DECLARATION_DATE_            IN VCON.EIVK_EEU_MOVEMENT_WARE.EEMW_DECLARATION_DATE%TYPE, -- ƒ‡Ú‡ «¬“
                                       CUR                            OUT SYS_REFCURSOR);                  
			 */
			stmt = conn.prepareCall("{ call READ_PRTL_EEU_MW_CHNG_LIST ( ?, ?, ?, ? ) }",
                    					ResultSet.TYPE_SCROLL_INSENSITIVE,
                    					ResultSet.CONCUR_READ_ONLY);			
			stmt.setString(1, organizationBIN);
			stmt.setString(2, pRequestNum);
			if (pShowRegDate){
				stmt.setDate(3, new java.sql.Date(DateReg.getTime()));	
			}else{
				stmt.setNull(3, OracleTypes.DATE);
			}			
			stmt.registerOutParameter(4, OracleTypes.CURSOR);
			_log.info("stmt.execute");
			stmt.execute();

			ResultSet cursor = (ResultSet) stmt.getObject(4);
			while (cursor.next()) {
				Request3Bean bean = new Request3Bean();
				bean.setRow(cursor.getRow());
				bean.setEEMWC_DECLARATION_NUMBER(cursor.getString("EEMWC_DECLARATION_NUMBER"));
				bean.setEEMWC_DECLARATION_DATE(cursor.getString("EEMWC_DECLARATION_DATE"));
				bean.setEEMWS_NAME(cursor.getString("EEMWS_NAME"));
				bean.setEEMWC_NEW_DECLARATION_NUMBER(cursor.getString("EEMWC_NEW_DECLARATION_NUMBER"));
				bean.setEEMWC_NEW_DECLARATION_DATE(cursor.getString("EEMWC_NEW_DECLARATION_DATE"));
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

