package dao;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Employee;
import model.Product;

public interface Dao {

	void connect();

	Employee getEmployee(int user, String pw);

	void disconnect();

	ArrayList<Product> getInventory();

	boolean writeInventory(ArrayList<Product> inventory);

}