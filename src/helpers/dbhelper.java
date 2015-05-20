package helpers;

public class dbhelper {
	
	private int id;
	
	private String categoryName;
	
	private String name;
	
	private String SKU;
	
	private int price;
	
	private String state;
	
	private String product;

	public dbhelper(String product, int price, String name, String state){
		this.product = product;
		this.price = price;
		this.name = name;
		this.state = state;
	}
/**
 * @return the id
 */
public String getName1() {
	return name;
}

/**
 * @return the cid
 */
public String getCategoryName() {
	return categoryName;
}

/**
 * @return the name
 */
public String getName() {
	return name;
}

/**
 * @return the sKU
 */

public int getPrice() {
	return price;
}


}
