package dao;

import java.util.ArrayList;

import model.Employee;
import model.Product;

public interface Dao {

	void connect();

	Employee getEmployee(int user, String pw);

	void disconnect();

	ArrayList<Product> getInventory();

	boolean writeInventory(ArrayList<Product> inventory);
	
	void updateProduct(Product product);
	
	void addProduct(Product product);
	
	void deleteProduct(Product product);

}