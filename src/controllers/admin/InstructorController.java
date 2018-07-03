package controllers.admin;

import helpers.MD5;
import helpers.Validate;
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
import java.util.ResourceBundle;

public class InstructorController implements Initializable {

    @FXML private ComboBox<Practicle> lecBox;
    @FXML private ListView<Practicle> lectureList;
    @FXML private VBox selectSubView;
    @FXML private TextField fnameField;
    @FXML private TextField lnameField;
    @FXML private TextField emailField;
    @FXML private TextField passwordField;
    @FXML private ComboBox<String> genderBox;
    @FXML private VBox lecDetails;
    @FXML private VBox lecList;
    @FXML private TableView<Instructor> instructorTable;
    @FXML private Button mainMenuButton;


    private Instructor instructor = new Instructor();
    private Practicle practicle = new Practicle();
    private int addButtonClick;
    private Instructor selectedRow;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        drawTable();
    }

    @FXML
    private void toMainPanel () throws IOException {
        Scene scene = mainMenuButton.getScene();
        VBox root = FXMLLoader.load(getClass().getResource("/views/MainPanel.fxml"));
        scene.setRoot(root);

    }

    @FXML
    private void getSelectedRow() {
        selectedRow = instructorTable.getSelectionModel().getSelectedItem();
    }


    @FXML   //  add **Practicle
    private void addLecture () {
        try {
            Practicle l = lecBox.getSelectionModel().getSelectedItem();
            if (l == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a practicle!", ButtonType.OK);
                alert.showAndWait();
            } else {
                instructor.addPracticle(selectedRow.getInstructor_id(), l.getPracticle_id());
                lectureList.getItems().add(l);
                lecBox.getItems().remove(l);
            }

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please save the Instructor first", ButtonType.OK);
            alert.showAndWait();

        }
    }

    @FXML   //  remove ** practicle
    private void removeLecture () {
        try {
            Practicle l = lectureList.getSelectionModel().getSelectedItem();
            instructor.removePracticle(selectedRow.getInstructor_id(), l.getPracticle_id());
            lectureList.getItems().remove(l);
            lecBox.getItems().add(l);

        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a Practical!", ButtonType.OK);
            alert.showAndWait();
        }

    }

    //    detail view
    @FXML
    private void lecButtonClicked () {
        configList(addButtonClick==1);
        configLecBox(addButtonClick==1);
        toPrac();
    }
    @FXML
    private void saveButtonClicked() {
        if (addButtonClick == 1) {
            try {
                System.out.println("addButton");
                selectedRow.setInstructor_id(addLecturer());
            } catch (NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.showAndWait();

            }
        } else {
            editLecturer();
            System.out.println("editButton");
        }
        selectedRow = null;
        toList();
    }
    @FXML
    private void cancelButtonClicked() {
        System.out.println("Cancel Button Clicked");
        toList();
        selectedRow = null;
    }

    //    TABLE VIEW **********************
    @FXML
    private void removeButtonClicked() {
        try {
            instructor.remove(selectedRow.getInstructor_id());
            refreshTable();
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a Lecture!", ButtonType.OK);
            alert.showAndWait();
        }
    }
    @FXML
    private void editButtonClicked() {
        System.out.println("Edit Button Clicked");
        try {
            addButtonClick = 0;
            passwordField.setDisable(true);
            setInputs(selectedRow);
            toDetails();
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a Lecture!", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    private void addButtonClicked() {
        System.out.println("Add Button Clicked");
        passwordField.setDisable(false);
        addButtonClick = 1;
        toDetails();
    }

    private void toPrac () {
        lecDetails.setVisible(false);
        lecList.setVisible(false);
        selectSubView.setVisible(true);
        selectSubView.toFront();
    }

    private void toList() {
        refreshTable();
        selectSubView.setVisible(false);
        lecDetails.setVisible(false);
        lecList.toFront();
        lecList.setVisible(true);
        clearInputs();
    }

    private void toDetails() {
        lecList.setVisible(false);
        selectSubView.setVisible(false);
        lecDetails.setVisible(true);
        lecDetails.toFront();
    }

    private void editLecturer () {
        instructor.update (
                fnameField.getText(),
                lnameField.getText(),
                emailField.getText(),
                genderBox.getSelectionModel().getSelectedItem(),
                selectedRow.getInstructor_id());
    }

    private int addLecturer () throws NullPointerException {
        Validate.validateTf(new TextField[] {fnameField, lnameField, emailField, passwordField});
        int ins = instructor.add(
                fnameField.getText(),
                lnameField.getText(),
                emailField.getText(),
                MD5.getHash(passwordField.getText()),
                genderBox.getSelectionModel().getSelectedItem(),
                3
        );
        clearInputs();
        return ins;
    }

    private void refreshTable () {
        instructorTable.getItems().clear();
        drawTable();
    }

    private void drawTable() {
        ResultSet rs = instructor.get();

        try {
            ObservableList<Instructor> data = instructorTable.getItems();

            while (rs.next()) {
                data.add(new Instructor(
                        rs.getInt("instructor_id"),
                        rs.getString("fname"),
                        rs.getString("lname"),
                        rs.getString("email"),
                        rs.getString("gender")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void configList (boolean newInstructor) {
        ResultSet rs;
        if (newInstructor) {
            rs = instructor.getPracticles(0);
        } else {
            rs = instructor.getPracticles(selectedRow.getInstructor_id());
        }
        try {

            ObservableList<Practicle> data = lectureList.getItems();
            data.clear();

            while (rs.next()) {
                System.out.println(rs.getString("name"));
                data.add(new Practicle(
                        rs.getInt("practicle.practicle_id"),
                        new Lab(
                                rs.getInt("lab_id"),
                                rs.getString("lab.name")
                        ),
                        new Subject(
                                rs.getInt("subject.subject_code"),
                                rs.getInt("subject.credits"),
                                new Course(),
                                rs.getInt("subject.sem"),
                                rs.getString("subject.name"),
                                rs.getDouble("subject.fee"),
                                rs.getInt("subject.compulsory"),
                                rs.getString("subject.type")
                        ),
                        rs.getString("start_time"),
                        rs.getString("end_time")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void configLecBox (boolean newInstructor) {

        ResultSet rs;
        if (newInstructor) {
            rs = practicle.get();
        } else {
            rs = instructor.getUnallocatedPracticles(selectedRow.getInstructor_id());
        }

        try {
            ObservableList<Practicle> data = lecBox.getItems();
            data.clear();
            while (rs.next()) {
                data.add(new Practicle(
                        rs.getInt("practicle.practicle_id"),
                        new Lab(
                                rs.getInt("lab_id"),
                                rs.getString("lab.name")
                        ),
                        new Subject(
                                rs.getInt("subject.subject_code"),
                                rs.getInt("subject.credits"),
                                new Course(),
                                rs.getInt("subject.sem"),
                                rs.getString("subject.name"),
                                rs.getDouble("subject.fee"),
                                rs.getInt("subject.compulsory"),
                                rs.getString("subject.type")
                        ),
                        rs.getString("start_time"),
                        rs.getString("end_time")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearInputs() {
        setInputs(new Instructor());
    }

    private void setInputs(Instructor instructor) {
        fnameField.setText(instructor.getFname());
        lnameField.setText(instructor.getLname());
        emailField.setText(instructor.getEmail());
        genderBox.getSelectionModel().select(instructor.getGender());
    }

}
