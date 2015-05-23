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
	//public static boolean buttonClicked = false;
	
	public static void buildAnalyticsHelper(int cid, boolean isTopK, boolean isStates) throws Exception{
        try{
            try {
                conn = HelperUtils.connect();
            } catch (Exception e) {
                System.err.println("Internal Server Error. This shouldn't happen.");
            }
            
            buildTop20(0,0,true,false);
            buildTop10(0,0,true);
            
            System.out.println(tableReady);
        }catch (Exception e) {
            System.err.println("Some error happened! build<br/>" + e.getLocalizedMessage());
        }  
	}
	
	public static void buildTop20(int uOffset, int cid, boolean isTopK, boolean isState) throws Exception{
		int end = uOffset + 20;
		String buildTop20 = "drop table if exists top20;";
		try{
			if(cid==0){
				if(isTopK){
					if(isState){
						//top20 states, topK, without cid
						buildTop20 += "create temporary table top20 as "
								+ "SELECT stt.stid, stt.stname, SUM(s.quantity*s.price) as msum "
								+ "from (select st.id as stid, st.name as stname from states st "
								+ "order by stname ASC LIMIT '"+end+"' OFFSET '"+uOffset+"') as stt, users u, sales s "
								+ "where u.id=s.uid and u.state=stt.stid group by stt.stid, stt.stname order by states_sales DESC";
					}else{
						//build top20 users table with selection of topK
						buildTop20 += "CREATE TEMPORARY TABLE top20 AS "
								+ "SELECT s.uid AS uid, SUM(s.quantity) AS uqsum, SUM(price*quantity) AS msum "
								+ "FROM sales s GROUP BY s.uid ORDER BY msum DESC, uqsum DESC LIMIT '"+end+"' OFFSET '"+uOffset+"'";
					}
				}else{
					if(isState){
						//states, topK, without cid
						buildTop20 += "create temporary table top20 as "
								+ "select stm.stid as stid, stm.stname as stname, SUM(COALESCE(sm.money,0)) as stsum "
								+ "FROM (select s.id as stid, s.name as stname from users u, states s where s.id = u.id "
								+ "order by stname asc LIMIT '"+end+"' OFFSET '"+uOffset+"') stm "
								+ "left outer join (select st.id as stid, (sum(quantity) * price) as money "
								+ "FROM sales s, states st, users u WHERE u. id = s.uid and u.state = st.id "
								+ "group by stid, pid, price) sm on sm.stid = stm.stid group by stm.stid, stm.stname order by stname";
					}else{
						//Alphabetical
						buildTop20 += "create temporary table top20 as "
						+ "select um.id as uid, um.name as uname, SUM(COALESCE(sm.money,0)) as usum "
						+ "FROM (select id, name from users order by name asc limit '"+end+"' offset '"+uOffset+"') um "
						+ "left outer join (select uid, (sum(quantity) * price) as money "
						+ "FROM sales group by uid, pid, price) sm on sm.uid = um.id Group by um.id, um.name ORDER BY uname asc";
				
					}
				}
			}else{
				if(isTopK){
					if(isState){
					//topk with cid
					buildTop20 += "CREATE TEMPORARY TABLE top20 AS "
							+ "SELECT u.id as uid, COALESCE(ss.uqsum,0) AS uqsum, COALESCE(ss.msum,0) AS msum "
							+ "from (users u left outer join (SELECT s.uid AS uid, SUM(s.quantity) AS uqsum, SUM(s.price*s.quantity) AS msum "
							+ "FROM sales s, products p WHERE p.id=s.id AND p.cid = '"+cid+"' GROUP BY s.uid) ss on u.id = ss.uid) "
							+ "ORDER BY msum DESC, uqsum DESC LIMIT '"+end+"' OFFSET '"+uOffset+"'";
					}else{
						//topK with cid, state
						buildTop20 += "create temporary table top20 as "
								+ "SELECT stt.stid, stt.stname, SUM(s.quantity*s.price) as msum "
								+ "from (select st.id as stid, st.name as stname from states st "
								+ "order by stname ASC LIMIT '"+end+"' OFFSET '"+uOffset+"') as stt, users u, sales s "
								+ "where s.pid in ( select  id from products p "
								+ "where p.cid = '"+cid+"') and u.id=s.uid and u.state=stt.stid "
								+ "group by stt.stid, stt.stname order by states_sales DESC";
					}
				}else{
					if(isState){
						//states, alphabetical, with cid
						buildTop20 += "create temporary table top20 as "
								+ "SELECT stt.stid, stt.stname, SUM(s.quantity*s.price) "
								+ "from (select st.id as stid, st.name as stname from states st "
								+ "order by stname ASC LIMIT '"+end+"' OFFSET '"+uOffset+"') as stt, users u, sales s "
								+ "where s.pid in ( select  id from products p where p.cid = '"+cid+"') and u.id=s.uid and u.state=stt.stid "
								+ "group by stt.stid, stt.stname order by stt.stname";
					}else{
					//Alphabetical user, topK, with cid
					buildTop20 += "create temporary table top20 as "
							+ "select um.id as uid, um.name as uname, SUM(COALESCE(sm.money,0)) as usum "
							+ "FROM (select id, name from users order by name asc limit '"+end+"' offset '"+uOffset+"') um "
							+ "left outer join (select uid, (sum(quantity) * price) as money FROM sales s "
							+ "where s.pid in ( select  id from products p where p.cid = '"+cid+"') group by uid, pid, price) sm "
							+ "on sm.uid = um.id Group by um.id, um.name ORDER BY uname asc";

					}
				}
			}
            stmt = conn.createStatement();
            stmt.executeUpdate(buildTop20);
		}catch(Exception e){
            System.err.println("Some error happened! build20<br/>" + e.getLocalizedMessage());
		}
	}
	
	public static void buildTop10(int pOffset, int cid, boolean isTopK) throws Exception{
		int end = pOffset + 10;
		String buildTop10 = "drop table if exists top10;";
		try{
			if(cid==0){
				if(isTopK){
					//topk, no cid
					buildTop10 += "CREATE TEMPORARY TABLE top10 AS "
							+ "SELECT s.pid AS pid, SUM(s.quantity) AS pqsum, SUM(s.quantity * s.price) AS psum "
							+ "FROM sales s GROUP BY s.pid ORDER BY psum DESC, pqsum DESC LIMIT '"+end+"' OFFSET '"+pOffset+"'";	
				}else{
					//alphabetical, no cid
					buildTop10 += "create temporary table top10 as "
							+ "select pm.id as pid, pm.name as pname, COALESCE(sm.money,0) as psum "
							+ "FROM (select id, name from products order by name asc limit 10 offset 0) pm "
							+ "left outer join (select pid, sum(quantity) * price as money "
							+ "from sales group by pid, price) sm on sm.pid = pm.id order by pname ASC '"+end+"' OFFSET '"+pOffset+"'";
				}
			}else{
				if(isTopK){
					//topk, with cid
				buildTop10 += "CREATE TEMPORARY TABLE top10 AS "
						+ "SELECT s.pid AS pid, SUM(s.quantity) AS pqsum, SUM(s.quantity * s.price) AS psum "
						+ "FROM sales s, products p WHERE p.id = s.pid AND p.cid = '"+cid+"' GROUP BY s.pid "
						+ "ORDER BY psum DESC, pqsum DESC LIMIT 10 OFFSET 0'"+pOffset+"' OFFSET '"+pOffset+"';";
				}else{
					//alphabetical, no cid
					buildTop10 += "create temporary table top10 as select pm.id as pid, pm.name as pname, COALESCE(sm.money,0) as psum "
							+ "FROM (select id, name from products order by name asc limit '"+end+"' offset '"+pOffset+"') pm "
							+ "left outer join (select pid, sum(quantity) * price as money from sales group by pid, price) sm "
							+ "on sm.pid = pm.id order by pname ASC";
				}
			}
            stmt = conn.createStatement();
            stmt.executeUpdate(buildTop10);
		}catch(Exception e){
            System.err.println("Some error happened! build10<br/>" + e.getLocalizedMessage());
		}
	}
	
	public static List<AnalyticsProduct> getAnalyticsProductList() throws Exception{
		List<AnalyticsProduct> res = new ArrayList<AnalyticsProduct>();
		Statement stmt = null;
		try{
			if(tableReady){
				throw new Exception("fuckedup");
			}
			String query = "SELECT t.pid, p.name, t.psum FROM top10 t, products p WHERE t.pid=p.id";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()){
				AnalyticsProduct ap = new AnalyticsProduct();
				ap.pid = rs.getInt(1);
				ap.pname = rs.getString(2);
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
	
	public static List<AnalyticsUser> getAnalyticsUserList() throws Exception{
		List<AnalyticsUser> res = new ArrayList<AnalyticsUser>();
		Statement stmt = null;
		try{

			String query = "SELECT t.uid, u.name, t.msum FROM top20 t, users u WHERE t.uid=u.id";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()){
				AnalyticsUser au = new AnalyticsUser();
				au.uid = rs.getInt(1);
				au.uname = rs.getString(2);
				au.sum = rs.getInt(3);
				res.add(au);
				System.out.println(au.uname + "\n");
			}
			System.out.println("size of the user list: " + res.size());
			return res;
		}catch(Exception e){
			System.err.println("Some error happened when getting the title.<br/>" + e.getLocalizedMessage());
			return new ArrayList<AnalyticsUser>();
		}
	}
	
	public static List<List<Integer>> getUserProductDataList(int rowMax, int colMax) throws Exception{
		
		List<List<Integer>> res = new ArrayList<List<Integer>>(rowMax);
		try{
			String query = "select up.pid, up.pqsum, up.uid, up.uqsum, up.msum, up.psum "
					+ "from (SELECT * from top20, top10) up "
					+ "left join sales s on (up.uid = s.uid and up.pid = s.pid)";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			int counter = 0;
			List<Integer> row = new ArrayList<Integer>(colMax);
			while(rs.next()){
				if(counter==colMax){
					counter = 0;
					res.add(row);
					row = new ArrayList<Integer>(colMax);
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
