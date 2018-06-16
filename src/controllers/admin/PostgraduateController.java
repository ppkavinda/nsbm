package controllers.admin;

import helpers.MD5;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import models.*;

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
    @FXML private TableView<Postgraduate> pgTable;

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private Postgraduate pg = new Postgraduate();
    private Faculty faculty = new Faculty();
    private Postgraduate selectedRow;
    private Course course = new Course();
    private int addButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        drawTable();
        configFacultyBox();
        configCourseBox();
    }

    @FXML
    private void removeButtonClicked() {
        pg.remove(selectedRow.getStudent_id());
        refreshTable();
    }

    @FXML
    private void saveButtonClicked() {
        if (addButton == 1) {
            addUg();
            System.out.println("addButton");
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
        addButton = 0;
        setInputs(selectedRow);
        passwordField.setDisable(true);
        toDetails();
    }

    @FXML
    private void addButtonClicked() {
        System.out.println("Add Button Clicked");
        passwordField.setDisable(false);
        addButton = 1;
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

    private void configCourseBox() {
        ResultSet rs = course.get();

        try {
            ObservableList<Course> data = courseBox.getItems();
            while (rs.next()) {
                data.add(
                        new Course(
                                rs.getInt("course_id"),
                                rs.getString("name"),
                                rs.getInt("duration"),
                                rs.getInt("credit_limit"),
                                rs.getString("type"),
                                new Faculty()
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void configFacultyBox() {
        ResultSet rs = faculty.get();

        try {
            ObservableList<Faculty> data = facultyBox.getItems();
            while (rs.next()) {
                data.add(
                        new Faculty(
                                rs.getInt("faculty_id"),
                                rs.getString("name")
                        )
                );
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
