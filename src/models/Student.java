package models;

import com.mysql.jdbc.Statement;
import db.DBConnection;
import db.DbSingleton;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Student {
    private final SimpleIntegerProperty student_id = new SimpleIntegerProperty(0);
    private final SimpleObjectProperty<Faculty> faculty = new SimpleObjectProperty<>(new Faculty());
    private final SimpleObjectProperty<Course> course = new SimpleObjectProperty<>(new Course());
    private final SimpleStringProperty fname = new SimpleStringProperty("");
    private final SimpleStringProperty lname = new SimpleStringProperty("");
    private final SimpleStringProperty email = new SimpleStringProperty("");
    private final SimpleStringProperty address1 = new SimpleStringProperty("");
    private final SimpleStringProperty address2 = new SimpleStringProperty("");
    private final SimpleStringProperty tpno = new SimpleStringProperty("");
    private final SimpleObjectProperty<Date> dob = new SimpleObjectProperty<>(new Date());
    private final SimpleStringProperty gender = new SimpleStringProperty("");

    private Connection conn = DbSingleton.getInstance().getConnection();
    private PreparedStatement stmt;

    public Student () {
        this (0, new Faculty(), new Course(), "", "", "", "", "", "", new java.sql.Date(0), "");
    }

    public Student (int student_id, Faculty faculty, Course course, String fname, String lname, String email, String address1, String address2, String tpno, Date dob, String gender) {
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

    /**
     *
     * @param student_id
     * @return
     */
    public ResultSet getSelectableSubjects (int student_id) {
        String sql = "SELECT * FROM subject INNER JOIN course ON course.course_id = subject.course_id WHERE subject_code NOT IN (SELECT subject.subject_code FROM `subject` WHERE subject.compulsory = 1 UNION SELECT subject.subject_code FROM student_subject, subject WHERE student_subject.subject_code = SUBJECT.subject_code AND student_subject.student_id = ? ) AND SUBJECT.type = IF( ( SELECT COUNT(undergraduate.student_id) FROM undergraduate WHERE undergraduate.student_id = ? ) > 0, \"UG\", \"PG\" ) AND SUBJECT.course_id =(SELECT student.course_id FROM student WHERE student.student_id = ? )";

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, student_id);
            stmt.setInt(2, student_id);
            stmt.setInt(3, student_id);
            return stmt.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getSelectedSubs (int student_id) {
        String sql = "SELECT subject.subject_code, subject.name, subject.credits, subject.fee, subject.course_id, subject.sem, subject.compulsory, subject.type, course.*\n" +
                "FROM `subject` INNER JOIN course ON course.course_id = subject.course_id\n" +
                " WHERE subject.course_id =(SELECT student.course_id FROM student WHERE student.student_id = ? )\n" +
                "  AND subject.type = \n" +
                " IF( ( SELECT COUNT(undergraduate.student_id) FROM undergraduate WHERE undergraduate.student_id = ? ) > 0,\n" +
                " \"UG\", " +
                " \"PG\" " +
                " ) " +
                " AND subject.compulsory = 1 " +
                " UNION " +
                "SELECT subject.subject_code, subject.name, subject.credits, subject.fee, subject.course_id, subject.sem, subject.compulsory, subject.type, course.*\n" +
                "FROM student_subject, subject INNER JOIN course ON course.course_id = subject.course_id\n" +
                "WHERE\n" +
                "    student_subject.subject_code = subject.subject_code AND student_subject.student_id = ? AND subject.type = \n" +
                "    IF( ( SELECT COUNT(undergraduate.student_id) FROM undergraduate WHERE undergraduate.student_id = ? ) > 0,\n" +
                "    \t\"UG\",\n" +
                "    \t\"PG\" \n" +
                "     )";

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, student_id);
            stmt.setInt(2, student_id);
            stmt.setInt(3, student_id);
            stmt.setInt(4, student_id);
            return stmt.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void removeSubject (int sub_id, int student_id) {
        String sql = "DELETE FROM `student_subject` WHERE `student_subject`.`student_id` = ? AND `student_subject`.`subject_code` = ?";
        System.out.println(sql + " sub: " + sub_id + " id: " + student_id);
        setParams(student_id, sub_id, sql);
    }

    public void addSubject (int sub_id, int student_id) {
        String sql = "INSERT INTO `student_subject` (`student_id`, `subject_code`) VALUES (?, ?);";
        System.out.println(sub_id + " " + student_id);
        setParams(student_id, sub_id, sql);
    }

    private void setParams(int sub_id, int student_id, String sql) {
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, sub_id);
            stmt.setInt(2, student_id);
            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update (String email, String fname, String lname, String address1, String address2,
                        String telephone, Date dob, String gender, int fac_id, int course_id, int user_id) {
        String sql2 = "UPDATE `student` SET `fname` = ?, `lname` = ?, `email` = ?, `address1` = ?, `address2` = ?, " +
                "`telephone` = ?, `dob` = ?, `gender` = ?, `fac_id` = ?, `course_id` = ? WHERE `student`.`student_id` = ?;";

        try {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int add (int student_id, String email, String password, String fname, String lname, String address1, String address2,
                     String telephone, Date dob, String gender, int fac_id, int course_id) {
        String sql2 = "INSERT INTO `student` (`student_id`, `fname`, `lname`, `email`, `address1`, `address2`, `telephone`, `password`, `dob`, `gender`, `fac_id`, `course_id`)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try {
            stmt = conn.prepareStatement(sql2, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, student_id);
            stmt.setString(2, fname);
            stmt.setString(3, lname);
            stmt.setString(4, email);
            stmt.setString(5, address1);
            stmt.setString(6, address2);
            stmt.setString(7, telephone);
            stmt.setString(8, password);
            stmt.setDate(9, (java.sql.Date) dob);
            stmt.setString(10, gender);
            stmt.setInt(11, fac_id);
            stmt.setInt(12, course_id);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return (int) generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
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

    public String getTpno() {
        return tpno.get();
    }

    public SimpleStringProperty tpnoProperty() {
        return tpno;
    }

    public void setTpno(String tpno) {
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
