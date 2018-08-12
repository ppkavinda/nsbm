package models;

import db.DBConnection;
import db.DbSingleton;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Test {
    private final SimpleIntegerProperty test_id = new SimpleIntegerProperty(0);
    private final SimpleObjectProperty<Subject> subject = new SimpleObjectProperty<>(new Subject());
    private final SimpleObjectProperty<Date> date = new SimpleObjectProperty<>(new Date());
    private final SimpleStringProperty type = new SimpleStringProperty("");
    private final SimpleStringProperty start_time = new SimpleStringProperty("");
    private final SimpleStringProperty end_time = new SimpleStringProperty("");

    private Connection conn = DbSingleton.getInstance().getConnection();
    private PreparedStatement stmt;

    public Test() {
        this(0, new Subject(), new Date(), "", "", "");
    }

    public Test(int test_id, Subject subject, Date date, String start_time, String end_time, String type) {
        setTest_id(test_id);
        setSubject(subject);
        setDate(date);
        setStart_time(start_time);
        setEnd_time(end_time);
        setType(type);
    }

    public void remove (int id) {
        String sql =   "DELETE FROM `test` WHERE `test`.`test_id` = ?";

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("deleted" + sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update (Date date, String start_time, String end_time, int subject_code, String type, int id) {
        String sql = "UPDATE `test` SET " +
                "`date` = ?, `start_time` = ?, `end_time` = ?, `subject_code` = ?, `type` = ?" +
                " WHERE `test`.`test_id` = ?;";

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setDate(1, (java.sql.Date) date);
            stmt.setString(2, start_time);
            stmt.setString(3, end_time);
            stmt.setInt(4, subject_code);
            stmt.setString(5, type);
            stmt.setInt(6, id);
            stmt.executeUpdate();

            System.out.println(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add(Date date, String start_time, String end_time, int subject_code, String type) {
        String sql = "INSERT INTO `test` (`date`, `start_time`, `end_time`, `subject_code`, `type`) " +
                "VALUES (?, ?, ?, ?, ?);";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setDate(1, (java.sql.Date) date);
            stmt.setString(2, start_time);
            stmt.setString(3, end_time);
            stmt.setInt(4, subject_code);
            stmt.setString(5, type);
            stmt.executeUpdate();

            System.out.println(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet get() {
        String sql = "SELECT * FROM `test` " +
                "INNER JOIN subject ON test.subject_code = subject.subject_code;";

        try {
            stmt = conn.prepareStatement(sql);
            return stmt.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // GETTERS and SETTERS
    public Subject getSubject() {
        return subject.get();
    }

    public void setSubject(Subject subject) {
        this.subject.set(subject);
    }

    public SimpleObjectProperty<Subject> subjectProperty() {
        return subject;
    }

    public int getTest_id() {
        return test_id.get();
    }

    public void setTest_id(int test_id) {
        this.test_id.set(test_id);
    }

    public SimpleIntegerProperty test_idProperty() {
        return test_id;
    }

    public Date getDate() {
        return date.get();
    }

    public void setDate(Date date) {
        this.date.set(date);
    }

    public SimpleObjectProperty<Date> dateProperty() {
        return date;
    }

    public String getStart_time() {
        return start_time.get();
    }

    public void setStart_time(String start_time) {
        this.start_time.set(start_time);
    }

    public SimpleStringProperty start_timeProperty() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time.get();
    }

    public void setEnd_time(String end_time) {
        this.end_time.set(end_time);
    }

    public SimpleStringProperty end_timeProperty() {
        return end_time;
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public String toString() {
        return String.valueOf(getTest_id());
    }
}
