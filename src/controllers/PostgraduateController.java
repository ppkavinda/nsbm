package controllers;

import helpers.MD5;
import helpers.Validate;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import models.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class PostgraduateController implements Initializable {

    @FXML private Button selectSubjectsButton;
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button removeButton;
    @FXML private Button sem1AddButton;
    @FXML private Button sem1RemoveButton;
    @FXML private Button sem2AddButton;
    @FXML private Button sem2RemoveButton;
    @FXML private Button subRegisterButton;
    @FXML private Button subCancelButton;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private Button mainMenuButton;
    @FXML private ComboBox<Subject> sem1SubBox;
    @FXML private ComboBox<Subject> sem2SubBox;
    @FXML private ComboBox<Faculty> facultyBox;
    @FXML private ComboBox<Course> courseBox;
    @FXML private ComboBox<String> genderBox;
    @FXML private DatePicker dobField;
    @FXML private ListView<Subject> sem1SubList;
    @FXML private ListView<Subject> sem2SubList;
    @FXML private Label errorLabel;
    @FXML private Text sem2CreditsLabel;
    @FXML private Text sem1CreditsLabel;
    @FXML private TextField passwordField;
    @FXML private TextField fnameField;
    @FXML private TextField lnameField;
    @FXML private TextField emailField;
    @FXML private TextField qtypeField;
    @FXML private TextField instituteField;
    @FXML private TextField qyearField;
    @FXML private TextField addressField1;
    @FXML private TextField addressField2;
    @FXML private TextField teleField;
    @FXML private VBox pgList;
    @FXML private VBox pgDetails;
    @FXML private VBox sem2SubDetails;
    @FXML private VBox sem1SubDetails;
    @FXML private VBox selectSubView;
    @FXML private TableView<Postgraduate> pgTable;

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private Student st = new Student();
    private Postgraduate pg = new Postgraduate();
    private PgTableController pgt = new PgTableController();
    private StudentSubjectController pgs = new StudentSubjectController();
    private Faculty faculty = new Faculty();
    private Course course = new Course();
    private int addButtonClick;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pgt.drawTable(pgTable);
        faculty.configFacultyBox(facultyBox);
        course.configCourseBox(courseBox);
    }

    @FXML
    private void toMainPanel() throws IOException {
        Scene scene = mainMenuButton.getScene();
        VBox root = FXMLLoader.load(getClass().getResource("/views/MainPanel.fxml"));
        scene.setRoot(root);
    }
    //    SUBJECT SELECT VIEW ************************
    @FXML
    private void registerButtonClicked() {
//        if credits not enough: display error msg
        if (Integer.valueOf(sem1CreditsLabel.getText()) > 30 && Integer.valueOf(sem2CreditsLabel.getText()) > 30) {
            errorLabel.setText("Error: Not Enough credits for Semester");
        } else {
            ObservableList<Subject> data = sem1SubList.getItems();
            ObservableList<Subject> data2 = sem2SubList.getItems();
//            insert UG into DB
            try {
                int sid = saveButtonClicked();
//            insert subject into table
                data.forEach(sub -> st.addSubject(sub.getSubject_code(), sid));
                data2.forEach(sub -> st.addSubject(sub.getSubject_code(), sid));
            } catch (SQLException |NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input. Please fill all fields correctly!", ButtonType.OK);
                alert.showAndWait();
            }
        }
    }

    @FXML   // remove selected subject form sem1 list View
    private void sem1RemoveButtonClicked() {
        pgs.removeSubject(sem1SubList, sem1SubBox, sem1CreditsLabel);
    }

    @FXML   // remove selected subject form se2 list View
    private void sem2RemoveButtonClicked() {
        pgs.removeSubject(sem2SubList, sem2SubBox, sem2CreditsLabel);
    }

    @FXML   // add selected subject into sem1 listView
    private void sem1SubButtonClicked() {
        pgs.addSubject(sem1SubList, sem1SubBox, sem1CreditsLabel);
    }

    @FXML   // add selected subject into sem2 listView
    private void sem2SubButtonClicked() {
        pgs.addSubject(sem2SubList, sem2SubBox, sem2CreditsLabel);
    }
