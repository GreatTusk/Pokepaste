/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.pool.OracleDataSource;

public class Conexion {

//    private static final int MAX_POOL_SIZE = 5;
//
//    // Configuration details (replace with actual values or externalize)
//    private static final String DB_URL = "jdbc:oracle:thin:@mdy1131_high?TNS_ADMIN=E:/Documents/GUI/Wallet_MDY1131";
//    private static final String DB_USER = "USUARIO_POKEMON_PASTE";
//    private static final String DB_PASSWORD = "Contrasena.123";
//
//    private final BlockingQueue<OracleConnection> connectionQueue;
//
//    private static final Conexion instance = new Conexion();
//
//    private Conexion() {
//        connectionQueue = new ArrayBlockingQueue<>(MAX_POOL_SIZE);
//        initializePool();
//    }
//
//    private void initializePool() {
//        try {
//            Properties info = new Properties();
//            info.put(OracleConnection.CONNECTION_PROPERTY_USER_NAME, DB_USER);
//            info.put(OracleConnection.CONNECTION_PROPERTY_PASSWORD, DB_PASSWORD);
//            info.put(OracleConnection.CONNECTION_PROPERTY_DEFAULT_ROW_PREFETCH, "500");
//
//            OracleDataSource ods = new OracleDataSource();
//            ods.setURL(DB_URL);
//            ods.setConnectionProperties(info);
//
//            for (int i = 0; i < MAX_POOL_SIZE; i++) {
//                OracleConnection connection = (OracleConnection) ods.getConnection();
//                connectionQueue.offer(connection);
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException("Error initializing connection pool", e);
//        }
//    }
//
//    public static Conexion getInstance() {
//        return instance;
//    }
//
//    public Connection getConnection() throws SQLException {
//        try {
//            return connectionQueue.take();
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            throw new SQLException("Error getting connection from the pool", e);
//        }
//    }
//
//    public void releaseConnection(Connection connection) {
//        if (connection instanceof OracleConnection) {
//            try {
//                if (!connection.isClosed()) {
//                    connection.close();  // Close the connection
//                }
//                connectionQueue.offer((OracleConnection) connection);
//            } catch (SQLException e) {
//                // Log or handle exception
//            }
//        } else {
//            throw new IllegalArgumentException("Invalid connection type");
//        }
//    }
//
//
//    public static Connection getDirectConnection() throws SQLException {
//        return getInstance().getConnection();
//    }
//
//    public static OracleConnection getOracleConnection() throws SQLException {
//        return (OracleConnection) getInstance().getConnection();
//    }
//  try (OracleConnection connection = Conexion.getOracleConnection())


        // Configuration details (replace with actual values or externalize)
        private static final String DB_URL = "jdbc:oracle:thin:@mdy1131_high?TNS_ADMIN=E:/Documents/GUI V1/GUI/Wallet_MDY1131";
        private static final String DB_USER = "USUARIO_POKEMON_PASTE";
        private static final String DB_PASSWORD = "Contrasena.123";

        private static final Conexion instance = new Conexion();

        private Conexion() {
            // Initialize any setup if needed
        }

        public static Conexion getInstance() {
            return instance;
        }

        public Connection getConnection() throws SQLException {
            Properties info = new Properties();
            info.put("user", DB_USER);
            info.put("password", DB_PASSWORD);

            return DriverManager.getConnection(DB_URL, info);
        }

        public void releaseConnection(Connection connection) {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                // Log or handle exception
                e.printStackTrace();
            }
        }

        public static Connection getDirectConnection() throws SQLException {
            return getInstance().getConnection();
        }
    }




 
