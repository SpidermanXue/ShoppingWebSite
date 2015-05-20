/**
 * 
 */
package helpers;

/**
 * @author Jules Testard
 *
 */
public class AnalyticsList {
	
	private int uid;
	private String uname;
	
	private int pid;
	private String pname;
	
	private int cid;
	private String cname;
	
	private int quan;
	private int price;
	private String state;
	

	/**
	 * @param id
	 * @param cid
	 * @param name
	 * @param sKU
	 * @param price
	 */

	public AnalyticsList(int uid, String uname, int pid, String pname, int cid, String cname, int quan, int price, String state) {
		this.uid = uid;
		this.uname = uname;
		this.pid = pid;
		this.pname = pname;
		this.cid = cid;
		this.cname = cname;
		this.quan = quan;
		this.price = price;
		this.state = state; 
	}

	/**
	 * @return the id
	 */
	public int getUid() {
		return uid;
	}

	public String getUname(){
		return uname;
	}
	
	public int getPid(){
		return pid;
	}
	
	public String getPname(){
		return pname;
	}
	
	public int getCid(){
		return cid;
	}
	
	public String getCname(){
		return cname;
	}
	
	public int getQuan(){
		return quan;
	}
	
	public int getPrice(){
		return price;
	}
	
	public String getState(){
		return state;
	}
	
}
