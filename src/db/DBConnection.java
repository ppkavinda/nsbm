package db;

import java.sql.*;

public class DBConnection {
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

    public ResultSet selectFrom (String fields, String table) {
        try {
            Statement stmt = connect().createStatement();
            String query = "SELECT " + fields + " FROM " + table;
            ResultSet rs = stmt.executeQuery(query);
            return rs;

        } catch (SQLException e) {
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
