package kz.bsbnb.eivk.zvt;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.NamingException;

import oracle.jdbc.OracleTypes;

import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import org.apache.log4j.Logger;

public class UserChekUtility {
	private static Logger _log = Logger.getLogger(UserChekUtility.class);

	public static Integer checkUser(String userID, String userIIN, String orgBIN) {
		_log.trace("checkUser input");
		Connection conn = null;
		PreparedStatement stmt = null;
		Integer count = 0;
		try {
			conn = DataAccess.getConnection("jdbc/PortalPool");
			stmt = conn.prepareStatement("select count(1) "
					+ "from   user_kisc t , organization_kisc ok "
					+ "where t.user_id = ? and t.legals_id = ok.id "
					+ "and   t.iin = ? and ok.bin = ?");
			stmt.setString(1, userID);
			stmt.setString(2, userIIN);
			stmt.setString(3, orgBIN);
			ResultSet cursor = stmt.executeQuery();
			while (cursor.next()) {
				count = cursor.getInt(1);
//				_log.info("get cursor count:"+count);
			}
		} catch (SQLException e) {
			_log.error("SQLException:" + e.getMessage());
		}catch(NamingException ne) {
			_log.error("NamingException:" + ne.getMessage());
		} finally {
			DataAccess.cleanUp(conn, stmt);
		}
		return count;
	}
	
	public static Integer checkOrgBIN(String orgBIN){
		int isChecked = 0;
		/*
		 * 
begin
  -- Call the function
  :result := vcon.prtl_check_bank_bin(p_bin_ => :p_bin_);
end;
		 */
		Connection conn = null;
		CallableStatement stmt = null;
		try {
			conn = DataAccess.getConnection("jdbc/VCONPOOL");
			stmt = conn.prepareCall("{ ? = call vcon.prtl_check_bank_bin ( ? ) }",
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			stmt.registerOutParameter(1, OracleTypes.FLOAT);
			stmt.setString(2, orgBIN);
			stmt.execute();
			isChecked = stmt.getInt(1);
		} catch (NamingException | SQLException e) {
			_log.error(e);
		} finally{
			DataAccess.cleanUp(conn, stmt);
		}
		return isChecked;
	}
}
