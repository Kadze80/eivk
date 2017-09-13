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

public class Request2Utility {

private static Logger _log = Logger.getLogger(Request2Utility.class);
	
	public static List<Request2Bean> getDatas(String organizationBIN, Date pDateFrom, Date pDateTo, String pRequest2Num, String pRequest2BIN, Boolean pShowToDate)
			throws Exception {
		
		_log.info("Start Request2Utility...");
		Connection conn = null;
		CallableStatement stmt = null;
		List<Request2Bean> datas = new ArrayList<Request2Bean>();
		try {
			conn = DataAccess.getConnection(COMMON.DATABASE_POOL_NAME);
			/*
  -- Call the procedure
            READ_PRTL_EEU_MOVEMENT_WA_LIST (p_bank_bin_ => :p_bank_bin_,
                                                            p_from_date_ => :p_from_date_,
                                                            p_to_date_ => :p_to_date_,
                                                            p_declaration_number_ => :p_declaration_number_,
                                                            p_person_bin_iin_ => :p_person_bin_iin_,
                                                            cur => :cur);                       
			 */
			stmt = conn.prepareCall("{ call READ_PRTL_EEU_MOVEMENT_WA_LIST ( ?, ?, ?, ?, ?, ? ) }",
                    					ResultSet.TYPE_SCROLL_INSENSITIVE,
                    					ResultSet.CONCUR_READ_ONLY);
			
			stmt.setString(1, organizationBIN);
			if(pShowToDate){
				stmt.setDate(2, new java.sql.Date(pDateFrom.getTime()));	
				stmt.setDate(3, new java.sql.Date(pDateTo.getTime()));
			}else{
				stmt.setNull(2, OracleTypes.DATE);	
				stmt.setNull(3, OracleTypes.DATE);
			}
			stmt.setString(4, pRequest2Num);
			stmt.setString(5, pRequest2BIN);
			stmt.registerOutParameter(6, OracleTypes.CURSOR);
			_log.info("stmt.execute");
			stmt.execute();

			ResultSet cursor = (ResultSet) stmt.getObject(6);
			while (cursor.next()) {
				Request2Bean bean = new Request2Bean();
				bean.setRow(cursor.getRow());
				bean.setDECLARATION_NUMBER(cursor.getString("EEMW_DECLARATION_NUMBER"));
				bean.setDECLARATION_DATE(cursor.getString("EEMW_DECLARATION_DATE"));
				bean.setEISIGN_STR(cursor.getString("T_EISIGN_STR"));
				bean.setINVOICE_NUMBER(cursor.getString("EEMW_INVOICE_NUMBER"));
				bean.setINVOICE_DATE(cursor.getString("EEMW_INVOICE_DATE"));
				bean.setCOST_OF_GOODS(cursor.getString("EEMW_COST_OF_GOODS"));
				bean.setCURRENCY_CODE(cursor.getString("EEMW_CURRENCY_CODE"));
				bean.setDATE_OF_REGISTRATION(cursor.getString("EEMW_DATE_OF_REGISTRATION"));
				bean.setCONTRACT_NUMBER(cursor.getString("EEMW_CONTRACT_NUMBER"));
				bean.setCONTRACT_DATE(cursor.getString("EEMW_CONTRACT_DATE"));
				bean.setRESIDENT_BIN_OR_IIN(cursor.getString("EEMW_RESIDENT_BIN_OR_IIN"));
				bean.setTAX_AUTH_DATE_STAMP(cursor.getString("EEMW_TAX_AUTH_DATE_STAMP"));
				bean.setNON_RESIDENT_NAME(cursor.getString("EEMW_NON_RESIDENT_NAME"));
				bean.setNON_RESIDENT_ID(cursor.getString("EEMW_NON_RESIDENT_ID"));
				bean.setNON_RESIDENT_COUNTRY_3N(cursor.getString("EEMW_NON_RESIDENT_COUNTRY_3N"));
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