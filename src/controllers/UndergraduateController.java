package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
    private UgTable ugt = new UgTable();
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

    }

    @FXML
    private void toMainPanel() throws IOException {
        Scene scene = mainMenuButton.getScene();
        VBox root = FXMLLoader.load(getClass().getResource("/views/MainPanel.fxml"));
        scene.setRoot(root);
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

    @FXML
    private Undergraduate getSelectedRow() {
        return ugTable.getSelectionModel().getSelectedItem();
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
        Undergraduate ug = getSelectedRow();
        if (ug == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a Student", ButtonType.OK);
            alert.showAndWait();
        } else {
            showDetailsDialog(ug);
        }
    }

    @FXML
    private void addButtonClicked() {
        System.out.println("Add Button Clicked");
        showDetailsDialog(null);   // null,  because it is a new student registration
    }

//    when add button clicked - open up a dialog box with a form to register students
    private void showDetailsDialog(Undergraduate ug) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UgDetailsForm.fxml"));

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);

        try {
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("/assets/css/login.css").toExternalForm());
            stage.setScene(scene);

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }

//      calling the setup method of the controller of dialog box FXML
        UgDetailsFormController controller = loader.getController();
        System.out.println(ug);
        controller.initData(ug);
        stage.showAndWait();

    }

    // GO TO THE SUBJECT SELECT VIEW
    private void toSelectSubject() {
        selectSubView.toFront();
        ugList.setVisible(false);
        ugDetails.setVisible(false);
        ugs.configSubBoxes(sem1SubBox, sem2SubBox, "UG");
        selectSubView.setVisible(true);
    }

    // GOT TO THE DETAILS PANEL ADD EDIT STUDENTS
    private void toDetails() {
        selectSubView.setVisible(false);
        ugList.setVisible(false);
        ugDetails.setVisible(true);
        ugDetails.toFront();
    }
}
