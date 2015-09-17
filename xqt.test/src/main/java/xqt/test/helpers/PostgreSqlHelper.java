package xqt.test.helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgreSqlHelper {
	Connection connection = null;
	public ResultSet execute(String connectionString, String username, String password, String query){        
        try {
            Class.forName("org.postgresql.Driver");
            //DriverManager.registerDriver(new org.postgresql.Driver());
        } catch (Exception e) {
            System.out.println(e.getClass().getName()+ ": " + e.getMessage()); // chenge to better exceptions
            return null;
        }        
        try {
            connection = DriverManager.getConnection(connectionString, username, password);
        } catch (SQLException e) {
                System.out.println("Connection Failed! Check output console");
                return null;
        }
        if (connection != null) {
            try{
                try (Statement stmt = connection.createStatement()) {
                    ResultSet rs = stmt.executeQuery(query);
                    connection.close();      
                    return rs;
                }
            } catch (SQLException ex){
                System.out.println("Failed to obtain table schema! " + ex.getMessage());
                return null;
            }
        } else {
                System.out.println("Failed to make a connection!");
                return null;
        }
	}

	public long executeScalar(String connectionString, String username, String password, String query){        
        try {
            Class.forName("org.postgresql.Driver");
            //DriverManager.registerDriver(new org.postgresql.Driver());
        } catch (Exception e) {
            System.out.println(e.getClass().getName()+ ": " + e.getMessage()); // chenge to better exceptions
            return 0;
        }        
        try {
            connection = DriverManager.getConnection(connectionString, username, password);
        } catch (SQLException e) {
                System.out.println("Connection Failed! Check output console");
                return 0;
        }
        if (connection != null) {
            try{
                try (Statement stmt = connection.createStatement()) {
                    long affectedRows = stmt.executeLargeUpdate(query);
                    connection.close();      
                    return affectedRows;
                }
            } catch (SQLException ex){
                System.out.println("Failed to obtain table schema! " + ex.getMessage());
                return 0;
            }
        } else {
                System.out.println("Failed to make a connection!");
                return 0;
        }
	}

	public long consumeResultSet(ResultSet resultset){
		long rowCount = 0;
		try {
			ResultSetMetaData meta = resultset.getMetaData();
			final int columnCount = meta.getColumnCount();
			while ( resultset.next() ) {
				for (int column = 1; column <= columnCount; column++) 
			    {
			        Object value = resultset.getObject(column);
			        if (value != null) {
			            // put is somewhere. Not needed for now
			        }
			        else{
			            // add a "null" string if needed.
			        }
			    }	
				rowCount++;
			}
		} catch (SQLException e) {
			System.out.println("Failed fetch rows!" + e.getMessage());		}
		return rowCount;
		
	}
}
