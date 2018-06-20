package controllers.admin;

import helpers.MD5;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import models.Instructor;
import models.Lecturer;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class InstructorController implements Initializable {

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

    private void editLecturer () {
        instructor.update (
                fnameField.getText(),
                lnameField.getText(),
                emailField.getText(),
                genderBox.getSelectionModel().getSelectedItem(),
                selectedRow.getInstructor_id());
    }

    private void addLecturer () {
        instructor.add(
                fnameField.getText(),
                lnameField.getText(),
                emailField.getText(),
                MD5.getHash(passwordField.getText()),
                genderBox.getSelectionModel().getSelectedItem(),
                3
        );
        clearInputs();
    }

    @FXML
    private void removeButtonClicked() {
        instructor.remove(selectedRow.getInstructor_id());
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
