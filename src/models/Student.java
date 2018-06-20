package models;

import com.mysql.jdbc.Statement;
import db.DBConnection;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

class Student {
    private final SimpleIntegerProperty student_id = new SimpleIntegerProperty(0);
    private final SimpleObjectProperty<Faculty> faculty = new SimpleObjectProperty<>(new Faculty());
    private final SimpleObjectProperty<Course> course = new SimpleObjectProperty<>(new Course());
    private final SimpleStringProperty fname = new SimpleStringProperty("");
    private final SimpleStringProperty lname = new SimpleStringProperty("");
    private final SimpleStringProperty email = new SimpleStringProperty("");
    private final SimpleStringProperty address1 = new SimpleStringProperty("");
    private final SimpleStringProperty address2 = new SimpleStringProperty("");
    private final SimpleIntegerProperty tpno = new SimpleIntegerProperty(0);
    private final SimpleObjectProperty<Date> dob = new SimpleObjectProperty<>(new Date());
    private final SimpleStringProperty gender = new SimpleStringProperty("");

    private DBConnection conn = new DBConnection();
    private PreparedStatement stmt;

    public Student () {
        this (0, new Faculty(), new Course(), "", "", "", "", "", 0, new java.sql.Date(0), "");
    }

    public Student (int student_id, Faculty faculty, Course course, String fname, String lname, String email, String address1, String address2, int tpno, Date dob, String gender) {
        setStudent_id(student_id);
        setFaculty(faculty);
        setCourse(course);
        setFname(fname);
        setLname(lname);
        setEmail(email);
        setAddress1(address1);
        setAddress2(address2);
        setTpno(tpno);
        setDob(dob);
        setGender(gender);
    }

    public void update (String email, String fname, String lname, String address1, String address2,
                        int telephone, Date dob, String gender, int fac_id, int course_id, int user_id) {
        String sql2 = "UPDATE `student` SET `fname` = ?, `lname` = ?, `email` = ?, `address1` = ?, `address2` = ?, " +
                "`telephone` = ?, `dob` = ?, `gender` = ?, `fac_id` = ?, `course_id` = ? WHERE `student`.`student_id` = ?;";

        try {
            stmt = conn.connect().prepareStatement(sql2);
            stmt.setString(1, fname);
            stmt.setString(2, lname);
            stmt.setString(3, email);
            stmt.setString(4, address1);
            stmt.setString(5, address2);
            stmt.setInt(6, telephone);
            stmt.setDate(7, (java.sql.Date) dob);
            stmt.setString(8, gender);
            stmt.setInt(9, fac_id);
            stmt.setInt(10, course_id);
            stmt.setInt(11, user_id);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void add (int student_id, String email, String password, String fname, String lname, String address1, String address2,
                     int telephone, Date dob, String gender, int fac_id, int course_id) {
        String sql2 = "INSERT INTO `student` (`student_id`, `fname`, `lname`, `email`, `address1`, `address2`, `telephone`, `password`, `dob`, `gender`, `fac_id`, `course_id`)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try {
            stmt = conn.connect().prepareStatement(sql2);
            stmt.setInt(1, student_id);
            stmt.setString(2, fname);
            stmt.setString(3, lname);
            stmt.setString(4, email);
            stmt.setString(5, address1);
            stmt.setString(6, address2);
            stmt.setInt(7, telephone);
            stmt.setString(8, password);
            stmt.setDate(9, (java.sql.Date) dob);
            stmt.setString(10, gender);
            stmt.setInt(11, fac_id);
            stmt.setInt(12, course_id);
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getAddress1() {
        return address1.get();
    }

    public SimpleStringProperty address1Property() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1.set(address1);
    }

    public String getAddress2() {
        return address2.get();
    }

    public SimpleStringProperty address2Property() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2.set(address2);
    }

    public int getTpno() {
        return tpno.get();
    }

    public SimpleIntegerProperty tpnoProperty() {
        return tpno;
    }

    public void setTpno(int tpno) {
        this.tpno.set(tpno);
    }

    public int getStudent_id() {
        return student_id.get();
    }

    public SimpleIntegerProperty student_idProperty() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id.set(student_id);
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

    public Course getCourse() {
        return course.get();
    }

    public SimpleObjectProperty<Course> courseProperty() {
        return course;
    }

    public void setCourse(Course course) {
        this.course.set(course);
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

    public Date getDob() {
        return dob.get();
    }

    public SimpleObjectProperty<Date> dobProperty() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob.set(dob);
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

    public String toString () { return getFname() + " " + getLname(); }
}
