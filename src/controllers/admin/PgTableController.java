package controllers.admin;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import models.Course;
import models.Faculty;
import models.Postgraduate;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PgTableController {
    private Postgraduate pg = new Postgraduate();

    protected void refreshTable(TableView<Postgraduate> pgTable) {
        pgTable.getItems().clear();
        drawTable(pgTable);
    }

    protected void drawTable(TableView<Postgraduate> pgTable) {
        ResultSet rs = pg.get();
        try {
            ObservableList<Postgraduate> data = pgTable.getItems();
            while (rs.next()) {
                data.add(new Postgraduate(
                        rs.getInt("student_id"),
                        new Faculty(
                                rs.getInt("faculty.faculty_id"),
                                rs.getString("faculty.name")
                        ),
                        new Course(
                                rs.getInt("course.course_id"),
                                rs.getString("course.name"),
                                rs.getInt("course.duration"),
                                rs.getInt("course.credit_limit"),
                                rs.getString("course.type"),
                                new Faculty()
                        ),
                        rs.getString("student.fname"),
                        rs.getString("student.lname"),
                        rs.getString("student.email"),
                        rs.getString("student.address1"),
                        rs.getString("student.address2"),
                        rs.getInt("student.telephone"),
                        rs.getDate("student.dob"),
                        rs.getString("student.gender"),
                        rs.getInt("postgraduate.year_of_completion"),
                        rs.getString("postgraduate.institute"),
                        rs.getString("postgraduate.qualification_type")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
