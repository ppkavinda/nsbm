package models;

import db.DBConnection;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Subject {
    private final SimpleIntegerProperty subject_code = new SimpleIntegerProperty(0);
    private final SimpleIntegerProperty credits = new SimpleIntegerProperty(0);
    private final SimpleIntegerProperty sem = new SimpleIntegerProperty(0);
    private final SimpleStringProperty name = new SimpleStringProperty("");
    private final SimpleDoubleProperty fee = new SimpleDoubleProperty(0);
    private final SimpleObjectProperty<Course> cou = new SimpleObjectProperty<>(new Course());

    private PreparedStatement stmt;
    private DBConnection conn = new DBConnection();

    public Subject () {
        this(0, 0, new Course(), 0, "", .00);
    }

    public Subject(int subject_code, int credits, Course cou, int sem, String name, Double fee) {
        setSubject_code(subject_code);
        setCredits(credits);
        setCou(cou);
        setSem(sem);
        setName(name);
        setFee(fee);

        System.out.println(this.cou + "LSDKFDS");
    }

    public Subject (int subject_code, int credits, int sem, String name, Double fee) {
        setSubject_code(subject_code);
        setCredits(credits);
        setName(name);
        setFee(fee);
    }

    public void configSubjectBox (ComboBox<Subject> subjectBox) {
        ResultSet rs = get();

        try {
            ObservableList<Subject> subjectBoxData = FXCollections.observableArrayList();
            while (rs.next()) {
                subjectBoxData.add(new Subject(
                        rs.getInt("subject_code"),
                        rs.getInt("credits"),
                        rs.getInt("sem"),
                        rs.getString("name"),
                        rs.getDouble("fee")
                ));
            }
            subjectBox.setItems(subjectBoxData);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void remove (int subject_id) {
        String sql = "DELETE FROM subject WHERE subject_code = ?";

        try {
            stmt = conn.connect().prepareStatement(sql);
            stmt.setInt(1, subject_id);
            stmt.executeUpdate();

            System.out.println(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void update (int subject_code, int credits, int course_id, int sem, String name, Double fee, int prev_id) {
        String sql = "UPDATE `subject` " +
                "SET `subject_code` = ?, `name` = ?, `credits` = ?, `fee` = ?, `course_id` = ?, `sem` = ? " +
                "WHERE `subject`.`subject_code` = ?;";

        try {
            stmt = conn.connect().prepareStatement(sql);
            stmt.setInt(1, subject_code);
            stmt.setString(2, name);
            stmt.setInt(3, credits);
            stmt.setDouble(4, fee);
            stmt.setInt(5, course_id);
            stmt.setInt(6, sem);
            stmt.setInt(7, prev_id);
            stmt.executeUpdate();
            System.out.println("Subject Updated");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void add (int subject_code, int credits, int course_id, int sem, String name, Double fee) {
        String sql = "INSERT INTO `subject` (`subject_code`, `name`, `credits`, `fee`, `course_id`, `sem`) " +
                "VALUES (?, ?, ?, ?, ?, ?);";

        try {
            stmt = conn.connect().prepareStatement(sql);
            stmt.setInt(1, subject_code);
            stmt.setString(2, name);
            stmt.setInt(3, credits);
            stmt.setDouble(4, fee);
            stmt.setInt(5, course_id);
            stmt.setInt(6, sem);
            stmt.executeUpdate();
            System.out.println(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet get () {
        String sql = "SELECT * FROM `subject` INNER JOIN nsbm_test.course ON subject.course_id = course.course_id";

        try {
            stmt = conn.connect().prepareStatement(sql);
            System.out.println(sql);
            return stmt.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // GETTERS and SETTERS
    public Course getCou() {
        return cou.get();
    }

    public SimpleObjectProperty<Course> couProperty() {
        return cou;
    }

    public void setCou(Course cou) {
        this.cou.set(cou);
    }

    public int getSubject_code() {
        return subject_code.get();
    }

    public SimpleIntegerProperty subject_codeProperty() {
        return subject_code;
    }

    public void setSubject_code(int subject_code) {
        this.subject_code.set(subject_code);
    }

    public int getCredits() {
        return credits.get();
    }

    public SimpleIntegerProperty creditsProperty() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits.set(credits);
    }

    public int getSem() {
        return sem.get();
    }

    public SimpleIntegerProperty semProperty() {
        return sem;
    }

    public void setSem(int sem) {
        this.sem.set(sem);
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

    public double getFee() {
        return fee.get();
    }

    public SimpleDoubleProperty feeProperty() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee.set(fee);
    }

    public String toString () { return getName(); }
}
