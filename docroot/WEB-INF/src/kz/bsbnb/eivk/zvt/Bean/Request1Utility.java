package kz.bsbnb.eivk.zvt.Bean;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kz.bsbnb.eivk.zvt.COMMON;
import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;

import com.liferay.portal.kernel.dao.jdbc.DataAccess;

public class Request1Utility {
	
	private static Logger _log = Logger.getLogger(Request1Utility.class);
	
	public static List<Request1Bean> getDatas(String organizationBIN, String pRequest1Num, Date pDateFrom, String pRequest1IsExpImp, String pRequest1BIN, String pRequest1RezID, String pRequest1RegNum)
			throws Exception {
		
		Connection conn = null;
		CallableStatement stmt = null;
		List<Request1Bean> datas = new ArrayList<Request1Bean>();
		try {
			conn = DataAccess.getConnection(COMMON.DATABASE_POOL_NAME);
			/*
  -- Создать запрос на получение ЗВТ от КГД МФ РК из веб - интерфейса портала
  PROCEDURE CREATE_PRTL_EEU_MW_REQUEST(P_BANK_BIN_                    IN VCON.BANK.BIN%TYPE, -- Код БИН УБ из ЭЦП
                                       P_PS_NUMBER_                   IN VCON.EIVK_EEU_MOVEMENT_WARE_REQ.EEMWR_PS_NUMBER%TYPE, -- Номер УНК
                                       P_PS_DATE_                     IN VCON.EIVK_EEU_MOVEMENT_WARE_REQ.EEMWR_PS_DATE%TYPE, -- Дата учетной регистрации
                                       P_EI_SIGN_                     IN VCON.EIVK_EEU_MOVEMENT_WARE_REQ.EEMWR_EI_SIGN%TYPE, -- Признак экспорт/импорт (1- экспорт, 2 - импорт)
                                       P_BIN_OR_IIN_                  IN VCON.EIVK_EEU_MOVEMENT_WARE_REQ.EEMWR_BIN_OR_IIN%TYPE, -- Код БИН/ИИН экспортера/импортера - резидента
                                       P_NON_RESIDENT_ID_             IN VCON.EIVK_EEU_MOVEMENT_WARE_REQ.EEMWR_NON_RESIDENT_ID%TYPE, -- Код нерезидента
                                       P_DECLARATION_NUMBER_          IN VCON.EIVK_EEU_MOVEMENT_WARE_REQ.EEMWR_DECLARATION_NUMBER%TYPE, -- Номер декларации
                                       ERR_CODE                       OUT NUMBER,
                                       ERR_MSG                        OUT VARCHAR2,
                                       EXTENDED_ERR_MSG               OUT VARCHAR2);

			 */
			stmt = conn.prepareCall("{ call CREATE_PRTL_EEU_MW_REQUEST ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) }",
                    					ResultSet.TYPE_SCROLL_INSENSITIVE,
                    					ResultSet.CONCUR_READ_ONLY);
			stmt.setString(1, organizationBIN);
			stmt.setString(2, pRequest1Num);
			stmt.setDate(3, new java.sql.Date(pDateFrom.getTime()));	
			stmt.setString(4, pRequest1IsExpImp);
			stmt.setString(5, pRequest1BIN);
			stmt.setString(6, pRequest1RezID);
			stmt.setString(7, pRequest1RegNum);
			stmt.registerOutParameter(8, OracleTypes.NUMBER);
			stmt.registerOutParameter(9, OracleTypes.VARCHAR);
			stmt.registerOutParameter(10, OracleTypes.VARCHAR);
			stmt.execute();

			Request1Bean bean = new Request1Bean();
            bean.setErr_code(stmt.getInt(8));
            bean.setErr_msg(stmt.getString(9));
            bean.setExtended_err_msg(stmt.getString(10));      
            datas.add(bean);
            
		} catch(Exception e) {
			_log.error(e);
			throw e;
	    } finally {
			DataAccess.cleanUp(conn, stmt);
		}
		return datas;
	}
}

