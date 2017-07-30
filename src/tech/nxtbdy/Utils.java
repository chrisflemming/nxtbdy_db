package tech.nxtbdy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Utils {
    public static Connection getConnection() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:ORCL", "c##nxtbdy", "Passw0rd");

        } catch (SQLException e) {

            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }

        if (connection == null) {
            System.out.println("Failed to make connection!");
        }

        return connection;
    }
}
