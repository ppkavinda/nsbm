package controllers.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import models.*;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PracticleController implements Initializable{
    @FXML private TableView<Practicle> practicleTable;
    @FXML private TextField startField;
    @FXML private TextField endField;
    @FXML private ComboBox<Lab> locationBox;
    @FXML private ComboBox<Subject> subjectBox;

    private Practicle practicle = new Practicle();
    private Practicle selectedRow;
    private Lab lab = new Lab();
    private Subject subject = new Subject();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configLocationBox();
        configSubjectBox();
        drawTable();
    }

    @FXML
    private void removeLecture () {
        practicle.remove(selectedRow.getPracticle_id());
        setInputs(new Practicle());
        refreshTable();
    }

    @FXML
    private void editLecture () {
        practicle.update(
                startField.getText(),
                endField.getText(),
                locationBox.getSelectionModel().getSelectedItem().getLab_id(),
                subjectBox.getSelectionModel().getSelectedItem().getSubject_code(),
                selectedRow.getPracticle_id()
        );
        setInputs(new Practicle());
        refreshTable();
    }

    @FXML
    private void getSelectedRow () {
        selectedRow = practicleTable.getSelectionModel().getSelectedItem();
        setInputs(selectedRow);
    }


    @FXML
    private void addLecture () {
        practicle.add(
                startField.getText(),
                endField.getText(),
                locationBox.getSelectionModel().getSelectedItem().getLab_id(),
                subjectBox.getSelectionModel().getSelectedItem().getSubject_code()
        );

        refreshTable();
        setInputs(new Practicle());
    }


    private void refreshTable () {
        practicleTable.getItems().clear();
        drawTable();
    }

    private void drawTable () {
        ResultSet rs = practicle.get();

        try {
            ObservableList<Practicle> data = practicleTable.getItems();

            while (rs.next()) {
                data.add(new Practicle(
                        rs.getInt("lab_id"),
                        new Lab(
                                rs.getInt("lab.lab_id"),
                                rs.getString("lab.name")
                        ),
                        new Subject(
                                rs.getInt("subject.subject_code"),
                                rs.getInt("subject.credits"),
                                new Course(),
                                rs.getInt("subject.sem"),
                                rs.getString("subject.name"),
                                rs.getDouble("subject.fee")
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
        ResultSet rs = lab.get();

        try {
            ObservableList<Lab> locationBoxData = FXCollections.observableArrayList();
            while (rs.next()) {
                locationBoxData.add(new Lab(
                        rs.getInt("lab_id"),
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

    private void setInputs (Practicle p) {
        startField.setText(p.getStart_time());
        endField.setText(p.getEnd_time());
        locationBox.getSelectionModel().select(p.getLab());
        subjectBox.getSelectionModel().select(p.getSubject());
    }

}
