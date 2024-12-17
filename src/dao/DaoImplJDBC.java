package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Employee;
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
		String insertQuery = "INSERT INTO historical_inventory (id_product, name, wholesalerPrice, available, stock, created_at) VALUES (?, ?, ?, ?, ?, ?)";

		try (PreparedStatement ps = connection.prepareStatement(insertQuery)) {
			int totalUpdated = 0;
			for (Product product : inventory) {
				ps.setInt(1, product.getId());
				ps.setString(2, product.getName());
				ps.setDouble(3, product.getWholesalerPrice().getValue());
				ps.setBoolean(4, product.isAvailable());
				ps.setInt(5, product.getStock());
				ps.setTimestamp(6, new java.sql.Timestamp(System.currentTimeMillis()));

				int rowsUpdated = ps.executeUpdate();
				totalUpdated += rowsUpdated;
			}

			return totalUpdated == inventory.size();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void addProduct(Product product) {
		String insertQuery = "INSERT INTO inventory (name, wholesalerPrice, available, stock) VALUES (?, ?, ?, ?)";

		try (PreparedStatement ps = connection.prepareStatement(insertQuery)) {
			ps.setString(1, product.getName());
			ps.setDouble(2, product.getWholesalerPrice().getValue());
			ps.setBoolean(3, product.isAvailable());
			ps.setInt(4, product.getStock());

			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateProduct(Product product) {
		String updateQuery = "UPDATE inventory SET stock = ? WHERE id = ?";

		try (PreparedStatement ps = connection.prepareStatement(updateQuery)) {
			;
			ps.setInt(1, product.getStock());
			ps.setInt(2, product.getId());

			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteProduct(Product product) {
		String deleteQuery = "DELETE FROM inventory WHERE id = ?";

		try (PreparedStatement ps = connection.prepareStatement(deleteQuery)) {
			ps.setInt(1, product.getId());

			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
