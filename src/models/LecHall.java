package models;

import db.DBConnection;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LecHall {
    private final SimpleIntegerProperty hall_id = new SimpleIntegerProperty(0);
    private final SimpleStringProperty name = new SimpleStringProperty("");

    DBConnection conn = new DBConnection();
    PreparedStatement stmt;

    public LecHall() {
        this (0, "");
    }

    public LecHall(int hall_id, String name) {
        setHall_id(hall_id);
        setName(name);
    }

    public void remove (int hall_id) {
        String sql = "DELETE FROM `lec_hall` WHERE `hall_id` = ?";

        try {
            stmt = conn.connect().prepareStatement(sql);

            stmt.setInt(1, hall_id);
            stmt.executeUpdate();
            System.out.println(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update (int hall_id, String name) {
        String sql = "UPDATE `lec_hall` SET `name` = ? WHERE `hall_id` = ?";

        try {
            stmt = conn.connect().prepareStatement(sql);
            stmt.setInt(2, hall_id);
            stmt.setString(1, name);

            stmt.executeUpdate();
            System.out.println(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add (String name) {
        String sql = "INSERT INTO `lec_hall` (`name`) VALUES (?);";

        try {
            stmt = conn.connect().prepareStatement(sql);
            stmt.setString(1, name);
            System.out.println(sql);    // todo Remove this line
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet get () {
        String sql = "SELECT * FROM lec_hall";

        try {
            stmt = conn.connect().prepareStatement(sql);
            System.out.println(sql);    // todo Remove this line
            return stmt.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int getHall_id() {
        return hall_id.get();
    }

    public SimpleIntegerProperty hall_idProperty() {
        return hall_id;
    }

    public void setHall_id(int hall_id) {
        this.hall_id.set(hall_id);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String toString () { return getName(); }
}
