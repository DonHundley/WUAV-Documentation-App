package com.WUAV.dal;

import com.microsoft.sqlserver.jdbc.*;

import java.sql.*;

public class DatabaseConnector {
    private SQLServerDataSource dataSource;

    /**
     * Setting the parameters to connect to database with given information
     */
    public DatabaseConnector() {
        dataSource = new SQLServerDataSource();
        dataSource.setDatabaseName("CSe2022B_e_2_WUAV_DOCUMENTATION");
        dataSource.setUser("CSe2022B_e_2");
        dataSource.setPassword("PotatoPotato");
        dataSource.setServerName("10.176.111.34");
        dataSource.setPortNumber(1433);
        dataSource.setTrustServerCertificate(true);
    }

    /**
     * Returning connection based on information given in DatabaseConnector
     */
    public Connection getConnection() throws SQLServerException {
        return dataSource.getConnection();
    }

    /**
     * com.WUAV.Main method to test if the connection is open
     */
    public static void main(String[] args) throws SQLException {
        DatabaseConnector databaseConnector = new DatabaseConnector();

        try (Connection connection = databaseConnector.getConnection()) {
            System.out.println("Is it open? " + !connection.isClosed());
        }
    }
}