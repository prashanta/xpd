package com.gemcity.xpd.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHelperMySql
{
	private static String url;
	private static ConnectionHelperMySql instance;

	private ConnectionHelperMySql()
	{    	
    	String driver = "com.mysql.jdbc.Driver";
    	try {
    		Class.forName(driver).newInstance();			
	    	url = "jdbc:mysql://localhost/xpddb";			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() throws SQLException {
		if (instance == null) {
			System.out.println("The instance was null");
			instance = new ConnectionHelperMySql();
		}
		else
			System.out.println("The instance was NOT null");
		try {			
			String username = "root";
			String password = "root";						
			Connection c = DriverManager.getConnection(url, username, password);			
			c.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			return c;
		} catch (SQLException e) {
			throw e;
		}
	}
	
	public static void close(Connection connection)
	{
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}