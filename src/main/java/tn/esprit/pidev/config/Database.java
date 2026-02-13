package tn.esprit.pidev.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class Database {
    private static final String CONFIG_PATH = "/config.properties";
    private static Connection connection;

    private Database() {
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            Properties properties = loadProperties();
            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.user");
            String password = properties.getProperty("db.password");
            String driver = properties.getProperty("db.driver");

            try {
                Class.forName(driver);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Oracle JDBC driver not found", e);
            }

            connection = DriverManager.getConnection(url, user, password);
        }

        return connection;
    }

    private static Properties loadProperties() throws SQLException {
        Properties properties = new Properties();
        try (InputStream inputStream = Database.class.getResourceAsStream(CONFIG_PATH)) {
            if (inputStream == null) {
                throw new SQLException("Missing config.properties on classpath");
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new SQLException("Failed to load config.properties", e);
        }
        return properties;
    }
}
