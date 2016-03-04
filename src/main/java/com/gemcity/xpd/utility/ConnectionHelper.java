package com.gemcity.xpd.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionHelper
{
	private static String url;
	private static ConnectionHelper instance;

	private ConnectionHelper()
	{    	
    	String driver = "com.progress.sql.jdbc.JdbcProgressDriver";
    	try {
    		System.out.println("asdfasfd");
			Class.forName(driver).newInstance();			
	    	url = "jdbc:jdbcprogress:T:192.168.74.20:6100:mfgsys";			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() throws SQLException {
		if (instance == null) {
			instance = new ConnectionHelper();
		}
		try {			
			String username = "sysprogress";
			String password = "aprilfools";						
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