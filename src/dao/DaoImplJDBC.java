package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Employee;
import model.Person;
import model.Product;

public class DaoImplJDBC implements Dao {
	
	public DaoImplJDBC() {
		connect();
	}

	private Connection connection;

	public static final String GET_EMPLOYEE = "select * from employee";
	public static final String GET_INVENTORY = "select * from inventory";

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
	    // Prepare query.
	    String query = GET_EMPLOYEE + " WHERE employeeId = ? AND password = ?";

	    try (PreparedStatement ps = connection.prepareStatement(query)) {
	        // Set parameters for employeeId and password.
	        ps.setInt(1, user);
	        ps.setString(2, pw);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                employee = new Employee(user, pw);
	            }
	        }
	    } catch (SQLException e) {
	        // Handle SQL error.
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
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public ArrayList<Product> getInventory() {
		ArrayList<Product> product = new ArrayList<>();
		try (Statement ps = connection.createStatement()) {
			try (ResultSet rs = ps.executeQuery(GET_INVENTORY)) {
				// For each result add to list.
				while (rs.next()) {
					// Get data for each product from column.
					product.add(new Product(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getBoolean(4),
							rs.getInt(5)));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return product;
	}

	@Override
	public boolean writeInventory(ArrayList<Product> inventory) {
		return true;
	}

	@Override
	public void insert(Product product) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean exists(int id, Statement stmt) {
		// TODO Auto-generated method stub
		return false;
	}

}
