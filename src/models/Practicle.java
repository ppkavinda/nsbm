package models;

import db.DBConnection;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Practicle {
    private  final SimpleIntegerProperty practicle_id = new SimpleIntegerProperty(0);
    private final SimpleObjectProperty<Lab> lab = new SimpleObjectProperty<>(new Lab());
    private final SimpleObjectProperty<Subject> subject = new SimpleObjectProperty<>(new Subject());
    private final SimpleStringProperty start_time= new SimpleStringProperty("");
    private final SimpleStringProperty end_time= new SimpleStringProperty("");

    private DBConnection conn = new DBConnection();
    private PreparedStatement stmt;

    public Practicle () {
        this (0, new Lab(), new Subject(), "", "");
    }

    public Practicle (int practicle_id, Lab lab, Subject subject, String start_time, String end_time) {
        setPracticle_id(practicle_id);
        setLab(lab);
        setSubject(subject);
        setStart_time(start_time);
        setEnd_time(end_time);
    }

    public void remove (int lec_id) {
        String sql = "DELETE FROM `practicle` WHERE `practicle`.`practicle_id` = ?";

        try {
            stmt = conn.connect().prepareStatement(sql);
            stmt.setInt(1, lec_id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void update (String start_time, String end_time, int lab, int subject_code, int practicle_id) {
        String sql = "UPDATE `practicle` SET `start_time` = ?, `end_time` = ?, `lab` = ?, subject_code = ? WHERE `practicle`.`practicle_id` = ?;";

        try {
            stmt = conn.connect().prepareStatement(sql);
            stmt.setString(1, start_time);
            stmt.setString(2, end_time);
            stmt.setInt(3, lab);
            stmt.setInt(4, subject_code);
            stmt.setInt(5, practicle_id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add (String start_time, String end_time, int lab, int subject_code) {
        String sql = "INSERT INTO `practicle` (`practicle_id`, `start_time`, `end_time`, `lab`, `subject_code`) VALUES (NULL, ?, ?, ?, ?);";

        try {
            stmt = conn.connect().prepareStatement(sql);
            stmt.setString(1, start_time);
            stmt.setString(2, end_time);
            stmt.setInt(3, lab);
            stmt.setInt(4, subject_code);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet get () {
        String sql = "SELECT * FROM `practicle` INNER JOIN lab ON lab.lab_id = practicle.lab INNER JOIN subject ON subject.subject_code = practicle.subject_code";

        try {
            stmt = conn.connect().prepareStatement(sql);
            return stmt.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int getPracticle_id() {
        return practicle_id.get();
    }

    public SimpleIntegerProperty practicle_idProperty() {
        return practicle_id;
    }

    public void setPracticle_id(int practicle_id) {
        this.practicle_id.set(practicle_id);
    }

    public Lab getLab() {
        return lab.get();
    }

    public SimpleObjectProperty<Lab> labProperty() {
        return lab;
    }

    public void setLab(Lab lab) {
        this.lab.set(lab);
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

    public String toString () { return this.getSubject() + " " + this.getStart_time() + " " + getLab(); }
}
