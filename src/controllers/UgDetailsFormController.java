package controllers;

import helpers.MD5;
import helpers.Validate;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Course;
import models.Faculty;
import models.Subject;
import models.Undergraduate;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class UgDetailsFormController implements Initializable {
    @FXML private Button cancelButton;
    @FXML private Button mainMenuButton;
    @FXML private Button saveButton;
    @FXML private Button selectSubjectsButton;
    @FXML private Button subRegisterButton;
    @FXML private ComboBox<Subject> sem2SubBox;
    @FXML private ComboBox<Subject> sem1SubBox;
    @FXML private ComboBox<Faculty> facultyBox;
    @FXML private ComboBox<Course> courseBox;
    @FXML private ComboBox<String> genderBox;
    @FXML private DatePicker dobField;
    @FXML private Label errorLabel;
    @FXML private ListView<Subject> sem1SubList;
    @FXML private ListView<Subject> sem2SubList;
    @FXML private TableView<Undergraduate> ugTable;
    @FXML private Label sem1CreditsLabel;
    @FXML private Text sem2CreditsLabel;
    @FXML private TextField sub1Field;
    @FXML private TextField sub3Field;
    @FXML private TextField sub2Field;
    @FXML private TextField passwordField;
    @FXML private TextField fnameField;
    @FXML private TextField lnameField;
    @FXML private TextField emailField;
    @FXML private TextField rankField;
    @FXML private TextField zscoreField;
    @FXML private TextField addressField1;
    @FXML private TextField addressField2;
    @FXML private TextField teleField;
    @FXML private VBox ugList;
    @FXML private VBox ugDetails;
    @FXML private VBox selectSubView;
    @FXML private VBox sem2SubDetails;
    @FXML private VBox sem1SubDetails;
    @FXML private Label labl;

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private boolean addButtonClicked;
    private Undergraduate ug;
    private UgSubDetails ugSubDetails = new UgSubDetails();
    private Faculty faculty = new Faculty();
    private Course course = new Course();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sem1CreditsLabel.textProperty().bind(ugSubDetails.sem1TotalCreditsProperty().asString());
        sem2CreditsLabel.textProperty().bind(ugSubDetails.sem2TotalCreditsProperty().asString());
        faculty.configFacultyBox(facultyBox);
        course.configCourseBox(courseBox);
    }

//    get the selected student data from the table
    void initData (Undergraduate ug) {
        if (ug == null) {
            this.ug = new Undergraduate();
            addButtonClicked = true;
        } else {
            this.ug = ug;
            addButtonClicked = false;
            setInputs(ug);
            passwordField.setDisable(true);
        }
    }

//    add a subject into semester 1 subject LISTVIEW and  remove it from semester 1 subject COMBO BOX
    public void sem1SubButtonClicked(ActionEvent actionEvent) {
        Subject sub = sem1SubBox.getSelectionModel().getSelectedItem();
        addSubject(sub, sem1SubBox.getItems(), sem1SubList.getItems());
    }

//    remove subject from semester 1 subject LISTVIEW and add it to semester 1 subject COMBO BOX
    public void sem1RemoveButtonClicked(ActionEvent actionEvent) {
        Subject sub = sem1SubList.getSelectionModel().getSelectedItem();
        removeSubject(sub, sem1SubList.getItems(), sem1SubBox.getItems());
    }

//    add a subject into semester 2 subject LISTVIEW and remove it from semester 2 subject COMBO BOX
    public void sem2SubButtonClicked(ActionEvent actionEvent) {
        Subject sub = sem2SubBox.getSelectionModel().getSelectedItem();
        addSubject(sub, sem2SubBox.getItems(), sem2SubList.getItems());
    }

//    remove a subject from semester 2 subject LISTVIEW. and add it to semester 2 subject COMBO BOX
    public void sem2RemoveButtonClicked(ActionEvent actionEvent) {
        Subject sub = sem2SubList.getSelectionModel().getSelectedItem();
        removeSubject(sub, sem2SubList.getItems(), sem2SubBox.getItems());
    }

//    add a subject selected by a student. (add a row to student_subject table)
    private void addSubject (Subject sub, ObservableList<Subject> items, ObservableList<Subject> items2) {
        if (sub != null) {
            ugSubDetails.subAdd(sub, this.ug);
            items.remove(sub);
            items2.add(sub);
        }
    }

