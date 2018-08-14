package models;

import com.mysql.jdbc.Statement;
import db.DBConnection;
import db.DbSingleton;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Connection;
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

    private Connection conn = DbSingleton.getInstance().getConnection();
    private PreparedStatement stmt;

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

    public void removeLecture(int staff_id, int lecture_id) {
        String sql = "DELETE FROM `lecturer_lec` WHERE `lecturer_lec`.`staff_id` = ? AND `lecturer_lec`.`lec_id` = ?";

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, staff_id);
            stmt.setInt(2, lecture_id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void remove (int lecturer_id) {
        String sql = "DELETE FROM `users` WHERE `users`.`user_id` = ?";

        try {
            stmt = conn.prepareStatement(sql);
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
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, fname);
            stmt.setString(2, lname);
            stmt.setString(3 ,email);
            stmt.setInt(5, lecturer_id);
            stmt.setString(4, gender);
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addLecture (int staff_id, int lec_id) throws SQLException {
        String sql = "INSERT INTO `lecturer_lec` (`staff_id`, `lec_id`) VALUES (?, ?);";

            stmt = conn.prepareStatement(sql);
            stmt.setInt(2, lec_id);
            stmt.setInt(1, staff_id);
            stmt.executeUpdate();
            stmt.close();

    }

    public int add(String fname, String lname, String email, String password, String gender, int role) {
        String sql1 = "INSERT INTO `users` (`password`, `email`, `role`) VALUES (?, ?, ?);";
        String sql2 = "INSERT INTO `lecturer` (`lecturer_id`, `fname`, `lname`, `email`, `password`, `gender`) VALUES (?, ?, ?, ?, ?, ?);";

        long lecture_id = 0;
        try {
            stmt = conn.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, password);
            stmt.setInt(3, role);
            stmt.setString(2, email);

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

            stmt = conn.prepareStatement(sql2);
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
        return (int) lecture_id;
    }

    public ResultSet getUnallocatedLectures (int staff_id) {
        String sql = "SELECT * FROM lecture " +
                "INNER JOIN lec_hall ON lec_hall.hall_id = lecture.hall_id " +
                "INNER JOIN subject ON subject.subject_code = lecture.hall_id " +
                "WHERE lecture.lecture_id NOT IN" +
                " (SELECT lecturer_lec.lec_id FROM lecturer_lec WHERE lecturer_lec.staff_id = ?)";

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, staff_id);
            return stmt.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getLectures (int staff_id) {
        String sql = "SELECT * FROM lecturer_lec, lecture, subject, lec_hall " +
                "WHERE lecturer_lec.lec_id = lecture.lecture_id " +
                "AND lecture.subject_code = subject.subject_code " +
                "AND lec_hall.hall_id = lecture.hall_id " +
                "AND lecturer_lec.staff_id = ?";

        System.out.println(sql);

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, staff_id);
            return stmt.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public ResultSet get () {
        String sql = "SELECT * FROM `lecturer`";

        try {
            stmt = conn.prepareStatement(sql);
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

    public String toString () { return this.getFname() + " " + this.getLname(); }
}
