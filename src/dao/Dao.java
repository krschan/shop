package dao;

import java.sql.Statement;
import java.util.ArrayList;

import model.Employee;
import model.Product;

public interface Dao {

	void connect();

	Employee getEmployee(int user, String pw);

	void disconnect();

	ArrayList<Product> getInventory();

	boolean writeInventory(ArrayList<Product> inventory);
	
	void insert(Product product);
	
	void update(String id);
	
	void delete(String id);
	
	boolean exists(int id, Statement stmt);

}