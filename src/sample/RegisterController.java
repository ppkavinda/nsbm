package sample;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {
    public TextField username;
    public TextField email;
    public DatePicker dob;
    public Label error;
    public TextField firstName;
    public Button registerButton;
    public TextField lastName;
    public Label registerLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void registerButtonClicked() {
        System.out.println(dob.getValue());

    }

    public void loginLinkClicked() throws IOException {
        Scene scene = registerButton.getScene();
        VBox root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        scene.getStylesheets().add(getClass().getResource("login.css").toExternalForm());
        scene.setRoot(root);
        System.out.println("Login.fxml opened");
    }
}
