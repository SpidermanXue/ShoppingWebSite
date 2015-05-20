package helpers;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
public class AnalyticsHelper {
	public static List<AnalyticsList> getList(String cate) throws Exception{
            Connection conn = HelperUtils.connect();
            Statement stmt = conn.createStatement();
            List<AnalyticsList> analytics = new ArrayList<AnalyticsList>(); 
            String sql = "CREATE TEMPORARY TABLE AS MyTempTable AS (SELECT s.uid AS uid, u.name AS uname, s.pid AS pid, p.name AS pname, c.id AS cid, c.name AS cname, SUM(s.quantity) AS quan, s.price AS price, m.state AS state"+
            "FROM (((categories c LEFT OUTER JOIN products p ON c.id=p.cid) LEFT OUTER JOIN sales s ON s.pid = p.id) LEFT OUTER JOIN users u ON s.uid = u.id) LEFT OUTER JOIN states m ON m.id = u.state" + 
            		"Where c.name ='" + cate + "' GROUP BY s.uid, s.pid) SELECT * FROM MyTempTable";
            //String sql2 = "CREATE TEMPORARY TABLE AS MyTempTabletwo AS SELECT * FROM (SELECT * FROM MyTempTable m GROUP BY uid) LEFT OUTER JOIN";
            ResultSet rs = stmt.executeQuery(sql);
     
            while (rs.next()){
            	Integer uid = rs.getInt(1);
                String uname = rs.getString(2);
                Integer pid = rs.getInt(3);
                String pname = rs.getString(4);
                Integer cid = rs.getInt(5);
                String cname = rs.getString(6);
            	Integer quantity = rs.getInt(7);
            	Integer price = rs.getInt(8);
            	String state = rs.getString(9);
            	analytics.add(new AnalyticsList(uid, uname, pid, pname, cid, cname, quantity, price, state));
            }
            return analytics ;
            
	}
}		
