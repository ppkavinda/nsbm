package controllers;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import models.AlResult;
import models.Course;
import models.Faculty;
import models.Undergraduate;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UgTable {

    private Undergraduate ug = new Undergraduate();

    protected void refreshTable(TableView<Undergraduate> ugTable) {
        ugTable.getItems().clear();
        try {
            drawTable(ugTable);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot Add Undergraduate!", ButtonType.OK);
            alert.showAndWait();
        }
    }

    protected void drawTable(TableView<Undergraduate> ugTable) throws SQLException {
        try {
            ResultSet rs = ug.get();
            ObservableList<Undergraduate> data = ugTable.getItems();
            while (rs.next()) {
                data.add(new Undergraduate(
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
                        new AlResult(
                                rs.getString("al_result.sub1"),
                                rs.getString("al_result.sub2"),
                                rs.getString("al_result.sub2")
                        ),
                        rs.getInt("undergraduate.rank"),
                        rs.getDouble("undergraduate.z_score")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Cannot load the table.!");
        }
    }

}
