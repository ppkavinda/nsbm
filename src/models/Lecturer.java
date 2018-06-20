package models;

import com.mysql.jdbc.Statement;
import db.DBConnection;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Lecturer {

    private final SimpleIntegerProperty lecturer_id = new SimpleIntegerProperty(0);
    private  final SimpleStringProperty fname = new SimpleStringProperty("");
    private final SimpleStringProperty lname = new SimpleStringProperty("");
    private final SimpleStringProperty email = new SimpleStringProperty("");
    private final SimpleStringProperty gender = new SimpleStringProperty("");

    private PreparedStatement stmt;
    private DBConnection conn = new DBConnection();

//    CONSTRUCTORS  GETTERS SETTERS

    public Lecturer () {
        this (0, "", "", "", "");
    }

    public Lecturer (int lecturer_id, String fname, String lname, String email, String gender) {
        setLecturer_id(lecturer_id);
        setFname(fname);
        setLname(lname);
        setEmail(email);
        setGender(gender);
    }

    public void remove (int lecturer_id) {
        String sql = "DELETE FROM `users` WHERE `users`.`user_id` = ?";

        try {
            stmt = conn.connect().prepareStatement(sql);
            stmt.setInt(1, lecturer_id);
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update (String fname, String lname, String email, String gender, int lecturer_id) {
        String sql = "UPDATE `lecturer` SET `fname` = ?, `lname` = ?, `email` = ?, `gender` = ? WHERE `lecturer`.`lecturer_id` = ?;";

        try {
            stmt = conn.connect().prepareStatement(sql);
            stmt.setString(1, fname);
            stmt.setString(2, lname);
            stmt.setString(3 ,email);
            stmt.setString(4, gender);
            stmt.setInt(5, lecturer_id);
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add(String fname, String lname, String email, String password, String gender, int role) {
        String sql1 = "INSERT INTO `users` (`password`, `email`, `role`) VALUES (?, ?, ?);";
        String sql2 = "INSERT INTO `lecturer` (`lecturer_id`, `fname`, `lname`, `email`, `password`, `gender`) VALUES (?, ?, ?, ?, ?, ?);";

        long lecture_id;
        try {
            stmt = conn.connect().prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, password);
            stmt.setString(2, email);
            stmt.setInt(3, role);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    lecture_id = generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
            stmt.close();

            stmt = conn.connect().prepareStatement(sql2);
            stmt.setInt(1, (int) lecture_id);
            stmt.setString(2, fname);
            stmt.setString(3, lname);
            stmt.setString(4, email);
            stmt.setString(5, password);
            stmt.setString(6, gender);
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public ResultSet get () {
        String sql = "SELECT * FROM `lecturer`";

        try {
            stmt = conn.connect().prepareStatement(sql);
            return stmt.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int getLecturer_id() {
        return lecturer_id.get();
    }

    public SimpleIntegerProperty lecturer_idProperty() {
        return lecturer_id;
    }

    public void setLecturer_id(int lecturer_id) {
        this.lecturer_id.set(lecturer_id);
    }

    public String getFname() {
        return fname.get();
    }

    public SimpleStringProperty fnameProperty() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname.set(fname);
    }

    public String getLname() {
        return lname.get();
    }

    public SimpleStringProperty lnameProperty() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname.set(lname);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getGender() {
        return gender.get();
    }

    public SimpleStringProperty genderProperty() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }
}
