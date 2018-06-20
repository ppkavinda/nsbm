package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class MainPanelController implements Initializable {

    @FXML private Button facultyButton;
    @FXML private Button courseButton;
    @FXML private Button labButton;
    @FXML private Button lechallButton;
    @FXML private Button subjectButton;
    @FXML private Button testButton;
    @FXML private Button instructorButton;
    @FXML private Button lecturerButton;
    @FXML private Button postgraduateButton;
    @FXML private Button undergraduateButton;
    @FXML private Button lectureButton;
    @FXML private Button practicleButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void registerButtonClicked() {
        System.out.println();
    }

    public void loginLinkClicked() throws IOException {
//        Scene scene = registerButton.getScene();
//        VBox root = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
//        scene.getStylesheets().add(getClass().getResource("/assets/css/login.css").toExternalForm());
//        scene.setRoot(root);
        System.out.println("Login.fxml opened");
    }

    private boolean validate() {
        return false;
    }

    public void courseButtonClicked () throws IOException {
        Scene scene = courseButton.getScene();
        VBox root = FXMLLoader.load(getClass().getResource("/views/admin/Course.fxml"));
        scene.setRoot(root);
    }
    public void facultyButtonClicked () throws IOException {
        Scene scene = facultyButton.getScene();
        VBox root = FXMLLoader.load(getClass().getResource("/views/Admin/Faculty.fxml"));
        scene.setRoot(root);
    }
    public void instructorButtonClicked () throws IOException {
        Scene scene = instructorButton.getScene();
        VBox root = FXMLLoader.load(getClass().getResource("/views/Admin/Instructor.fxml"));
        scene.setRoot(root);
    }
    public void labButtonClicked () throws IOException {
        Scene scene = labButton.getScene();

        VBox root = FXMLLoader.load(getClass().getResource("/views/Admin/Lab.fxml"));
        scene.setRoot(root);
    }
    public void lecHallButtonClicked () throws IOException {
        Scene scene = lechallButton.getScene();
        VBox root = FXMLLoader.load(getClass().getResource("/views/Admin/LecHall.fxml"));
        scene.setRoot(root);
    }
    public void lectureButtonClicked () throws IOException {
        Scene scene = lectureButton.getScene();
        VBox root = FXMLLoader.load(getClass().getResource("/views/Admin/Lecture.fxml"));
        scene.setRoot(root);
    }
    public void lecturerButtonClicked () throws IOException {
        Scene scene = lecturerButton.getScene();
        VBox root = FXMLLoader.load(getClass().getResource("/views/Admin/Lecturer.fxml"));
        scene.setRoot(root);
    }
    public void postgraduateButtonClicked () throws IOException {
        Scene scene = postgraduateButton.getScene();
        VBox root = FXMLLoader.load(getClass().getResource("/views/Admin/Postgraduate.fxml"));
        scene.setRoot(root);
    }
    public void practicleButtonClicked () throws IOException {
        Scene scene = practicleButton.getScene();
        VBox root = FXMLLoader.load(getClass().getResource("/views/Admin/Practicle.fxml"));
        scene.setRoot(root);
    }
    public void undergraduateButtonClicked () throws IOException {
        Scene scene = undergraduateButton.getScene();
        VBox root = FXMLLoader.load(getClass().getResource("/views/Admin/Undergraduate.fxml"));
        scene.setRoot(root);
    }
    public void subjectButtonClicked () throws IOException {
        Scene scene = subjectButton.getScene();
        VBox root = FXMLLoader.load(getClass().getResource("/views/Admin/Subject.fxml"));
        scene.setRoot(root);
    }
    public void testButtonClicked () throws IOException {
        Scene scene = testButton.getScene();
        VBox root = FXMLLoader.load(getClass().getResource("/views/Admin/Test.fxml"));
        scene.setRoot(root);
    }
}
