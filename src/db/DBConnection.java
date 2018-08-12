package db;

import java.sql.*;

public class DBConnection {

//    // placeholder for hold current object
//    private static DBConnection __me = null;
//
//    // private constructor - no other object can instantiate
//    private DBConnection () {}
//
//    public static DBConnection getInstance () {
//        if (__me == null ) {
//            __me = new DBConnection();
//        }
//        return __me;
//    }

    public Connection connect () {
        Connection conn;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/nsbm_test", "nsbm_user", "nsbm");
            return conn;

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;

        }
    }

    public ResultSet loginUser (String sql, String username, String password) {
        try {
            PreparedStatement stmt = connect().prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            System.out.println(stmt.toString());

            ResultSet rs = stmt.executeQuery();
            return rs;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
