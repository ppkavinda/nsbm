package controllers.admin;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

    private Course selectedRow;
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
            facultyBox.getSelectionModel().getSelectedItem().getFaculty_id()
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
        facultyBox.getSelectionModel().select(selectedRow.getFaculty());
    }

    @FXML
    private void addCourse (ActionEvent e) {
        course.add(
                nameField.getText(),
                Integer.parseInt(durationField.getText()),
                Integer.parseInt(creditField.getText()),
                getTypeCode(),
                facultyBox.getSelectionModel().getSelectedItem().getFaculty_id()
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
}
