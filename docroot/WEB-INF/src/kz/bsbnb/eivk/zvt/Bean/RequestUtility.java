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

public class RequestUtility {
private static Logger _log = Logger.getLogger(RequestUtility.class);
	
	public static List<RequestBean> getDatas(String organizationBIN, Date pDateFrom, Date pDateTo, String pRequestNum, String pRequestRegNum, String pRequestRezID, String pRequestBIN, Boolean pShowToDate)
			throws Exception {
		
		_log.info("Start RequestUtility...");
		Connection conn = null;
		CallableStatement stmt = null;
		List<RequestBean> datas = new ArrayList<RequestBean>();
		try {
			conn = DataAccess.getConnection(COMMON.DATABASE_POOL_NAME);
			/*
  -- Call the procedure
  -- Просмотре информации о запросах 
  PROCEDURE READ_PRTL_EEU_MW_REQ_LIST (P_BANK_BIN_                    IN VCON.BANK.BIN%TYPE, -- Код БИН УБ
                                      P_FROM_DATE_                   IN VCON.OUTGOING_WS.OW_SEND_DATE_TIME%TYPE, -- За период - с
                                      P_TO_DATE_                     IN VCON.OUTGOING_WS.OW_SEND_DATE_TIME%TYPE, -- За период - по
                                      P_EEMWR_PS_NUMBER_             IN VCON.EIVK_EEU_MOVEMENT_WARE_REQ.EEMWR_PS_NUMBER%TYPE, -- По УНК
                                      P_DECLARATION_NUMBER_          IN VCON.EIVK_EEU_MOVEMENT_WARE_REQ.EEMWR_DECLARATION_NUMBER%TYPE, -- Номер ЗВТ
                                      P_EEMWR_BIN_OR_IIN_            IN VCON.EIVK_EEU_MOVEMENT_WARE_REQ.EEMWR_BIN_OR_IIN%TYPE, -- БИН/ИИН резидента
                                      P_EEMWR_NON_RESIDENT_ID_       IN VCON.EIVK_EEU_MOVEMENT_WARE_REQ.EEMWR_NON_RESIDENT_ID%TYPE, -- Идентификатор нерезидента
                                      CUR                            OUT SYS_REFCURSOR);
                  
			 */
			stmt = conn.prepareCall("{ call READ_PRTL_EEU_MW_REQ_LIST ( ?, ?, ?, ?, ?, ?, ?, ? ) }",
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
			stmt.setString(4, pRequestNum);
			stmt.setString(5, pRequestRegNum);
			stmt.setString(6, pRequestBIN);
			stmt.setString(7, pRequestRezID);
			stmt.registerOutParameter(8, OracleTypes.CURSOR);
			_log.info("stmt.execute");
			stmt.execute();

			ResultSet cursor = (ResultSet) stmt.getObject(8);
			while (cursor.next()) {
				RequestBean bean = new RequestBean();
				bean.setRow(cursor.getRow());
				bean.setEEMWR_PS_NUMBER(cursor.getString("EEMWR_PS_NUMBER"));
				bean.setEEMWR_PS_DATE(cursor.getString("EEMWR_PS_DATE"));
				bean.setEEMWR_EISIGN_STR(cursor.getString("EEMWR_EISIGN_STR"));
				bean.setEEMWR_BIN_OR_IIN(cursor.getString("EEMWR_BIN_OR_IIN"));
				bean.setEEMWR_NON_RESIDENT_ID(cursor.getString("EEMWR_NON_RESIDENT_ID"));
				bean.setEEMWR_DECLARATION_NUMBER(cursor.getString("EEMWR_DECLARATION_NUMBER"));
				bean.setWSPS_NAME(cursor.getString("WSPS_NAME"));
				bean.setOW_SEND_DATE_TIME(cursor.getString("OW_SEND_DATE_TIME"));
				bean.setOW_ERROR_CODE(cursor.getString("OW_ERROR_CODE"));
				bean.setOW_ERROR_MESSAGE(cursor.getString("OW_ERROR_MESSAGE"));
				bean.setOW_REFERENCE_NUMBER(cursor.getString("OW_REFERENCE_NUMBER"));
				bean.setOW_RESPONSE_ERROR_CODE(cursor.getString("OW_RESPONSE_ERROR_CODE"));
				bean.setOW_RESPONSE_ERROR_MESSAGE(cursor.getString("OW_RESPONSE_ERROR_MESSAGE"));
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