package model;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Sale extends Object {
	private Client client;
	private ArrayList<Product> products = new ArrayList<>();
	private double amount;
	private static double totalSale;
	private LocalDateTime date;
	
	
	public Sale(String client, ArrayList<Product> products, double amount, LocalDateTime date) {
		super();
		this.client = new Client (client);
		this.products = products;
		this.amount = amount;
		this.date = date;
	}
	
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	public ArrayList<Product> getProducts() {
		return products;
	}
	public void setProducts(ArrayList<Product> products) {
		this.products = products;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public LocalDateTime getDate() {
		return date;
	}

	@Override
	public String toString() {
		DateTimeFormatter formatDateTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		String formatDate = date.format(formatDateTime);
		
		return "Sale [Client=" + client + ", Date= " + formatDate + ", Products=" + products + ", Amount=" + amount + "]";
	}
	
	
	
	

}
