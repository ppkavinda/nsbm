package controllers.pg;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import models.Course;
import models.Faculty;
import models.Postgraduate;
import models.Undergraduate;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PgTable {
    private Postgraduate pg = new Postgraduate();

    protected void refreshTable(TableView<Postgraduate> pgTable) {
        pgTable.getItems().clear();
        try {
            drawTable(pgTable);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot Add Undergraduate!", ButtonType.OK);
            alert.showAndWait();
        }
    }

    protected void drawTable(TableView<Postgraduate> pgTable) throws SQLException {
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
                        rs.getString("student.telephone"),
                        rs.getDate("student.dob"),
                        rs.getString("student.gender"),
                        rs.getInt("postgraduate.year_of_completion"),
                        rs.getString("postgraduate.institute"),
                        rs.getString("postgraduate.qualification_type")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Cannot load the table.!");

        }
    }
}
