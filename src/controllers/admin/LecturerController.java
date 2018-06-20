package controllers.admin;

import helpers.MD5;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import models.Lecturer;
import models.Subject;
import models.Undergraduate;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class LecturerController implements Initializable {
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
    }

    private void editLecturer () {
        lecturer.update (
            fnameField.getText(),
            lnameField.getText(),
            emailField.getText(),
            genderBox.getSelectionModel().getSelectedItem(),
            selectedRow.getLecturer_id());
    }

    private void addLecturer () {
        lecturer.add(
            fnameField.getText(),
            lnameField.getText(),
            emailField.getText(),
            MD5.getHash(passwordField.getText()),
            genderBox.getSelectionModel().getSelectedItem(),
            2
        );
        clearInputs();
    }

    @FXML
    private void removeButtonClicked() {
        lecturer.remove(selectedRow.getLecturer_id());
        refreshTable();
    }

    @FXML
    private void saveButtonClicked() {
        if (addButtonClick == 1) {
            addLecturer();
            System.out.println("addButton");
        } else {
            editLecturer();
            System.out.println("editButton");
        }
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
        lecDetails.setVisible(false);
        lecDetails.toBack();
        lecList.setVisible(true);
        lecList.toFront();
        clearInputs();
    }

    private void toDetails() {
        lecList.setVisible(false);
        lecList.toBack();
        lecDetails.setVisible(true);
        lecDetails.toFront();
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

    private void clearInputs() {
        setInputs(new Lecturer());
    }

    private void setInputs(Lecturer lec) {
        fnameField.setText(lec.getFname());
        lnameField.setText(lec.getLname());
        emailField.setText(lec.getEmail());
        genderBox.getSelectionModel().select(lec.getGender());
    }

}
