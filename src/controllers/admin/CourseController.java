package controllers.admin;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import models.Course;
import models.Faculty;

import javax.swing.*;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CourseController implements Initializable {

    @FXML private ComboBox<String> facultyBox;
    @FXML private TableView<Course> courseTable;
    @FXML private TextField nameField;
    @FXML private TextField durationField;
    @FXML private TextField creditField;
    @FXML private ComboBox<String> typeBox;

    private Course selectedRow;
    private ObservableList<Course> data;

    private Course course = new Course();
    private Faculty faculty = new Faculty();

    // TODO: 6/7/2018 validate

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ResultSet rs = faculty.get();
        try {
            System.out.println("faculty combBox");
            while (rs.next()) {
                facultyBox.getItems().add(rs.getInt(1) + ": " + rs.getString(2));
                System.out.println("faculty: " + rs.getInt(1) + ": " + rs.getString(2));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        drawTable();
    }

    @FXML
    private void removeCourse (ActionEvent e) {
        ObservableList<Course> selectedItems = courseTable.getSelectionModel().getSelectedItems();
        selectedItems.forEach(data::remove);
        faculty.remove(selectedRow.getCourse_id());
    }

   @FXML
   private void editCourse (ActionEvent e) {
        course.update(selectedRow.getCourse_id(),
            nameField.getText(),
            Integer.parseInt(durationField.getText()),
            Integer.parseInt(creditField.getText()),
            getTypeCode(),
            getFacultyCode()
        );
        refreshTable();
        System.out.println("updated");
   }

    // getting the selected Row of table
    @FXML
    private void getSelectedRow () {
        selectedRow= courseTable.getSelectionModel().getSelectedItem();
        nameField.setText(selectedRow.getName());
        durationField.setText(String.valueOf(selectedRow.getDuration()));
        creditField.setText(String.valueOf(selectedRow.getCredit_limit()));
        typeBox.getSelectionModel().select(selectedRow.getType());
        facultyBox.getSelectionModel().select(selectedRow.getFaculty().getName());
    }

    @FXML
    private void addCourse (ActionEvent e) {
        course.add(
                nameField.getText(),
                Integer.parseInt(durationField.getText()),
                Integer.parseInt(creditField.getText()),
                getTypeCode(),
                getFacultyCode()
        );
        refreshTable();
        System.out.println("Course Inserted");
    }

    //  convert comboBox options to ENUM
    private String getTypeCode () {
        if (typeBox.getSelectionModel().getSelectedIndex() == 0) {
            return "M";     // Masters
        } else {
            return "B";     // Bachelors
        }
    }

    // get the faculty_id from comboBox option
    private int getFacultyCode () {
        return Integer.parseInt(String.valueOf(facultyBox.getSelectionModel().getSelectedItem().charAt(0)));
    }

    private void refreshTable () {
        courseTable.getItems().clear();
        drawTable();
    }

    private void drawTable() {
        ResultSet rs = course.get();

        try {
            while (rs.next()) {
                data = courseTable.getItems();
                //  adding courses to the table
                data.add(new Course(
                    rs.getInt(1),   // id
                    rs.getString(2),   //  name
                    rs.getInt(3),   //  Duration
                    rs.getInt(4),   //  Credit Limit
                    rs.getString(5),    //  Type
                    new Faculty(rs.getInt("faculty.faculty_id"), rs.getString("faculty.name"))   //  Faculty
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
