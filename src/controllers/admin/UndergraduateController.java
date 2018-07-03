package controllers.admin;

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

public class UndergraduateController implements Initializable {
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

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private Undergraduate ug = new Undergraduate();
    private StudentSubjectController ugs = new StudentSubjectController();
    private UgTableController ugt = new UgTableController();
    private Student st = new Student();
    private Faculty faculty = new Faculty();
    private Course course = new Course();
    private int addButtonClick; //  to determine weather to ADD or EDIT Ug (when add button clicked :1 else :0)

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            ugt.drawTable(ugTable);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
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
                data2.forEach(sub -> st.addSubject(sub.getSubject_code(), sid));
                data.forEach(sub -> st.addSubject(sub.getSubject_code(), sid));

            } catch (SQLException |NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input. Please fill all fields correctly!", ButtonType.OK);
                alert.showAndWait();
            }

            data.forEach(sub -> System.out.println(sub.getName()));
        }
    }

    @FXML   // remove selected subject form sem1 list View
    private void sem1RemoveButtonClicked() {
        ugs.removeSubject(sem1SubList, sem1SubBox, sem1CreditsLabel);
    }

    @FXML   // remove selected subject form se2 list View
    private void sem2RemoveButtonClicked() {
        ugs.removeSubject(sem2SubList, sem2SubBox, sem2CreditsLabel);
    }

    @FXML   // add selected subject into sem1 listView
    private void sem1SubButtonClicked() {
        ugs.addSubject(sem1SubList, sem1SubBox, sem1CreditsLabel);
    }

    @FXML   // add selected subject into sem2 listView
    private void sem2SubButtonClicked() {
        ugs.addSubject(sem2SubList, sem2SubBox, sem2CreditsLabel);
    }

    //    DETAILS VIEW ********************************
    @FXML
    private void selectSubjectButtonClicked() {
        System.out.println("Subject Select Button Clicked");
        if (addButtonClick == 1) {
//            add button clicked. so add a new UG
            sem1SubDetails.setDisable(false);
            sem2SubDetails.setDisable(false);
            ugs.clearSemList(sem1SubList, sem2SubList, sem1CreditsLabel, sem2CreditsLabel, "UG");

        } else {
//            edit button clicked. so edit selected UG
            sem1SubDetails.setDisable(true);
            sem2SubDetails.setDisable(true);
            ugs.configSemList(getSelectedRow().getStudent_id(), sem1SubList, sem2SubList, sem1CreditsLabel, sem2CreditsLabel, "UG");
        }
        toSelectSubject();
    }

    @FXML
    private int saveButtonClicked() throws SQLException, NullPointerException {
        if (addButtonClick == 1) {
//            add button clicked. so add new UG
            System.out.println("addButtonClick");
            return addUg();

        } else {
            try {
//            edit button clicked. so edit selected UG
                editUg();
            } catch (SQLException | NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.showAndWait();
            }
            System.out.println("editButton");
        }
        return 0;
    }

    @FXML
    private Undergraduate getSelectedRow() {
        return ugTable.getSelectionModel().getSelectedItem();
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
            ug.remove(getSelectedRow().getStudent_id());
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a Student", ButtonType.OK);
            alert.showAndWait();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
        ugt.refreshTable(ugTable);
    }

    @FXML
    private void editButtonClicked() {
        System.out.println("Edit Button Clicked");
        try {
            setInputs(getSelectedRow());
            addButtonClick = 0;
            saveButton.setVisible(true);
            passwordField.setDisable(true);
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
        subRegisterButton.setVisible(true);
        saveButton.setVisible(false);
        addButtonClick = 1;
        selectSubjectsButton.setText("Select Subjects");
        toDetails();
    }

    // GO TO THE SUBJECT SELECT VIEW
    private void toSelectSubject() {
        selectSubView.toFront();
        ugList.setVisible(false);
        ugDetails.setVisible(false);
        ugs.configSubBoxes(sem1SubBox, sem2SubBox, "UG");
        selectSubView.setVisible(true);
    }

    // GO TO THE STUDENTS TABLEvIEW
    private void toList() {
        ugDetails.setVisible(false);
        ugt.refreshTable(ugTable);
        selectSubView.setVisible(false);
        ugDetails.toBack();
        ugList.setVisible(true);
        ugList.toFront();
        clearInputs();
    }

    // GOT TO THE DETAILS PANEL ADD EDIT STUDENTS
    private void toDetails() {
        selectSubView.setVisible(false);
        ugList.setVisible(false);
        ugDetails.setVisible(true);
        ugDetails.toFront();
    }

    private void editUg() throws SQLException, NullPointerException {
        Validate.validateTf(new TextField [] {emailField, fnameField, lnameField, addressField1, addressField2, teleField, sub1Field, sub2Field, sub3Field});
        ug.update(
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
                getSelectedRow().getStudent_id()
        );

        toList();
    }

    private int addUg() throws SQLException, NullPointerException {
        Validate.validateTf(new TextField [] {emailField, fnameField, lnameField, addressField1, addressField2, teleField, sub1Field, sub2Field, sub3Field});
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
        toList();
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
