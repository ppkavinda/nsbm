package models;

import com.mysql.jdbc.Statement;
import controllers.admin.InstructorController;
import db.DBConnection;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Instructor {
    private final SimpleIntegerProperty instructor_id = new SimpleIntegerProperty(0);
    private  final SimpleStringProperty fname = new SimpleStringProperty("");
    private final SimpleStringProperty lname = new SimpleStringProperty("");
    private final SimpleStringProperty email = new SimpleStringProperty("");
    private final SimpleStringProperty gender = new SimpleStringProperty("");

    private PreparedStatement stmt;
    private DBConnection conn = new DBConnection();

    public Instructor () {
        this (0, "", "", "", "");
    }

    public Instructor (int instructor_id, String fname, String lname, String email, String gender) {
        setInstructor_id(instructor_id);
        setFname(fname);
        setLname(lname);
        setEmail(email);
        setGender(gender);
    }


    public void remove (int instructor_id) {
        String sql = "DELETE FROM `users` WHERE `users`.`user_id` = ?";

        try {
            stmt = conn.connect().prepareStatement(sql);
            stmt.setInt(1, instructor_id);
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update (String fname, String lname, String email, String gender, int lecturer_id) {
        String sql = "UPDATE `instructor` SET `fname` = ?, `lname` = ?, `email` = ?, `gender` = ? WHERE `instructor`.`instructor_id` = ?;";

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
        String sql2 = "INSERT INTO `instructor` (`instructor_id`, `fname`, `lname`, `email`, `password`, `gender`) VALUES (?, ?, ?, ?, ?, ?);";

        long instructor_id;
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
                    instructor_id = generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
            stmt.close();

            stmt = conn.connect().prepareStatement(sql2);
            stmt.setInt(1, (int) instructor_id);
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
        String sql = "SELECT * FROM `instructor`";

        try {
            stmt = conn.connect().prepareStatement(sql);
            return stmt.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int getInstructor_id() {
        return instructor_id.get();
    }

    public SimpleIntegerProperty instructor_idProperty() {
        return instructor_id;
    }

    public void setInstructor_id(int instructor_id) {
        this.instructor_id.set(instructor_id);
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
