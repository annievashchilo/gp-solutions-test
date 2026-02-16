package propertyview.usecase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class GetSqlDbConnectionUseCase {
    public Connection execute() throws SQLException {
        Properties properties = new Properties();
        try {
            properties.load(
                    getClass().getClassLoader().getResourceAsStream("application.properties"));
        } catch (Exception e) {
            System.err.println("Failed to load database properties: " + e.getMessage());
            return null;
        }

        String dbUrl = properties.getProperty("spring.datasource.url");
        String dbUser = properties.getProperty("spring.datasource.username");
        String dbPassword = properties.getProperty("spring.datasource.password");

        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found: " + e.getMessage());
            return null;
        }

        Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        if (conn == null) {
            System.err.println("Failed to establish a database connection.");
        }

        return conn;
    }
}
