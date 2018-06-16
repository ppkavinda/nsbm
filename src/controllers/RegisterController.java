package controllers;

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

public class RegisterController implements Initializable {
    public DatePicker dob;
    public Label error;
    public TextField firstName;
    public TextField lastName;
    public TextField email;
    public PasswordField password, password_conf;
    public Button registerButton;
    public TextField username;
    public Hyperlink loginLink;
    public Label titleLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void registerButtonClicked() {
        System.out.println();

    }

    public void loginLinkClicked() throws IOException {
        Scene scene = registerButton.getScene();
        VBox root = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
        scene.getStylesheets().add(getClass().getResource("/assets/css/login.css").toExternalForm());
        scene.setRoot(root);
        System.out.println("Login.fxml opened");
    }

    private boolean validate() {
        return false;
    }
}
