package model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="products")
public class ProductList {
	private int total;
	
	private ArrayList<Product> products = new ArrayList<>();  // Initialize products	
	  
	
	public void setProducts(ArrayList<Product> products) {
		this.products = products;
	}

	public ProductList() {};	
	
	public ProductList(ArrayList<Product> products) {
		this.products = products;
	}

	@XmlElement(name="product")
	public ArrayList<Product> getProducts() {
		return products;
	}
	
	@XmlAttribute(name="total")
	public int getTotal() {
		return total;
	}
	
	public void setTotal(int total) {
		this.total = total;
	}
	
}
