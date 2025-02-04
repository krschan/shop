package model;

import java.util.List;
import java.util.ArrayList;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@Entity
@Table(name = "inventory")

@XmlRootElement(name = "product")
@XmlType(propOrder = { "id", "name", "available", "wholesalerPrice", "publicPrice", "stock" })
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = true)
	private int id;

	@Column
	private boolean available;

	@Column
	private String name;

	@Column
	private double price;

	@Transient
	private Amount publicPrice;

	@Transient
	private Amount wholesalerPrice;

	@Column
	private int stock;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<ProductHistory> history = new ArrayList<>();

	private static int totalProducts;

	static double EXPIRATION_RATE = 0.60;

	// Empty constructor is needed.
	public Product() {
	}

	// Default constructor.
	public Product(String name, double wholesalerPrice, double publicPrice, boolean available, int stock) {
		super();
		this.id = totalProducts + 1;
		this.name = name;
		this.wholesalerPrice = new Amount(wholesalerPrice);
		this.publicPrice = new Amount(publicPrice);
		this.available = available;
		this.stock = stock;
		totalProducts++;
	}

	// Default constructor for Hibernate.
	public Product(boolean available, String name, double price, int stock) {
		super();
		this.id = totalProducts + 1;
		this.available = available;
		this.name = name;
		this.price = price;
		this.wholesalerPrice = new Amount(price);
		this.publicPrice = new Amount(price * 2);
		this.stock = stock;
		totalProducts++;
	}

	// JDBC.
	public Product(int id, String name, double wholesalerPrice, boolean available, int stock) {
		super();
		this.id = id;
		this.name = name;
		this.wholesalerPrice = new Amount(wholesalerPrice);
		this.publicPrice = new Amount(wholesalerPrice * 2);
		this.available = available;
		this.stock = stock;
	}

	// Hibernate.
	public Product(int id, boolean available, String name, double price, int stock) {
		super();
		this.id = id;
		this.available = available;
		this.name = name;
		this.price = price;
		this.stock = stock;
	}

	// Getters and setters.
	@XmlAttribute(name = "id")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@XmlAttribute(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@XmlElement(name = "publicPrice")
	public Amount getPublicPrice() {
		return publicPrice;
	}

	public void setPublicPrice(Amount publicPrice) {
		this.publicPrice = publicPrice;
	}

	@XmlElement(name = "wholesalerPrice")
	public Amount getWholesalerPrice() {
		return wholesalerPrice;
	}

	public void setWholesalerPrice(Amount wholesalerPrice) {
		this.wholesalerPrice = wholesalerPrice;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	@XmlElement(name = "stock")
	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public static int getTotalProducts() {
		return totalProducts;
	}

	public void setTotalProducts(int totalProducts) {
		Product.totalProducts = totalProducts;
	}

	public List<ProductHistory> getHistory() {
		return history;
	}

	public void setHistory(List<ProductHistory> history) {
		this.history = history;
	}

	// Method to set the expiration date.
	public void expire() {
		double valueExpire = this.getPublicPrice().getValue() * EXPIRATION_RATE;
		this.publicPrice.setValue(valueExpire);
	}

	// Method to calculate price public.
	public void publicPriceCalculate() {
		// Check if wholesalerPrice is not zero
		if (this.wholesalerPrice != null) {
			this.publicPrice = new Amount(this.wholesalerPrice.getValue() * 2);
		} else {
			System.out.println("WholesalerPrice is null or not set");
		}
	}

	// Method to add history.
	public void addHistory(ProductHistory productHistory) {
		history.add(productHistory);
		productHistory.setProduct(this);
	}

	@Override
	public String toString() {
		return "Product [name=" + name + ", publicPrice=" + publicPrice + ", wholesalerPrice=" + wholesalerPrice
				+ ", stock=" + stock + "]";
	}
}
