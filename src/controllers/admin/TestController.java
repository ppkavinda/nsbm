package controllers.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import models.Subject;
import models.Test;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

public class TestController implements Initializable {

    Test test = new Test();
    Subject subject = new Subject();
    @FXML private DatePicker dateField;
    @FXML private TextField startField;
    @FXML private TextField endField;
    @FXML private ComboBox<Subject> subjectBox;
    @FXML private ComboBox typeBox;
    @FXML private TableView<Test> testTable;
    @FXML private Button mainMenuButton;

    private ObservableList<Subject> subjectBoxData = FXCollections.observableArrayList();
    private Test selectedRow;
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configSubjectBox();
        drawTable();
    }

    @FXML
    private void toMainPanel () throws IOException {
        Scene scene = mainMenuButton.getScene();
        VBox root = FXMLLoader.load(getClass().getResource("/views/MainPanel.fxml"));
        scene.setRoot(root);

    }

    @FXML
    private void removeTest () {
        test.remove(selectedRow.getTest_id());
        refreshTable();
        clearInputs();
    }

    @FXML
    private void editTest () {
        test.update(
            java.sql.Date.valueOf(dateField.getValue()),
            startField.getText(),
            endField.getText(),
            subjectBox.getSelectionModel().getSelectedItem().getSubject_code(),
            typeBox.getSelectionModel().getSelectedItem().toString(),
            selectedRow.getTest_id()
        );
        clearInputs();
        refreshTable();
    }

    @FXML
    private void getSelectedRow () {
        selectedRow = testTable.getSelectionModel().getSelectedItem();
        System.out.println(selectedRow.getDate().getClass());
        dateField.setValue(LocalDate.parse(selectedRow.getDate().toString(), dtf));
        startField.setText(selectedRow.getStart_time());
        endField.setText(selectedRow.getEnd_time());
        subjectBox.getSelectionModel().select(selectedRow.getSubject());
        typeBox.getSelectionModel().select(selectedRow.getType());
    }

    @FXML
    private void addTest() {
        test.add(
            java.sql.Date.valueOf(dateField.getValue()),
            startField.getText(),
            endField.getText(),
            subjectBox.getSelectionModel().getSelectedItem().getSubject_code(),
            typeBox.getSelectionModel().getSelectedItem().toString()
        );
        refreshTable();
        clearInputs();
    }

    private void refreshTable() {
        testTable.getItems().clear();
        drawTable();
    }

    private void drawTable() {
        ResultSet rs = test.get();

        try {
            ObservableList<Test> data = testTable.getItems();
            while (rs.next()) {
                data.add(new Test(
                    rs.getInt("test_id"),
                    new Subject(
                        rs.getInt("subject.subject_code"),
                        rs.getInt("subject.credits"),
                        rs.getInt("subject.sem"),
                        rs.getString("subject.name"),
                        rs.getDouble("subject.fee")
                    ),
                    rs.getDate("date"),
                    rs.getString("start_time"),
                    rs.getString("end_time"),
                    rs.getString("type")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void configSubjectBox() {
        ResultSet rs = subject.get();

        try {
            while (rs.next()) {
                subjectBoxData.add(new Subject(
                    rs.getInt("subject_code"),
                    rs.getInt("credits"),
                    rs.getInt("sem"),
                    rs.getString("name"),
                    rs.getDouble("fee")
                ));
            }
            subjectBox.setItems(subjectBoxData);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearInputs() {
        dateField.setValue(null);
        startField.setText("");
        endField.setText("");
        subjectBox.getSelectionModel().select(null);
        typeBox.getSelectionModel().select(null);
    }
}
