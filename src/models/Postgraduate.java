package models;

import com.mysql.jdbc.Statement;
import db.DbSingleton;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Postgraduate extends Student {
    private final SimpleIntegerProperty year_of_completion = new SimpleIntegerProperty(0);
    private final SimpleStringProperty institute = new SimpleStringProperty("");
    private final SimpleStringProperty qualification_type = new SimpleStringProperty("");

    private Connection conn = DbSingleton.getInstance().getConnection();
    private PreparedStatement stmt;

    public Postgraduate () {
        super();
    }

    public Postgraduate (int student_id, Faculty faculty, Course course, String fname, String lname, String email, String address1, String address2, String tpno, Date dob, String gender, int year_of_completion, String institute, String quilification_type) {
        super(student_id, faculty, course, fname, lname, email, address1, address2, tpno, dob, gender);
        setYear_of_completion(year_of_completion);
        setInstitute(institute);
        setQualification_type(quilification_type);
    }

    public void remove(int id) throws SQLException {
        String sql = "DELETE FROM `users` WHERE `users`.`user_id` = ?";

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new SQLException("Cannot delete the Postgraduate!");
        }
    }

//    CONSTRUCTORS, GETTERS & SETTERS

    public int update(String email, String fname, String lname, String address1, String address2, String telephone, Date dob, String gender, int fac_id, int course_id, String qualification_type, String institute, int year_of_completion, int user_id) {
        String sql1 = "UPDATE `users` SET `email` = ? WHERE `users`.`user_id` = ?;";

        String sql2 = "UPDATE `postgraduate` SET `qualification_type` = ?, `institute` = ?, `year_of_completion` = ? " +
                "WHERE `postgraduate`.`student_id` = ?;";

        try {
            stmt = conn.prepareStatement(sql1);
            stmt.setString(1, email);
            stmt.setInt(2, user_id);
            stmt.executeUpdate();
            stmt.close();

            super.update(email, fname, lname, address1, address2, telephone, dob, gender, fac_id, course_id, user_id);

            stmt = conn.prepareStatement(sql2);
            stmt.setString(1, qualification_type);
            stmt.setString(2, institute);
            stmt.setInt(3, year_of_completion);
            stmt.setInt(4, user_id);
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user_id;
    }

    public int add(String email, String password, int role, String fname, String lname, String address1, String address2, String telephone, Date dob, String gender, int fac_id, int course_id, String qualification_type, String institute, int year_of_completion) {
        String sql1 = "INSERT INTO `users` (`password`, `email`, `role`) VALUES (?, ?, ?);";
        String sql3 = "INSERT INTO `postgraduate` (`student_id`, `qualification_type`, `institute`, `year_of_completion`) VALUES (?, ?, ?, ?);";

        long student_id;
        try {
            stmt = conn.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, password);
            stmt.setString(2, email);
            stmt.setInt(3, role);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    student_id = generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
            stmt.close();

            super.add((int) student_id, email, password, fname, lname, address1, address2, telephone, dob, gender, fac_id, course_id);

            stmt = conn.prepareStatement(sql3);
            stmt.setInt(1, (int) student_id);
            stmt.setString(2, qualification_type);
            stmt.setString(3, institute);
            stmt.setInt(4, year_of_completion);
            stmt.executeUpdate();
            stmt.close();

            return (int) student_id;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public ResultSet get() {
        String sql = "SELECT * FROM `postgraduate`, student, faculty, course" +
                " WHERE postgraduate.student_id = student.student_id " +
                "AND student.fac_id = faculty.faculty_id " +
                "AND student.course_id = course.course_id " +
                "ORDER BY postgraduate.student_id";
        try {
            stmt = conn.prepareStatement(sql);
            return stmt.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

//    GETTERS & SETTERS

    public int getYear_of_completion() {
        return year_of_completion.get();
    }

    public SimpleIntegerProperty year_of_completionProperty() {
        return year_of_completion;
    }

    public void setYear_of_completion(int year_of_completion) {
        this.year_of_completion.set(year_of_completion);
    }

    public String getInstitute() {
        return institute.get();
    }

    public SimpleStringProperty instituteProperty() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute.set(institute);
    }

    public String getQualification_type() {
        return qualification_type.get();
    }

    public SimpleStringProperty qualification_typeProperty() {
        return qualification_type;
    }

    public void setQualification_type(String qualification_type) {
        this.qualification_type.set(qualification_type);
    }
}
