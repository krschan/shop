package model;

import main.Logable;

public class Employee extends Person implements Logable{
	
	private final static int USER = 123;
	private final static String PASSWORD = "test";
	
	private int employeeId = USER;

    public boolean login(int user, String password) {
        return user == USER && password.equals(PASSWORD);
    }
}
