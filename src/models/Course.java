package models;

import db.DBConnection;
import db.DbSingleton;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import java.sql.*;

public class Course {
    private final SimpleIntegerProperty course_id = new SimpleIntegerProperty(0);
    private final SimpleIntegerProperty duration = new SimpleIntegerProperty(0);
    private final SimpleIntegerProperty credit_limit = new SimpleIntegerProperty(0);
    private final SimpleObjectProperty<Faculty> faculty = new SimpleObjectProperty<>(new Faculty());
    private final SimpleStringProperty type = new SimpleStringProperty("");
    private final SimpleStringProperty name = new SimpleStringProperty("");

    private Connection conn = DbSingleton.getInstance().getConnection();

    private PreparedStatement stmt = null;

    public Course () {
        this(0, "", 0, 0, "", new Faculty());
    }

    public Course (int course_id, String name, int duration, int credit_limit, String type, Faculty faculty) {
        setCourse_id(course_id);
        setDuration(duration);
        setCredit_limit(credit_limit);
        setFaculty(faculty);
        setName(name);
        setType(type);
    }

    public void configCourseBox(ComboBox<Course> courseBox) {
        ResultSet rs = get();

        try {
            ObservableList<Course> data = courseBox.getItems();
            while (rs.next()) {
                data.add(
                        new Course(
                                rs.getInt("course_id"),
                                rs.getString("name"),
                                rs.getInt("duration"),
                                rs.getInt("credit_limit"),
                                rs.getString("type"),
                                new Faculty()
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void remove (int id) throws SQLException {
        String sql = "DELETE FROM course WHERE `course_id` = ?";

        stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();

        System.out.println(sql);

    }

    public void update (int course_id, String name, int duration, int credit_limit, String type, int faculty) {
        try {
            String sql = "UPDATE `course` " +
                    "SET `name` = ?, `duration` = ?, `credit_limit` = ?, `type` = ?, `faculty_id` = ? " +
                    "WHERE `course`.`course_id` = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setInt(2, duration);
            stmt.setInt(3, credit_limit);
            stmt.setString(4, type);
            stmt.setInt(5, faculty);
            stmt.setInt(6, course_id);

            stmt.executeUpdate();
            System.out.println(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add (String name, int duration, int credit_limit, String type, int faculty ) {
        try {
            String sql = "INSERT INTO `course` (`name`, `duration`, `credit_limit`, `type`, `faculty_id`) " +
                    "" +
                    "VALUES (?, ?, ?, ?, ?);";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setInt(2, duration);
            stmt.setInt(3, credit_limit);
            stmt.setString(4, type);
            stmt.setInt(5, faculty);

            stmt.executeUpdate();
            System.out.println(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // get all the details of the table;
    public ResultSet get () {

        try {
            String sql = "SELECT * FROM course INNER JOIN `faculty` ON faculty.faculty_id = course.faculty_id";
            stmt = conn.prepareStatement(sql);
            System.out.println(sql);

            return stmt.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // get particular row from the table
    public ResultSet get (int courseId) {
        try {
            String sql = "SELECT * FROM course WHERE course_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, courseId);
            System.out.println(sql);

            return stmt.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // GETTERS and SETTERS
    public SimpleIntegerProperty course_idProperty() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id.set(course_id);
    }

    public int getCourse_id() {
        return course_id.get();
    }

    public int getDuration() {
        return duration.get();
    }

    public SimpleIntegerProperty durationProperty() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration.set(duration);
    }

    public int getCredit_limit() {
        return credit_limit.get();
    }

    public SimpleIntegerProperty credit_limitProperty() {
        return credit_limit;
    }

    public void setCredit_limit(int credit_limit) {
        this.credit_limit.set(credit_limit);
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

    public Faculty getFaculty() {
        return faculty.get();
    }

    public SimpleObjectProperty<Faculty> facultyProperty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty.set(faculty);
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String toString () {
        return getName();
    }
}
