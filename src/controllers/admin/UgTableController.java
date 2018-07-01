package controllers.admin;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import models.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UgTableController {

    private Undergraduate ug = new Undergraduate();

    protected void refreshTable(TableView<Undergraduate> ugTable) {
        ugTable.getItems().clear();
        drawTable(ugTable);
    }

    protected void drawTable(TableView<Undergraduate> ugTable) {
        ResultSet rs = ug.get();
        try {
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
                        rs.getInt("student.telephone"),
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
        }
    }

}
