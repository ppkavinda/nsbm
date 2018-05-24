package sample;

import db.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.md5.MD5;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button loginButton;
    @FXML
    private Label error;

    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        error.setVisible(false);
    }

    //    when login button clicked -> validate username and attempt to login
    public void loginButtonClicked() {
        // validating username
        clearError();
        if (!validate()) {
            System.out.println(username.getText() + " " + password.getText());
            setError("Invalid Details!");
        } else {
            if (!login()) {
                // fail login. setting error message
                setError("Unable to login.");
                password.setText("");
                System.out.println("Login failure");
            } else {
                System.out.println("Login success ");
                // login successful. continue with app
                // TODO
            }
        }
    }

    //    when register link clicked -> load the REGISTER scene
    public void registerButtonClicked() throws IOException {

        Scene scene = loginButton.getScene();
        VBox root = FXMLLoader.load(getClass().getResource("Register.fxml"));
        scene.getStylesheets().add(getClass().getResource("login.css").toExternalForm());
        scene.setRoot(root);
        System.out.println("Register.fxml opened");
    }

    //    check weather the username is empty or not
    private boolean validate() {
        if (!username.getText().equals("") && username.getText() != null &&
                !password.getText().equals("") && password.getText() != null
                ) {
            System.out.println("validated");
            return true;
        }
        return false;
    }

    private boolean login() {
        DBConnection conn = new DBConnection();
        try {
            String sql = "SELECT id FROM users WHERE username = ? and password = ?;";
            ResultSet rs = conn.loginUser(sql, username.getText(), MD5.getHash(password.getText()));

            // counting the resultSet
            int rowcount = 0;
            if (rs.last()) {
                rowcount = rs.getRow();
                rs.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
            }

            // return true if logged in
            return rowcount == 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void setError(String msg) {
        error.setText(msg);
        error.setVisible(true);
    }

    private void clearError() {
        error.setVisible(false);
    }
}
