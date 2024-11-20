package model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="product")
@XmlType(propOrder= {"name","wholesalerPrice","publicPrice", "available", "stock", "badge"})
public class Product {
	private int id;
	private String name;
	private String badge;
	private double publicPrice;
	private double wholesalerPrice;
	private boolean available;
	private int stock;
	private static int totalProducts;
	private int total;

	static double EXPIRATION_RATE = 0.60;
	
	public Product() {
		
	}

	public Product(String name, double wholesalerPrice, double publicPrice, boolean available, int stock) {
		super();
		this.id = totalProducts + 1;
		this.name = name;
		this.wholesalerPrice = wholesalerPrice;
		this.publicPrice = publicPrice;
		this.available = available;
		this.stock = stock;
		totalProducts++;
	}

	@XmlAttribute(name="id")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@XmlAttribute(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public double getPublicPrice() {
		return publicPrice;
	}

	public void setPublicPrice(double publicPrice) {
		this.publicPrice = publicPrice;
	}

	@XmlElement(name="wholesalerPrice")
	public double getWholesalerPrice() {
		return wholesalerPrice;
	}

	public void setWholesalerPrice(double wholesalerPrice) {
		this.wholesalerPrice = wholesalerPrice;
	}

	public String getBadge() {
		return badge;
	}

	public void setBadge(String badge) {
		this.badge = badge;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	@XmlElement(name="stock")
	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public static int getTotalProducts() {
		return totalProducts;
	}

	public static void setTotalProducts(int totalProducts) {
		Product.totalProducts = totalProducts;
	}

	public void expire() {
		this.publicPrice = this.getPublicPrice() * EXPIRATION_RATE;
	}
	
	@XmlAttribute(name="total")
	public int getTotal() {
		return total;
	}
	
	public void setTotal(int total) {
		this.total = total;
	}
	
	public void publicPriceCalculate() {
	    // Check if wholesalerPrice is not zero
	    if (this.wholesalerPrice > 0) {
	        this.publicPrice = this.wholesalerPrice * 2;
	    } else {
	        System.out.println("WholesalerPrice is null or not set");
	    }
	}
	@Override
	public String toString() {
		return "Product [name=" + name + ", publicPrice=" + publicPrice + ", wholesalerPrice=" + wholesalerPrice
				+ ", stock=" + stock + "]";
	}
}
