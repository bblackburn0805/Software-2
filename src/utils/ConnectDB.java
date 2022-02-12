
/*
C195 - Started March 2020
Brandon Blackburn
Last updated: 09/25/2020

Requirement Criteria in this class:


 */


package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
    private static final String userName = "U05v49";
    private static final String password = "53688619273";
    private static final String jdbcURL = "jdbc:mySQL://wgudb.ucertify.com/U05v49";

    private static final String mySQLJDBCDriver = "com.mysql.jdbc.Driver";      //Driver Reference
    private static Connection conn = null;

    public static Connection startConnection() {
        try {
            Class.forName(mySQLJDBCDriver);
            conn = DriverManager.getConnection(jdbcURL, userName, password);
            System.out.println("Connection successful!");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void closeConnection() {
        try {
            conn.close();
            System.out.println("Connection Closed");
        }
        catch(SQLException e){
            System.out.println(e.getErrorCode());
        }

    }

    public static Connection getConnection(){return conn;}
}
