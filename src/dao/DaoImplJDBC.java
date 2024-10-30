package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Employee;
import model.Product;
import main.Shop;

public class DaoImplJDBC implements Dao {
	
	private Connection connection;

	@Override
	public void connect() {
		try {
			// Define connection parameters
			String url = "jdbc:mysql://localhost:3306/shopSQL";
			String user = "root";
			String password = ""; 
			this.connection = DriverManager.getConnection(url, user, password);
			return;		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Employee getEmployee(int user, String pw) {
		Employee employee = null;
		// prepare query
		String query = "select * from employee where employeeId = ? AND password = ? ";
		
		try (PreparedStatement ps = connection.prepareStatement(query)) { 
			// set id to search for
			ps.setInt(1,user);
			ps.setString(2, pw);
		  	//System.out.println(ps.toString());
	        try (ResultSet rs = ps.executeQuery()) {
	        	if (rs.next()) {
	        		employee =  new Employee(user, pw);            		            				
	        	}
	        }
	    } catch (SQLException e) {
			// in case error in SQL
			e.printStackTrace();
		}
		return employee;
	}

	@Override
	public void disconnect() {
		try {
			if (connection != null) {
				connection.close();
			}		
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public ArrayList<Product> getInventory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean writeInventory(ArrayList<Product> inventory) {
		// TODO Auto-generated method stub
		return false;
	}

}
