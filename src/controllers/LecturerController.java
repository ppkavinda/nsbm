package controllers;

import helpers.MD5;
import helpers.Validate;
import javafx.collections.FXCollections;
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
import java.util.ResourceBundle;

public class LecturerController implements Initializable {
    @FXML private ComboBox<Lecture> lecBox;
    @FXML private ListView<Lecture> lectureList;
    @FXML private VBox selectSubView;
    @FXML private TextField fnameField;
    @FXML private TextField lnameField;
    @FXML private TextField emailField;
    @FXML private TextField passwordField;
    @FXML private ComboBox<String> genderBox;
    @FXML private VBox lecDetails;
    @FXML private VBox lecList;
    @FXML private Button mainMenuButton;
    @FXML private TableView<Lecturer> lecturerTable;

    private Lecturer lecturer = new Lecturer();
    private Lecture lecture = new Lecture();
    private int addButtonClick;
    private Lecturer selectedRow;

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
        selectedRow = lecturerTable.getSelectionModel().getSelectedItem();
        System.out.println(selectedRow);
    }

    @FXML
    private void addLecture () {
        try {
            Lecture l = lecBox.getSelectionModel().getSelectedItem();
            if (l == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a lecture!", ButtonType.OK);
                alert.showAndWait();
            } else {
                lecturer.addLecture(selectedRow.getLecturer_id(), l.getLecture_id());
                lectureList.getItems().add(l);
                lecBox.getItems().remove(l);
            }

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Lecture Already allocated!", ButtonType.OK);
            alert.showAndWait();
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please save the Lecture first", ButtonType.OK);
            alert.showAndWait();

        }
    }

    @FXML
    private void removeLecture () {
        try {
            Lecture l = lectureList.getSelectionModel().getSelectedItem();
            lecturer.removeLecture(selectedRow.getLecturer_id(), l.getLecture_id());
            lectureList.getItems().remove(l);
            lecBox.getItems().add(l);

        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a Lecture!", ButtonType.OK);
            alert.showAndWait();
        }

    }

//    DETAIL VIEW
    @FXML
    private void lecButtonClicked () {
        configList(addButtonClick==1);
        configLecBox(addButtonClick==1);
        toLec();
    }
    @FXML
    private void saveButtonClicked() {
        if (addButtonClick == 1) {
            try {
                selectedRow.setLecturer_id(addLecturer());
                System.out.println("addButton");
            } catch (NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.showAndWait();
            }
        } else {
            editLecturer();
            System.out.println("editButton");
        }
        selectedRow = null;
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
            lecturer.remove(selectedRow.getLecturer_id());
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
            setInputs(selectedRow);
            passwordField.setDisable(true);
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

    private void toLec () {
        lecDetails.setVisible(false);
        lecList.setVisible(false);
        selectSubView.setVisible(true);
        selectSubView.toFront();
    }

    private void toList() {
        refreshTable();
        lecDetails.setVisible(false);
        selectSubView.setVisible(false);
        lecList.setVisible(true);
        lecList.toFront();
        clearInputs();
    }

    private void toDetails() {
        lecList.setVisible(false);
        selectSubView.setVisible(false);
        lecDetails.setVisible(true);
        lecDetails.toFront();
    }

    private void editLecturer () {
        lecturer.update (
                fnameField.getText(),
                lnameField.getText(),
                emailField.getText(),
                genderBox.getSelectionModel().getSelectedItem(),
                selectedRow.getLecturer_id());
    }

    private int addLecturer () throws NullPointerException {
        Validate.validateTf(new TextField[] {fnameField, lnameField, emailField, passwordField});
        int lec = lecturer.add(
                fnameField.getText(),
                lnameField.getText(),
                emailField.getText(),
                MD5.getHash(passwordField.getText()),
                genderBox.getSelectionModel().getSelectedItem(),
                2
        );
        clearInputs();
        return lec;
    }
    private void refreshTable () {
        lecturerTable.getItems().clear();
        drawTable();
    }
    private void drawTable() {
        ResultSet rs = lecturer.get();

        try {
            ObservableList<Lecturer> data = lecturerTable.getItems();

            while (rs.next()) {
                data.add(new Lecturer(
                    rs.getInt("lecturer_id"),
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

    private void configList (boolean newLecture) {
        ResultSet rs;
        if (newLecture) {   //    if adding new Lecturer clear lectures list
            rs = lecturer.getLectures(0);
        } else {            //    else getting allocated lectures
            rs = lecturer.getLectures(selectedRow.getLecturer_id());
        }
        try {

            ObservableList<Lecture> data = lectureList.getItems();
            data.clear();

            while (rs.next()) {
                System.out.println(rs.getString("name"));
                data.add(new Lecture(
                        rs.getInt("lecture.lecture_id"),
                        new LecHall(
                            rs.getInt("lec_hall.hall_id"),
                            rs.getString("lec_hall.name")
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
                        rs.getString("lecture.start_time"),
                        rs.getString("lecture.end_time")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void configLecBox (boolean newLecture) {

        ResultSet rs;
        if (newLecture) {   //    if newLecture is true getting all lectures
            rs = lecture.get();
        } else {            //    else getting allocated lectures
            rs = lecturer.getUnallocatedLectures(selectedRow.getLecturer_id());
        }
        try {
            ObservableList<Lecture> data = lecBox.getItems();
            data.clear();
            while (rs.next()) {
                data.add(new Lecture(
                        rs.getInt("lecture.lecture_id"),
                        new LecHall(
                                rs.getInt("lec_hall.hall_id"),
                                rs.getString("lec_hall.name")
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
                        rs.getString("lecture.start_time"),
                        rs.getString("lecture.end_time")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearInputs() {
        setInputs(new Lecturer());
    }

    private void setInputs(Lecturer lec) {
        fnameField.setText(lec.getFname());
        lnameField.setText(lec.getLname());
        emailField.setText(lec.getEmail());
        passwordField.setText("");
        genderBox.getSelectionModel().select(lec.getGender());
    }

}
