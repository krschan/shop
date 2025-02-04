package model;

import dao.Dao;
import dao.DaoImplJDBC;
import main.Logable;

public class Employee extends Person implements Logable {

	// Hardcoded version to enter the shop menu.
//	private final static int USER = 123;
//	private final static String PASSWORD = "test";
//	private int employeeId = USER;

	// Connection using JDBC.
	public Dao dao = new DaoImplJDBC();

	public Employee() {
	}

	public Employee(int user, String pw) {
	}

	public boolean login(int user, String password) {
		dao.connect();
		Employee employee = dao.getEmployee(user, password);
		dao.disconnect();
		return employee != null;
	}
}
