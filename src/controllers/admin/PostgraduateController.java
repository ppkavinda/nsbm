package controllers.admin;

import helpers.MD5;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import models.*;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class PostgraduateController implements Initializable{

    @FXML private TextField passwordField;
    @FXML private ComboBox<Faculty> facultyBox;
    @FXML private ComboBox<Course> courseBox;
    @FXML private ComboBox<String> genderBox;
    @FXML private TextField fnameField;
    @FXML private TextField lnameField;
    @FXML private TextField emailField;
    @FXML private TextField qtypeField;
    @FXML private TextField instituteField;
    @FXML private TextField qyearField;
    @FXML private TextField addressField1;
    @FXML private TextField addressField2;
    @FXML private TextField teleField;
    @FXML private DatePicker dobField;
    @FXML private VBox pgList;
    @FXML private VBox pgDetails;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private Button mainMenuButton;
    @FXML private TableView<Postgraduate> pgTable;

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private Postgraduate pg = new Postgraduate();
    private Faculty faculty = new Faculty();
    private Postgraduate selectedRow;
    private Course course = new Course();
    private int addButtonClick;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        drawTable();
        faculty.configFacultyBox(facultyBox);
        course.configCourseBox(courseBox);
//        ObservableList<Course> data = courseBox.getItems();
//        facultyBox.valueProperty().addListener((observable, oldValue, newValue) -> {
//            data.removeIf(course1 -> course1.getFaculty().getFaculty_id() == 6);
//            courseBox.setItems(data);
//            System.out.println(observable.getValue().getFaculty_id());
//            System.out.println(newValue.getFaculty_id());
//        });
    }

    @FXML
    private void toMainPanel () throws IOException {
        Scene scene = mainMenuButton.getScene();
        VBox root = FXMLLoader.load(getClass().getResource("/views/MainPanel.fxml"));
        scene.setRoot(root);
    }

    @FXML
    private void removeButtonClicked() {
        pg.remove(selectedRow.getStudent_id());
        refreshTable();
    }

    @FXML
    private void saveButtonClicked() {
        if (addButtonClick == 1) {
            addUg();
            System.out.println("addButtonClick");
        } else {
            editUg();
            System.out.println("editButton");
        }
    }

    private void editUg() {
        pg.update(
                emailField.getText(),
                fnameField.getText(),
                lnameField.getText(),
                addressField1.getText(),
                addressField2.getText(),
                Integer.valueOf(teleField.getText()),
                java.sql.Date.valueOf(dobField.getValue()),
                genderBox.getSelectionModel().getSelectedItem(),
                facultyBox.getSelectionModel().getSelectedItem().getFaculty_id(),
                courseBox.getSelectionModel().getSelectedItem().getCourse_id(),
                qtypeField.getText(),
                instituteField.getText(),
                Integer.valueOf(qyearField.getText()),
                selectedRow.getStudent_id()
        );

        toList();
    }

    @FXML
    private void getSelectedRow() {
        selectedRow = pgTable.getSelectionModel().getSelectedItem();
    }

    private void addUg() {
        pg.add(
                emailField.getText(),
                MD5.getHash(passwordField.getText()),
                5,
                fnameField.getText(),
                lnameField.getText(),
                addressField1.getText(),
                addressField2.getText(),
                Integer.parseInt(teleField.getText()),
                java.sql.Date.valueOf(dobField.getValue()),
                genderBox.getSelectionModel().getSelectedItem(),
                facultyBox.getSelectionModel().getSelectedItem().getFaculty_id(),
                courseBox.getSelectionModel().getSelectedItem().getCourse_id(),
                qtypeField.getText(),
                instituteField.getText(),
                Integer.valueOf(qyearField.getText())
        );

        toList();
    }

    @FXML
    private void cancelButtonClicked() {
        System.out.println("Cancel Button Clicked");
        toList();
    }

    @FXML
    private void editButtonClicked() {
        System.out.println("Edit Button Clicked");
        addButtonClick = 0;
        setInputs(selectedRow);
        passwordField.setDisable(true);
        toDetails();
    }

    @FXML
    private void addButtonClicked() {
        System.out.println("Add Button Clicked");
        passwordField.setDisable(false);
        addButtonClick = 1;
        toDetails();
    }

    private void toList() {
        refreshTable();
        pgDetails.setVisible(false);
        pgDetails.toBack();
        pgList.setVisible(true);
        pgList.toFront();
        clearInputs();
    }

    private void toDetails() {
        pgList.setVisible(false);
        pgList.toBack();
        pgDetails.setVisible(true);
        pgDetails.toFront();
    }

    private void refreshTable() {
        pgTable.getItems().clear();
        drawTable();
    }

    private void drawTable() {
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

    private void clearInputs() {
        setInputs(new Postgraduate());
    }

    private void setInputs(Postgraduate pg) {
        System.out.println(pg.getInstitute());
        emailField.setText(pg.getEmail());
        passwordField.setText(null);
        fnameField.setText(pg.getFname());
        lnameField.setText(pg.getLname());
        addressField1.setText(pg.getAddress1());
        addressField2.setText(pg.getAddress2());
        dobField.setValue(LocalDate.parse(pg.getDob().toString(), dtf));
        genderBox.getSelectionModel().select(pg.getGender());
        facultyBox.getSelectionModel().select(pg.getFaculty());
        courseBox.getSelectionModel().select(pg.getCourse());
        qtypeField.setText(String.valueOf(pg.getQualification_type()));
        instituteField.setText(String.valueOf(pg.getInstitute()));
        qyearField.setText(String.valueOf(pg.getYear_of_completion()));
    }
}
