package controllers.pg;

import controllers.pg.PgSubDetails;
import helpers.MD5;
import helpers.Validate;
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
import models.Postgraduate;
import models.Subject;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class PgDetailsDialogController implements Initializable {
    @FXML
    private Button selectSubjectsButton;
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
    @FXML private Label sem2CreditsLabel;
    @FXML private Label sem1CreditsLabel;
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

    private PgSubDetails pgSubDetails = new PgSubDetails();
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private boolean addButtonClicked;
    private Postgraduate pg;
    private Faculty faculty = new Faculty();
    private Course course = new Course();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sem1CreditsLabel.textProperty().bind(pgSubDetails.sem1TotalCreditsProperty().asString());
        sem2CreditsLabel.textProperty().bind(pgSubDetails.sem2TotalCreditsProperty().asString());
        faculty.configFacultyBox(facultyBox);
        course.configCourseBox(courseBox);
    }

    //    get the selected student data from the table
    void initData (Postgraduate pg) {
        if (pg == null) {
            this.pg = new Postgraduate();
            addButtonClicked = true;
        } else {
            this.pg = pg;
            addButtonClicked = false;
            setInputs(pg);
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
            pgSubDetails.subAdd(sub, this.pg);
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
                pgSubDetails.subRemove(sub, this.pg);
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
            this.pg.setStudent_id(saveStudent());

            // insert SELECTED subjects into the subject ListViews
            pgSubDetails.configSubList(sem1SubList, sem2SubList, pg.getSelectedSubs(pg.getStudent_id()));

            // insert Subjects into SELECTABLE subjects into the ComboBoxes
            pgSubDetails.configSubBox(sem1SubBox, sem2SubBox, pg.getSelectableSubjects(pg.getStudent_id()));

            // goto the subject-select-section
            pgDetails.setVisible(false);
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
            int sid =   addPg();
            addButtonClicked = false;

            System.out.println("Undergraduate added!! finally :)");
            return sid;

        } else {
//            edit button clicked. so edit selected UG
            System.out.println("editButton");
            int sid = editPg();
            System.out.println("Undergraduate saved!! finally :)");
            return sid;
        }
    }

    //    update Existing Undergraduate
    private int editPg() throws SQLException, NullPointerException{
        Validate.validateTf(new TextField [] {emailField, fnameField, lnameField, addressField1, addressField2, teleField, qtypeField, instituteField, qyearField});
        return pg.update(
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
                this.pg.getStudent_id()
        );
    }

    //    add new Postgraduate to db
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
