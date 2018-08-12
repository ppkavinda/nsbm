package models;

import db.DBConnection;
import db.DbSingleton;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Lab {
    private final SimpleIntegerProperty lab_id = new SimpleIntegerProperty(0);
    private final SimpleStringProperty name = new SimpleStringProperty("");

    private PreparedStatement stmt;

    private DbSingleton conn = DbSingleton.getInstance();

    public Lab () {
        this(0, "");
    }

    public Lab(int lab_id, String name) {
        setLab_id(lab_id);
        setName(name);
    }

    public void remove (int id) {
        String sql = "DELETE FROM lab WHERE `lab_id` = ?";

        try {
            System.out.println(sql);
            stmt = conn.getConnection().prepareStatement(sql);
            stmt.setInt(1, id);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public void update (int id, String name) {
        String sql = "UPDATE `lab` SET `name` = ? WHERE `lab`.`lab_id` = ?;";

        try {
            stmt = conn.getConnection().prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setInt(2, id);
            stmt.executeUpdate();
            System.out.println(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add (String name) {
        String sql = "INSERT INTO `lab` (`name`) VALUES (?);";

        try {
            stmt = conn.getConnection().prepareStatement(sql);
            stmt.setString(1, name);
            stmt.executeUpdate();
            System.out.println(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet get () {
        String sql = "SELECT * FROM lab ";

        try {
            stmt = conn.getConnection().prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            System.out.println(sql);       // TODO: 6/7/2018 remove
            return rs;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Lab:GET ::Null");
        return null;
    }

    public int getLab_id() {
        return lab_id.get();
    }

    public SimpleIntegerProperty lab_idProperty() {
        return lab_id;
    }

    public void setLab_id(int lab_id) {
        this.lab_id.set(lab_id);
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
