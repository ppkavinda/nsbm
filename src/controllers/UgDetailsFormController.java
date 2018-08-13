package controllers;

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
    @FXML private Text sem1CreditsLabel;
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
    private Faculty faculty = new Faculty();
    private Course course = new Course();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        faculty.configFacultyBox(facultyBox);
        course.configCourseBox(courseBox);
    }

    void initData (Undergraduate ug) {
        if (ug == null) {
            this.ug = new Undergraduate();
            addButtonClicked = true;
        } else {
            this.ug = ug;
            addButtonClicked = false;
            setInputs(ug);
            System.out.println(ug.getStudent_id());
        }
    }

    @FXML
    private void cancelButtonClicked(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void selectSubjectButtonClicked(ActionEvent actionEvent) {
        // Register new Student or Save existing student
        try {
            saveStudent();
        } catch (SQLException | NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            e.printStackTrace();
            alert.showAndWait();
        }

        // goto the subject-select-section

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
                int sid = saveStudent();
//            insert subject into table
//                data2.forEach(sub -> st.addSubject(sub.getSubject_code(), sid));
//                data.forEach(sub -> st.addSubject(sub.getSubject_code(), sid));

            } catch (NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input. Please fill all fields correctly!", ButtonType.OK);
                alert.showAndWait();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            data.forEach(sub -> System.out.println(sub.getName()));
        }
    }

    private int saveStudent() throws SQLException {

        if (addButtonClicked) {
//            add button clicked. so add new UG
            System.out.println("addButtonClick");
            int sid =   addUg();

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
