package models;

import db.DBConnection;
import db.DbSingleton;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Faculty {

    private final SimpleIntegerProperty faculty_id = new SimpleIntegerProperty(0);
    private final SimpleStringProperty name = new SimpleStringProperty("");

    private PreparedStatement stmt = null;

    private DbSingleton conn = DbSingleton.getInstance();

    public Faculty () {
        this(0, "");
    }

    public Faculty(int faculty_id, String name) {
        setFaculty_id(faculty_id);
        setName(name);
    }

    public void configFacultyBox(ComboBox<Faculty> facultyBox) {
        ResultSet rs = get();

        try {
            ObservableList<Faculty> data = facultyBox.getItems();
            while (rs.next()) {
                data.add(
                        new Faculty(
                                rs.getInt("faculty_id"),
                                rs.getString("name")
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void remove (int id) throws SQLException {
        String sql = "DELETE FROM faculty WHERE `faculty_id` = ?";

        stmt = conn.getConnection().prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();

    }

    // get all the record of table
    public ResultSet get () {
        String sql = "SELECT * FROM faculty";

        try {
            stmt = conn.getConnection().prepareStatement(sql);
            return stmt.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ResultSet get (int faculty_id) {
        String sql = "SELECT * FROM faculty WHERE faculty_id = " + faculty_id;

        try {
            System.out.println("SQL" + sql);
            stmt = conn.getConnection().prepareStatement(sql);
            return stmt.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void add (String name) {
        String sql = "INSERT INTO faculty (`name`) VALUES (?); ";
        try {
            System.out.println(sql + " " + name);
            stmt = conn.getConnection().prepareStatement(sql);
            stmt.setString(1, name);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void update (int id, String name) {
        String sql = "UPDATE faculty SET `name` = ? WHERE `faculty_id` = ?";
        try {
            System.out.println(sql + " " + name);
            stmt = conn.getConnection().prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setInt(2, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public int getFaculty_id() {
        return faculty_id.get();
    }

    public SimpleIntegerProperty faculty_idProperty() {
        return faculty_id;
    }

    public void setFaculty_id(int faculty_id) {
        this.faculty_id.set(faculty_id);
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