//    remove a subject selected by a student. (delete row from Student_subject table)
    private void removeSubject(Subject sub, ObservableList<Subject> items, ObservableList<Subject> items2) {
        if (sub != null) {
            if (sub.getCompulsory() == 1) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot remove Compulsory subjects.", ButtonType.OK);
                alert.showAndWait();
            } else {
                ugSubDetails.subRemove(sub, this.ug);
                items.remove(sub);
                items2.add(sub);
            }
        }
    }

    //    cancel all and close Dialog box
    @FXML
    private void cancelButtonClicked(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

//  save student to DB and goto the subject-select-section
    @FXML
    private void selectSubjectButtonClicked(ActionEvent actionEvent) {
        // Register new Student or Save existing student
        try {
            this.ug.setStudent_id(saveStudent());

            // insert SELECTED subjects into the subject ListViews
            ugSubDetails.configSubList(sem1SubList, sem2SubList, ug.getSelectedSubs(ug.getStudent_id()));

            // insert Subjects into SELECTABLE subjects into the ComboBoxes
            ugSubDetails.configSubBox(sem1SubBox, sem2SubBox, ug.getSelectableSubjects(ug.getStudent_id()));

            // goto the subject-select-section
            ugDetails.setVisible(false);
            selectSubView.setVisible(true);
            selectSubView.toFront();

        } catch (SQLException | NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            e.printStackTrace();
            alert.showAndWait();
        }
    }

//  trigger: add or edit methods according to the addButtonClicked states
    private int saveStudent() throws SQLException {

        if (addButtonClicked) {
//            add button clicked. so add new UG
            System.out.println("addButtonClick");
            int sid =   addUg();
            addButtonClicked = false;

            System.out.println("Undergraduate added!! finally :)");
            return sid;

        } else {
//            edit button clicked. so edit selected UG
            System.out.println("editButton");
            int sid = editUg();
            System.out.println("Undergraduate saved!! finally :)");
            return sid;
        }
    }

//    update Existing Undergraduate
    private int editUg() throws SQLException, NullPointerException {
        Validate.validateTf(new TextField [] {emailField, fnameField, lnameField, addressField1, addressField2, teleField, sub1Field, sub2Field, sub3Field});
        return ug.update(
                emailField.getText(),
                fnameField.getText(),
                lnameField.getText(),
                addressField1.getText(),
                addressField2.getText(),
                teleField.getText(),
                java.sql.Date.valueOf(dobField.getValue()),
                genderBox.getSelectionModel().getSelectedItem(),
                facultyBox.getSelectionModel().getSelectedItem().getFaculty_id(),
                courseBox.getSelectionModel().getSelectedItem().getCourse_id(),
                rankField.getText(),
                Double.valueOf(zscoreField.getText()),
                sub1Field.getText(),
                sub2Field.getText(),
                sub3Field.getText(),
                ug.getStudent_id()
        );

    }

//    add new Undergraduate to db
    private int addUg() throws SQLException {
        Validate.validateTf(new TextField[] {emailField, fnameField, lnameField, addressField1, addressField2, teleField, sub1Field, sub2Field, sub3Field});
        System.out.println("validation Done 1");
        int stid = ug.add(
                emailField.getText(),
                MD5.getHash(passwordField.getText()),
                4,
                fnameField.getText(),
                lnameField.getText(),
                addressField1.getText(),
                addressField2.getText(),
                teleField.getText(),
                java.sql.Date.valueOf(dobField.getValue()),
                genderBox.getSelectionModel().getSelectedItem(),
                facultyBox.getSelectionModel().getSelectedItem().getFaculty_id(),
                courseBox.getSelectionModel().getSelectedItem().getCourse_id(),
                rankField.getText(),
                Double.valueOf(zscoreField.getText()),
                sub1Field.getText(),
                sub2Field.getText(),
                sub3Field.getText()
        );
        System.out.println("UG Added 1");
        return stid;
    }

    private void clearInputs() {
        setInputs(new Undergraduate());
    }

    private void setInputs(Undergraduate ug) throws NullPointerException {
        if (ug == null) {
            throw new NullPointerException("Please select a student");
        } else {
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

}
