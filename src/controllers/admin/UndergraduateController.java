package controllers.admin;

import helpers.MD5;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import models.AlResult;
import models.Course;
import models.Faculty;
import models.Undergraduate;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class UndergraduateController implements Initializable {
    @FXML private TextField sub1Field;
    @FXML private TextField sub3Field;
    @FXML private TextField sub2Field;
    @FXML private TextField sub4Field;
    @FXML private TextField passwordField;
    @FXML private ComboBox<Faculty> facultyBox;
    @FXML private ComboBox<Course> courseBox;
    @FXML private ComboBox<String> genderBox;
    @FXML private TextField fnameField;
    @FXML private TextField lnameField;
    @FXML private TextField emailField;
    @FXML private TextField rankField;
    @FXML private TextField zscoreField;
    @FXML private TextField addressField1;
    @FXML private TextField addressField2;
    @FXML private TextField teleField;
    @FXML private DatePicker dobField;
    @FXML private VBox ugList;
    @FXML private VBox ugDetails;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private Button mainMenuButton;
    @FXML private TableView<Undergraduate> ugTable;

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private Undergraduate ug = new Undergraduate();
    private Faculty faculty = new Faculty();
    private Undergraduate selectedRow;
    private Course course = new Course();
    private int addButtonClick;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        drawTable();
        faculty.configFacultyBox(facultyBox);
        course.configCourseBox(courseBox);
    }

    @FXML
    private void toMainPanel () throws IOException {
        Scene scene = mainMenuButton.getScene();
        VBox root = FXMLLoader.load(getClass().getResource("/views/MainPanel.fxml"));
        scene.setRoot(root);

    }

    @FXML
    private void removeButtonClicked() {
        ug.remove(selectedRow.getStudent_id());
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
        ug.update(
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
            Integer.valueOf(rankField.getText()),
            Double.valueOf(zscoreField.getText()),
            sub1Field.getText(),
            sub2Field.getText(),
            sub3Field.getText(),
            selectedRow.getStudent_id()
        );

        toList();
    }

    @FXML
    private void getSelectedRow() {
        selectedRow = ugTable.getSelectionModel().getSelectedItem();
    }

    private void addUg() {
        ug.add(
            emailField.getText(),
            MD5.getHash(passwordField.getText()),
            4,
            fnameField.getText(),
            lnameField.getText(),
            addressField1.getText(),
            addressField2.getText(),
            Integer.parseInt(teleField.getText()),
            java.sql.Date.valueOf(dobField.getValue()),
            genderBox.getSelectionModel().getSelectedItem(),
            facultyBox.getSelectionModel().getSelectedItem().getFaculty_id(),
            courseBox.getSelectionModel().getSelectedItem().getCourse_id(),
            Integer.parseInt(rankField.getText()),
            Double.valueOf(zscoreField.getText()),
            sub1Field.getText(),
            sub2Field.getText(),
            sub3Field.getText()
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
        ugDetails.setVisible(false);
        ugDetails.toBack();
        ugList.setVisible(true);
        ugList.toFront();
        clearInputs();
    }

    private void toDetails() {
        ugList.setVisible(false);
        ugList.toBack();
        ugDetails.setVisible(true);
        ugDetails.toFront();
    }

    private void refreshTable() {
        ugTable.getItems().clear();
        drawTable();
    }

    private void drawTable() {
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

    private void clearInputs() {
        setInputs(new Undergraduate());
    }

    private void setInputs(Undergraduate ug) {
        System.out.println(ug.getRank());
        emailField.setText(ug.getEmail());
        passwordField.setText(null);
        fnameField.setText(ug.getFname());
        lnameField.setText(ug.getLname());
        addressField1.setText(ug.getAddress1());
        addressField2.setText(ug.getAddress2());
        teleField.setText(String.valueOf(ug.getTpno()));
        dobField.setValue(LocalDate.parse(ug.getDob().toString(), dtf));
        genderBox.getSelectionModel().select(ug.getGender());
        facultyBox.getSelectionModel().select(ug.getFaculty());
        courseBox.getSelectionModel().select(ug.getCourse());
        rankField.setText(String.valueOf(ug.getRank()));
        zscoreField.setText(String.valueOf(ug.getZ_score()));
        sub1Field.setText(ug.getAl_result().getSub1());
        sub2Field.setText(ug.getAl_result().getSub2());
        sub3Field.setText(ug.getAl_result().getSub3());
    }
}
