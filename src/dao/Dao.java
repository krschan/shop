package dao;

import java.sql.SQLException;

import model.Employee;

public interface Dao {

	void connect() throws SQLException;
	
	Employee getEmployee(int user, String pw);
	
	void disconnect() throws SQLException;
	
}
