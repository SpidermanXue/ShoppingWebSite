package helpers;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
public class AnalyticsHelper {
	private final static String tempTableName = "MyTempTable";
	private static Boolean tableReady = false;
	private static Connection conn = null;
	private static Statement stmt = null;
	
	public AnalyticsHelper(int cid) throws Exception{
        try{
            try {
                conn = HelperUtils.connect();
            } catch (Exception e) {
                System.err.println("Internal Server Error. This shouldn't happen.");
            }
            String buildTempTable = "CREATE TEMPORARY TABLE MyTempTable AS "
            		+ "(SELECT s.uid AS uid, u.name AS uname, s.pid AS pid, p.name AS pname, "
            		+ "c.id AS cid, c.name AS cname, SUM(s.quantity) AS quan, s.price AS price, "
            		+ "m.name AS state FROM (((categories c LEFT OUTER JOIN products p ON c.id=p.cid) "
            		+ "LEFT OUTER JOIN sales s ON s.pid = p.id) LEFT OUTER JOIN users u ON s.uid = u.id) "
            		+ "LEFT OUTER JOIN states m ON m.id = u.state Where c.id = '" + cid + "' "
            		+ "GROUP BY s.uid, s.pid, u.name, p.name, c.id, s.price, m.name);";
            stmt = conn.createStatement();
            stmt.executeQuery(buildTempTable);
            System.out.println(tableReady);
        }catch (Exception e) {
            System.err.println("Some error happened!<br/>" + e.getLocalizedMessage());
        }  
	}
	
	public static List<AnalyticsTitle> getTitleList() throws Exception{
		List<AnalyticsTitle> res = new ArrayList<AnalyticsTitle>();
		Statement stmt = null;
		try{
			if(tableReady){
				throw new Exception("fuckedup");
			}
			String query = "SELECT DISTINCT pid , pname, (SUM(quan)) * price AS SUM "
					+ "FROM MyTempTable GROUP BY pname, price, pid ORDER BY SUM DESC LIMIT 10";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()){
				AnalyticsTitle at = new AnalyticsTitle();
				at.pid = rs.getInt(1);
				at.pname = rs.getString(2);
				at.sum = rs.getInt(3);
				res.add(at);
				System.out.println(at.pname + "\n");
			}
			return res;
		}catch(Exception e){
			System.err.println("Some error happened when getting the title.<br/>" + e.getLocalizedMessage());
			return new ArrayList<AnalyticsTitle>();
		}
	}
	
	
	
	public static void closeAll(){
		try {
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
}		
