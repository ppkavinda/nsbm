package models;

import db.DBConnection;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Lecture {
    private final SimpleIntegerProperty lecture_id = new SimpleIntegerProperty(0);
    private final SimpleObjectProperty<LecHall> lec_hall = new SimpleObjectProperty<>(new LecHall());
    private final SimpleObjectProperty<Subject> subject = new SimpleObjectProperty<>(new Subject());
    private final SimpleStringProperty start_time = new SimpleStringProperty("");
    private final SimpleStringProperty end_time = new SimpleStringProperty("");

    private DBConnection conn = new DBConnection();
    PreparedStatement stmt;

    public Lecture () { this (0, new LecHall(), new Subject(), "", "");}

    public Lecture (int lecture_id, LecHall lec_hall, Subject subject, String start_time, String end_time) {
        setLecture_id(lecture_id);
        setLec_hall(lec_hall);
        setSubject(subject);
        setStart_time(start_time);
        setEnd_time(end_time);
    }

    public void remove (int lec_id) {
        String sql = "DELETE FROM `lecture` WHERE `lecture`.`lecture_id` = ?";

        try {
            stmt = conn.connect().prepareStatement(sql);
            stmt.setInt(1, lec_id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update (String start_time, String end_time, int hall_id, int subject_code, int lec_id) {
        String sql = "UPDATE `lecture` SET `start_time` = ?, `end_time` = ?, `hall_id` = ?, subject_code = ? WHERE `lecture`.`lecture_id` = ?;";

        try {
            stmt = conn.connect().prepareStatement(sql);
            stmt.setString(1, start_time);
            stmt.setString(2, end_time);
            stmt.setInt(3, hall_id);
            stmt.setInt(4, subject_code);
            stmt.setInt(5, lec_id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add (String start_time, String end_time, int hall_id, int subject_code) {
        String sql = "INSERT INTO `lecture` (`lecture_id`, `start_time`, `end_time`, `hall_id`, `subject_code`) VALUES (NULL, ?, ?, ?, ?);";

        try {
            stmt = conn.connect().prepareStatement(sql);
            stmt.setString(1, start_time);
            stmt.setString(2, end_time);
            stmt.setInt(3, hall_id);
            stmt.setInt(4, subject_code);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet get () {
        String sql = "SELECT * FROM `lecture` INNER JOIN lec_hall ON lec_hall.hall_id = lecture.hall_id INNER JOIN subject ON subject.subject_code = lecture.subject_code";

        try {
            stmt = conn.connect().prepareStatement(sql);
            return stmt.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int getLecture_id() {
        return lecture_id.get();
    }

    public SimpleIntegerProperty lecture_idProperty() {
        return lecture_id;
    }

    public void setLecture_id(int lecture_id) {
        this.lecture_id.set(lecture_id);
    }

    public LecHall getLec_hall() {
        return lec_hall.get();
    }

    public SimpleObjectProperty<LecHall> lec_hallProperty() {
        return lec_hall;
    }

    public void setLec_hall(LecHall lec_hall) {
        this.lec_hall.set(lec_hall);
    }

    public Subject getSubject() {
        return subject.get();
    }

    public SimpleObjectProperty<Subject> subjectProperty() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject.set(subject);
    }

    public String getStart_time() {
        return start_time.get();
    }

    public SimpleStringProperty start_timeProperty() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time.set(start_time);
    }

    public String getEnd_time() {
        return end_time.get();
    }

    public SimpleStringProperty end_timeProperty() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time.set(end_time);
    }
}
