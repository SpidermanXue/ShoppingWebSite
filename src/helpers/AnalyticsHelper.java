package helpers;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jdt.internal.compiler.ast.ThrowStatement;
public class AnalyticsHelper {
	private static Boolean tableReady = false;
	private static Connection conn = null;
	private static Statement stmt = null;
	
	public static int uOffset = 0;
	public static int pOffset = 0;
	
	private static HashMap<Integer, HashMap<Integer, Integer>> hash = null;
	
	public static void buildAnalyticsHelper(int cid, boolean isTopK, boolean isStates) throws Exception{
        hash = new HashMap<Integer, HashMap<Integer, Integer>>();
        try{
            try {
                conn = HelperUtils.connect();
            } catch (Exception e) {
                System.err.println("Internal Server Error. This shouldn't happen.");
            }
            buildTop20(0,cid,isTopK,isStates);
            buildTop10(0,cid,isTopK);
            
            
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
								+ "SELECT stt.stid, stt.stname, SUM(s.quantity*s.price) as states_sales "
								+ "from (select st.id as stid, st.name as stname "
								+ "from states st order by stname ASC LIMIT '"+end+"' OFFSET '"+uOffset+"') as stt, users u, sales s "
								+ "where u.id=s.uid and u.state=stt.stid group by stt.stid, stt.stname "
								+ "order by states_sales DESC";
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
						//state, topk, with cid
						buildTop20 += "create temporary table top20 as "
								+ "SELECT stt.stid, stt.stname, SUM(s.quantity*s.price) as msum "
								+ "from (select st.id as stid, st.name as stname from states st "
								+ "order by stname ASC LIMIT '"+end+"' OFFSET '"+uOffset+"') as stt, users u, sales s "
								+ "where s.pid in ( select  id from products p "
								+ "where p.cid = '"+cid+"') and u.id=s.uid and u.state=stt.stid "
								+ "group by stt.stid, stt.stname order by msum DESC";
					}else{
						//topK with cid, customer
						buildTop20 += "CREATE TEMPORARY TABLE top20 AS "
								+ "SELECT u.id as uid, COALESCE(ss.uqsum,0) AS uqsum, COALESCE(ss.msum,0) AS msum "
								+ "from (users u left outer join (SELECT s.uid AS uid, SUM(s.quantity) AS uqsum, SUM(s.price*s.quantity) AS msum "
								+ "FROM sales s, products p WHERE p.id=s.id AND p.cid = '"+cid+"' GROUP BY s.uid) ss on u.id = ss.uid) "
								+ "ORDER BY msum DESC, uqsum DESC LIMIT '"+end+"' OFFSET '"+uOffset+"'";
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
							+ "FROM (select id, name from products order by name asc limit '"+end+"' offset '"+pOffset+"') pm "
							+ "left outer join (select pid, sum(quantity) * price as money "
							+ "from sales group by pid, price) sm on sm.pid = pm.id order by pname ASC";
				}
			}else{
				if(isTopK){
					//topk, with cid
					buildTop10 += "CREATE TEMPORARY TABLE top10 AS "
							+ "SELECT s.pid AS pid, SUM(s.quantity) AS pqsum, SUM(s.quantity * s.price) AS psum "
							+ "FROM sales s, products p WHERE p.id = s.pid AND p.cid = '"+cid+"' GROUP BY s.pid "
							+ "ORDER BY psum DESC, pqsum DESC LIMIT '"+end+"' OFFSET '"+pOffset+"'";
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
	
	public static List<AnalyticsUser> getAnalyticsUserList(boolean isStates) throws Exception{
		List<AnalyticsUser> res = new ArrayList<AnalyticsUser>();

		Statement stmt = null;
		try{
			String query = null;
			if (isStates){
				query = "SELECT * FROM top20"; 
			}
			else{
				query = "SELECT t.uid, u.name, t.msum FROM top20 t, users u WHERE t.uid=u.id";
			}
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
			System.err.println("Some error happened when getting the userlist.<br/>" + e.getLocalizedMessage());
			return new ArrayList<AnalyticsUser>();
		}
	}
	
	public static void buildUserProductDataMap(boolean isState) throws Exception{
		System.out.println("getting 200 -- inside");
		try{
			String query = null;
			if(isState){
				query = "select ak.pid as pid, ak.stid as stid, COALESCE(SUM(ak.money),0) as psum "
						+ "from (select us.pid, us.stid, SUM(s.quan) * s.price AS money "
						+ "from (SELECT * FROM (SELECT * from top20, top10) up left join users u on up.stid=u.state) us "
						+ "left join (select pid, uid, SUM(quantity) as quan, price from sales group by pid, uid, price) s "
						+ "on (us.id = s.uid and us.pid = s.pid) group by us.stid, us.pid, s.price) ak Group by ak.pid, ak.stid";
			}else{
			query = "select up.pid, up.uid, SUM(s.price*s.quantity) "
					+ "from (SELECT * from top20, top10) up left join sales s on (up.uid = s.uid and up.pid = s.pid) "
					+ "group by up.uid, up.pid";
			}
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			System.out.println(rs);
			while(rs.next()){
				int pid = rs.getInt(1);
				int uid = rs.getInt(2);
				int sum = rs.getInt(3);
				if(hash.containsKey(uid)){
					hash.get(uid).put(pid, sum);
				}else{
					hash.put(uid, new HashMap<Integer, Integer>());
					hash.get(uid).put(pid, sum);
				}
			}
			
			// test print out
			for (Integer name: hash.keySet()){
	            String key =name.toString();
	            String value = hash.get(name).toString();  
	            System.out.println(key + " " + value);  
	            } 
			
		}catch(Exception e){
			System.out.println("Error inside build 200 data.  " + e.getLocalizedMessage());
		}
	}
	
	public static int getSum(int uid, int pid) throws Exception{	
		if(hash.containsKey(uid)){
			if(hash.get(uid).containsKey(pid)){
				return hash.get(uid).get(pid);
			}
		}
		throw new Exception("error finding in hash map, fetal");
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