//    DETAILS VIEW ****************
    @FXML
    private void selectSubjectButtonClicked() {
        System.out.println("Subject Select Button Clicked");
        if (addButtonClick == 1) {
    //            add button clicked. so add a new UG
            sem1SubDetails.setDisable(false);
            sem2SubDetails.setDisable(false);
//            pgs.clearSemList(sem1SubList, sem2SubList, sem1CreditsLabel, sem2CreditsLabel, "PG");

        } else {
    //            edit button clicked. so edit selected UG
            sem1SubDetails.setDisable(true);
            sem2SubDetails.setDisable(true);
//            pgs.configSemList(getSelectedRow().getStudent_id(), sem1SubList, sem2SubList, sem1CreditsLabel, sem2CreditsLabel, "PG");
        }
        toSelectSubject();
    }
    @FXML
    private int saveButtonClicked() throws SQLException, NullPointerException {
        if (addButtonClick == 1) {
//            add button clicked. so add new PG
            System.out.println("addButtonClick");
            return addPg();

        } else {
            try {
//            edit button clicked. so edit selected PG
                editPg();
            } catch (SQLException | NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.showAndWait();
            }
            System.out.println("editButton");
        }
        return 0;
    }
    @FXML
    private Postgraduate getSelectedRow() {
        return pgTable.getSelectionModel().getSelectedItem();
    }
    @FXML
    private void cancelButtonClicked() {
        System.out.println("Cancel Button Clicked");
        toList();
    }
//    TABLE VIEW ******************
    @FXML
    private void removeButtonClicked() {
        try {
            pg.remove(getSelectedRow().getStudent_id());
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a Student", ButtonType.OK);
            alert.showAndWait();
        }
        pgt.refreshTable(pgTable);
    }
    @FXML
    private void editButtonClicked() {
        System.out.println("Edit Button Clicked");
        try {
            setInputs(getSelectedRow());
            addButtonClick = 0;
            passwordField.setDisable(true);
            saveButton.setVisible(true);
            subRegisterButton.setVisible(false);
            selectSubjectsButton.setText("Selected Subjects");
            toDetails();

        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }
    @FXML
    private void addButtonClicked() {
        System.out.println("Add Button Clicked");
        passwordField.setDisable(false);
        saveButton.setVisible(false);
        subRegisterButton.setVisible(true);
        addButtonClick = 1;
        selectSubjectsButton.setText("Select Subjects");
        toDetails();
    }


    // GO TO THE SUBJECT SELECT VIEW
    private void toSelectSubject() {
        selectSubView.toFront();
        pgDetails.setVisible(false);
        pgList.setVisible(false);
        selectSubView.setVisible(true);
        pgs.configSubBoxes(sem1SubBox, sem2SubBox, "UG");
    }

    // GO TO THE STUDENTS TABLEvIEW
    private void toList() {
        pgt.refreshTable(pgTable);
        pgDetails.setVisible(false);
        selectSubView.setVisible(false);
        pgDetails.toBack();
        pgList.setVisible(true);
        pgList.toFront();
        clearInputs();
    }

    // GOT TO THE DETAILS PANEL ADD EDIT STUDENTS
    private void toDetails() {
        pgList.setVisible(false);
        selectSubView.setVisible(false);
        pgList.toBack();
        pgDetails.setVisible(true);
        pgDetails.toFront();
    }

    private void editPg() throws SQLException, NullPointerException{
        Validate.validateTf(new TextField [] {emailField, fnameField, lnameField, addressField1, addressField2, teleField, qtypeField, instituteField, qyearField});
        pg.update(
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
                qtypeField.getText(),
                instituteField.getText(),
                Integer.valueOf(qyearField.getText()),
                getSelectedRow().getStudent_id()
        );

        toList();
    }

    private int addPg() throws SQLException, NullPointerException{
        Validate.validateTf(new TextField [] {emailField, fnameField, lnameField, addressField1, addressField2, teleField, qtypeField, instituteField, qyearField});
        int stid = pg.add(
                emailField.getText(),
                MD5.getHash(passwordField.getText()),
                5,
                fnameField.getText(),
                lnameField.getText(),
                addressField1.getText(),
                addressField2.getText(),
                teleField.getText(),
                java.sql.Date.valueOf(dobField.getValue()),
                genderBox.getSelectionModel().getSelectedItem(),
                facultyBox.getSelectionModel().getSelectedItem().getFaculty_id(),
                courseBox.getSelectionModel().getSelectedItem().getCourse_id(),
                qtypeField.getText(),
                instituteField.getText(),
                Integer.valueOf(qyearField.getText())
        );
        toList();
        return stid;

    }

    private void clearInputs() {
        setInputs(new Postgraduate());
    }

    private void setInputs(Postgraduate pg) {
        if (pg == null) {
            throw new NullPointerException("Please select a student");
        } else {
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
            teleField.setText(String.valueOf(pg.getTpno()));
            qyearField.setText(String.valueOf(pg.getYear_of_completion()));
        }
    }
}
