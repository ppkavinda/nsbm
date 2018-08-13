package models;

import com.mysql.jdbc.Statement;
import db.DBConnection;
import db.DbSingleton;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Undergraduate extends Student {
    private final SimpleObjectProperty<AlResult> al_result = new SimpleObjectProperty<>(new AlResult());
    private final SimpleIntegerProperty rank = new SimpleIntegerProperty(0);
    private final SimpleDoubleProperty z_score = new SimpleDoubleProperty(.0);

    private Connection conn = DbSingleton.getInstance().getConnection();
    private PreparedStatement stmt;

    public Undergraduate() {
        this(new AlResult(), 0, .0);
    }

    public Undergraduate(int student_id, Faculty faculty, Course course, String fname, String lname, String email,
                         String address1, String address2, String tpno, Date dob, String gender, AlResult al_result, int rank,
                         Double z_score) {
        super(student_id, faculty, course, fname, lname, email, address1, address2, tpno, dob, gender);
        setAl_result(al_result);
        setRank(rank);
        setZ_score(z_score);
    }

    private Undergraduate(AlResult al_result, int rank, Double z_score) {
        setAl_result(al_result);
        setRank(rank);
        setZ_score(z_score);
    }

    public void remove(int id) throws SQLException {
        String sql = "DELETE FROM `users` WHERE `users`.`user_id` = ?";

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Cannot delete Undergraduate!");

        }
    }

//    CONSTRUCTORS, GETTERS & SETTERS

    public int update(String email, String fname, String lname, String address1, String address2,
                       String telephone, Date dob, String gender, int fac_id, int course_id, String rank, Double z_score, String sub1,
                       String sub2, String sub3, int user_id) throws SQLException {
        String sql1 = "UPDATE `users` SET `email` = ? WHERE `users`.`user_id` = ?;";
        String sql2 = "UPDATE `student` SET `fname` = ?, `lname` = ?, `email` = ?, `address1` = ?, `address2` = ?, " +
                "`telephone` = ?, `dob` = ?, `gender` = ?, `fac_id` = ?, `course_id` = ? WHERE `student`.`student_id` = ?;";
        String sql3 = "UPDATE `undergraduate` SET `rank` = ?, `z_score` = ? WHERE `undergraduate`.`student_id` = ?;";
        String sql4 = "UPDATE `al_result` SET `sub1` = ?, `sub2` =?, `sub3` = ? WHERE `al_result`.`student_id` = ?;";

        try {
            stmt = conn.prepareStatement(sql1);
            stmt.setString(1, email);
            stmt.setInt(2, user_id);
            stmt.executeUpdate();
            stmt.close();

            stmt = conn.prepareStatement(sql2);
            stmt.setString(1, fname);
            stmt.setString(2, lname);
            stmt.setString(3, email);
            stmt.setString(4, address1);
            stmt.setString(5, address2);
            stmt.setString(6, telephone);
            stmt.setDate(7, (java.sql.Date) dob);
            stmt.setString(8, gender);
            stmt.setInt(9, fac_id);
            stmt.setInt(10, course_id);
            stmt.setInt(11, user_id);
            stmt.executeUpdate();
            stmt.close();

            stmt = conn.prepareStatement(sql3);
            stmt.setString(1, rank);
            stmt.setDouble(2, z_score);
            stmt.setInt(3, user_id);
            stmt.executeUpdate();
            stmt.close();

            stmt = conn.prepareStatement(sql4);
            stmt.setString(1, sub1);
            stmt.setString(2, sub2);
            stmt.setString(3, sub3);
            stmt.setInt(4, user_id);
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Cannot update Undergraduate!");
        }
        return user_id;
    }

    public int  add(String email, String password, int role, String fname, String lname, String address1, String address2,
                    String telephone, Date dob, String gender, int fac_id, int course_id, String rank, Double z_score, String sub1,
                    String sub2, String sub3) throws SQLException {
        String sql1 = "INSERT INTO `users` (`password`, `email`, `role`) VALUES (?, ?, ?);";
        String sql3 = "INSERT INTO `undergraduate` (`student_id`, `rank`, `z_score`) VALUES (?, ?, ?);";
        String sql4 = "INSERT INTO `al_result` (`student_id`, `sub1`, `sub2`, `sub3`) VALUES (?, ?, ?, ?);";

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
            System.out.println("User REGISTERED 1");

            super.add((int) student_id, email, password, fname, lname, address1, address2, telephone, dob, gender, fac_id, course_id);
            System.out.println("Student REGISTered 1");

            stmt = conn.prepareStatement(sql3);
            stmt.setInt(1, (int) student_id);
            stmt.setString(2, rank);
            stmt.setDouble(3, z_score);
            stmt.executeUpdate();
            stmt.close();
            System.out.println("UG registered 2");

            stmt = conn.prepareStatement(sql4);
            stmt.setInt(1, (int) student_id);
            stmt.setString(2, sub1);
            stmt.setString(3, sub2);
            stmt.setString(4, sub3);
            stmt.executeUpdate();
            stmt.close();
            System.out.println("al result registered");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Cannot register Undergraduate!");
        }
        return (int) student_id;
    }

    public ResultSet get() throws SQLException {
        String sql = "SELECT * FROM `undergraduate`, student, faculty, course, al_result" +
                " WHERE undergraduate.student_id = student.student_id " +
                "AND student.fac_id = faculty.faculty_id " +
                "AND student.course_id = course.course_id " +
                "AND undergraduate.student_id = al_result.student_id ORDER BY undergraduate.student_id";
        try {
            stmt = conn.prepareStatement(sql);
            return stmt.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Cannot load Undergraduates!");
        }
    }

    public AlResult getAl_result() {
        return al_result.get();
    }

    public void setAl_result(AlResult al_result) {
        this.al_result.set(al_result);
    }

    public SimpleObjectProperty<AlResult> al_resultProperty() {
        return al_result;
    }

    public int getRank() {
        return rank.get();
    }

    public void setRank(int rank) {
        this.rank.set(rank);
    }

    public SimpleIntegerProperty rankProperty() {
        return rank;
    }

    public double getZ_score() {
        return z_score.get();
    }

    public void setZ_score(double z_score) {
        this.z_score.set(z_score);
    }

    public SimpleDoubleProperty z_scoreProperty() {
        return z_score;
    }

    public String toString() {
        return getFname() + " " + getLname();
    }
}
