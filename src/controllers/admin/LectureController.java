package controllers.admin;

import javafx.collections.FXCollections;
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
import models.Course;
import models.LecHall;
import models.Lecture;
import models.Subject;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LectureController implements Initializable {
    @FXML private TextField startField;
    @FXML private TextField endField;
    @FXML private ComboBox<LecHall> locationBox;
    @FXML private ComboBox<Subject> subjectBox;
    @FXML private TableView<Lecture> lectureTable;
    @FXML private Button mainMenuButton;

    private Lecture lecture = new Lecture();
    private LecHall lec_hall = new LecHall();
    private Subject subject = new Subject();
    private Lecture selectRow;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configSubjectBox();
        configLocationBox();
        drawTable ();
    }

    @FXML
    private void toMainPanel () throws IOException {
        Scene scene = mainMenuButton.getScene();
        VBox root = FXMLLoader.load(getClass().getResource("/views/MainPanel.fxml"));
        scene.setRoot(root);

    }

    @FXML
    private void removeLecture () {
        lecture.remove(selectRow.getLecture_id());
        setInputs(new Lecture());
        refreshTable();
    }

    @FXML
    private void editLecture () {
        lecture.update(
            startField.getText(),
            endField.getText(),
            locationBox.getSelectionModel().getSelectedItem().getHall_id(),
            subjectBox.getSelectionModel().getSelectedItem().getSubject_code(),
            selectRow.getLecture_id()
        );
        setInputs(new Lecture());
        refreshTable();
    }

    @FXML
    private void getSelectedRow () {
        selectRow = lectureTable.getSelectionModel().getSelectedItem();
        setInputs(selectRow);
    }

    @FXML
    private void addLecture () {
        lecture.add(
            startField.getText(),
            endField.getText(),
            locationBox.getSelectionModel().getSelectedItem().getHall_id(),
            subjectBox.getSelectionModel().getSelectedItem().getSubject_code()
        );

        refreshTable();
        setInputs(new Lecture());
    }

    private void refreshTable () {
        lectureTable.getItems().clear();
        drawTable();
    }

    private void drawTable () {
        ResultSet rs = lecture.get();

        try {
            ObservableList<Lecture> data = lectureTable.getItems();

            while (rs.next()) {
                data.add(new Lecture(
                        rs.getInt("lecture_id"),
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
                        rs.getString("start_time"),
                        rs.getString("end_time")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void configLocationBox () {
        ResultSet rs = lec_hall.get();

        try {
            ObservableList<LecHall> locationBoxData = FXCollections.observableArrayList();
            while (rs.next()) {
                locationBoxData.add(new LecHall(
                        rs.getInt("hall_id"),
                        rs.getString("name")
                ));
            }
            locationBox.setItems(locationBoxData);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void configSubjectBox () {
        ResultSet rs = subject.get();

        try {
            ObservableList<Subject> subjectBoxData = FXCollections.observableArrayList();
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

    private void setInputs (Lecture l) {
        startField.setText(l.getStart_time());
        endField.setText(l.getEnd_time());
        locationBox.getSelectionModel().select(l.getLec_hall());
        subjectBox.getSelectionModel().select(l.getSubject());
    }
}
