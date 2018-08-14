package db;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DbSingleton {
    private static String db_url;
    private static String db_port;
    private static String db_name;
    private static String db_user;
    private static String db_password;
    private static Connection connection;

    private DbSingleton() {
        /*
         *Default database parameters
         */
        db_url = "jdbc:mysql://localhost";
        db_port = "3306";
        db_name = "nsbm_test";
        db_user = "nsbm_user";
        db_password = "nsbm";
        /* Creation of an instance of the connection statement*/
        connection = setConnection();
    }
    /* Private method charge to set the connection statement*/
    private static Connection setConnection() {
        try {
            String url = "" + db_url + ":" + db_port + "/" + db_name + "";
            java.sql.Connection conn = DriverManager.getConnection(url, db_user, db_password);

            //Creation of the Statement object
//            java.sql.Statement state = conn.createStatement();
            return (Connection) conn;
        } catch (SQLException ex) {
            Logger.getLogger(DbSingleton.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /* Private inner class responsible for instantiating the single instance of the singleton */
    private static class DbSingletonManagerHolder {
        private final static DbSingleton instance = new DbSingleton();
    }

    /**
     * @return
    Public method, which is the only method allowed to return an instance of
    the singleton (the instance here is the database connection statement)
     */
    public static DbSingleton getInstance() {
        try {
            return DbSingletonManagerHolder.instance;
        } catch (ExceptionInInitializerError ex) {

        }
        return null;
    }
    public static Connection getConnection() {
        return connection;
    }

    public ResultSet loginUser (String username, String password) {
        try {
            String sql = "SELECT user_id FROM users WHERE email = ? AND password = ? AND role = 1";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            System.out.println(stmt.toString());

            return stmt.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}