package helpers;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
public class AnalyticsHelper {
	private static Boolean tableReady = false;
	private static Connection conn = null;
	private static Statement stmt = null;
	
	public static int uOffset = 0;
	public static int pOffset = 0;
	//public static int cid = 0;
	public static boolean buttonClicked = false;
	
	public static void buildAnalyticsHelper(int cid) throws Exception{
        try{
            try {
                conn = HelperUtils.connect();
            } catch (Exception e) {
                System.err.println("Internal Server Error. This shouldn't happen.");
            }
            String buildTempTable = "CREATE TEMPORARY TABLE tempsale AS "
            		+ "SELECT s.uid AS uid, s.pid AS ppid, SUM(quantity) as quan, ((SUM(quantity)) * price) "
            		+ "AS sum FROM SALES s GROUP BY s.pid, s.uid, s.price ORDER BY sum DESC, pid ASC";
            stmt = conn.createStatement();
            stmt.executeUpdate(buildTempTable);
            
            buildTop20(0);
            buildTop10(0,0);
            
            System.out.println(tableReady);
        }catch (Exception e) {
            System.err.println("Some error happened! build<br/>" + e.getLocalizedMessage());
        }  
	}
	
	public static void buildTop20(int uOffset) throws Exception{
		int end = uOffset + 20;
		try{
			String buildTop20 = "CREATE TEMPORARY TABLE top20 AS "
            		+ "SELECT t.uid AS uid, SUM(quan) AS qsum, SUM(sum) AS msum "
            		+ "FROM tempsale t GROUP BY t.uid ORDER BY msum DESC LIMIT '"+end+"' OFFSET '"+uOffset+"'";            
            stmt = conn.createStatement();
            stmt.executeUpdate(buildTop20);
		}catch(Exception e){
            System.err.println("Some error happened! build20<br/>" + e.getLocalizedMessage());
		}
	}
	
	public static void buildTop10(int pOffset, int cid) throws Exception{
		int end = pOffset + 10;
		String buildTop10 = null;
		try{
			if(cid==0){
				buildTop10 = "CREATE TEMPORARY TABLE top10 AS "
            		+ "SELECT p.name AS pname, p.id AS pid, SUM(s.quantity * s.price) AS psum "
            		+ "FROM products p, sales s Where s.pid = p.id GROUP BY s.pid, p.name, p.id "
            		+ "ORDER BY psum DESC LIMIT '"+end+"' OFFSET '"+pOffset+"';";
			}else{
				buildTop10 = "CREATE TEMPORARY TABLE top10 AS "
	            		+ "SELECT p.name AS pname, p.id AS pid, SUM(s.quantity * s.price) AS psum "
	            		+ "FROM products p, sales s Where s.pid = p.id AND p.cid = '"+cid+"' GROUP BY s.pid, p.name, p.id "
	            		+ "ORDER BY psum DESC LIMIT '"+pOffset+"' OFFSET '"+pOffset+"';";
			}
            stmt = conn.createStatement();
            stmt.executeUpdate(buildTop10);
		}catch(Exception e){
            System.err.println("Some error happened! build10<br/>" + e.getLocalizedMessage());
		}
	}
	
	public static void buildStates(int uOffset, int cid) throws Exception{
		int end = pOffset + 10;
		String buildStates = "";
		try{
			stmt = conn.createStatement();
			stmt.executeQuery(buildStates);
		}catch(Exception e){
			
		}
	}
	public static void changeAnalyticsTable(int newpOffset, int newuOffset, int pid, boolean isTopK, boolean isState){
		if (pOffset != newpOffset){
			
		}
	}
	
	public static List<AnalyticsProduct> getAnalyticsProductList() throws Exception{
		List<AnalyticsProduct> res = new ArrayList<AnalyticsProduct>();
		Statement stmt = null;
		try{
			if(tableReady){
				throw new Exception("fuckedup");
			}
			String query = "SELECT * FROM top10";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()){
				AnalyticsProduct ap = new AnalyticsProduct();
				ap.pname = rs.getString(1);
				ap.pid = rs.getInt(2);
				ap.sum = rs.getInt(3);
				res.add(ap);
				System.out.println(ap.pname + "\n");
			}
			return res;
		}catch(Exception e){
			System.err.println("Some error happened when getting the title. getProduct 10<br/>" + e.getLocalizedMessage());
			return new ArrayList<AnalyticsProduct>();
		}
	}
	
	public static List<AanalyticsUser> getAnalyticsUserList() throws Exception{
		List<AanalyticsUser> res = new ArrayList<AanalyticsUser>();
		Statement stmt = null;
		try{

			String query = "SELECT t.uid, t.qsum, t.msum, u.name FROM top20 t, users u WHERE t.uid=u.id;";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()){
				AanalyticsUser au = new AanalyticsUser();
				au.uid = rs.getInt(1);
				au.sum = rs.getInt(3);
				au.uname = rs.getString(4);
				res.add(au);
				System.out.println(au.uname + "\n");
			}
			return res;
		}catch(Exception e){
			System.err.println("Some error happened when getting the title.<br/>" + e.getLocalizedMessage());
			return new ArrayList<AanalyticsUser>();
		}
	}
	
//	public int getUserDataForProduct(int uid, int pid) throws Exception{
//		
//		try{
//			String query = "SELECT sum from tempsale t WHERE t.uid = 1 and t.ppid = 1;";
//			stmt = conn.createStatement();
//			ResultSet rs = stmt.executeQuery(query);
//			if(rs.next()){
//			int res = rs.getInt(1);
//			return res;
//			}
//			return 0;
//		}catch (Exception e){
//			System.err.println("Error happened getting product sale for user");
//			return -1;
//		}
//	}
	
	public static List<List<Integer>> getUserProductDataList() throws Exception{
		List<List<Integer>> res = new ArrayList<List<Integer>>(20);
		try{
			String query = "select ts.sum, tc.pid, tc.uid, tc.msum, tc.psum "
					+ "from (SELECT * from top20, top10) tc "
					+ "left join tempsale ts on (tc.uid = ts.uid and tc.pid = ts.ppid) "
					+ "order by psum DESC, msum DESC";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			int counter = 0;
			List<Integer> row = new ArrayList<Integer>(10);
			while(rs.next()){
				if(counter==9){
					counter = 0;
					res.add(row);
					row = new ArrayList<Integer>(10);
				}
					row.add(rs.getInt(1));
					counter++;
			}
			return res;
		}catch(Exception e){
			return res;
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
