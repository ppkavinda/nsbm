package controllers;

import helpers.Validate;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import models.Course;
import models.Faculty;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CourseController implements Initializable {

    @FXML private Button mainMenuButton;
    @FXML private ComboBox<Faculty> facultyBox;
    @FXML private TableView<Course> courseTable;
    @FXML private TextField nameField;
    @FXML private TextField durationField;
    @FXML private TextField creditField;
    @FXML private ComboBox<String> typeBox;

    private ObservableList<Course> data;

    private Course course = new Course();
    private Faculty faculty = new Faculty();

    // TODO: 6/7/2018 validate

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        faculty.configFacultyBox(facultyBox);
        drawTable();
    }

    @FXML
    private void toMainPanel () throws IOException {
            Scene scene = mainMenuButton.getScene();
            VBox root = FXMLLoader.load(getClass().getResource("/views/MainPanel.fxml"));
            scene.setRoot(root);
    }

    @FXML
    private void removeCourse () {
        ObservableList<Course> selectedItems = courseTable.getSelectionModel().getSelectedItems();
        try {
            course.remove(getSelectedRow().getCourse_id());
            selectedItems.forEach(data::remove);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot delete Course. Student are still following the course.", ButtonType.OK);
            alert.showAndWait();
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a Course", ButtonType.OK);
            alert.showAndWait();
        }
    }

   @FXML
   private void editCourse () {
        try {
            course.update(getSelectedRow().getCourse_id(),
                    nameField.getText(),
                    Integer.parseInt(durationField.getText()),
                    Integer.parseInt(creditField.getText()),
                    getTypeCode(),
                    facultyBox.getSelectionModel().getSelectedItem().getFaculty_id()
            );
            clearInputs();
            refreshTable();
            System.out.println("updated");
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a Course", ButtonType.OK);
            alert.showAndWait();
        }
   }

    // getting the selected Row of table
    private Course getSelectedRow () {
        return courseTable.getSelectionModel().getSelectedItem();
    }
    @FXML
    private void setSelectedRow () {
        setInputs(getSelectedRow());
    }

    @FXML
    private void addCourse () {
        try {
            Validate.validateTf(new TextField[] {nameField, creditField, durationField});

            course.add(
                    nameField.getText(),
                    Integer.parseInt(durationField.getText()),
                    Integer.parseInt(creditField.getText()),
                    getTypeCode(),
                    facultyBox.getSelectionModel().getSelectedItem().getFaculty_id()
            );
            clearInputs();
            refreshTable();
            System.out.println("Course Inserted");
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    //  convert comboBox options to ENUM
    private String getTypeCode () {
        if (typeBox.getSelectionModel().getSelectedIndex() == 0) {
            return "M";     // Masters
        } else {
            return "B";     // Bachelors
        }
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
                    new Faculty(
                            rs.getInt("faculty.faculty_id"),
                            rs.getString("faculty.name")
                    )   //  Faculty
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearInputs () {
        setInputs(new Course());
    }

    private void setInputs (Course c) {
        nameField.setText(c.getName());
        creditField.setText(String.valueOf(c.getCredit_limit()));
        durationField.setText(String.valueOf(c.getDuration()));
        typeBox.getSelectionModel().select(c.getType());
        facultyBox.getSelectionModel().select(c.getFaculty());
    }
}
