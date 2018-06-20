package controllers;

import db.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import helpers.MD5;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private TextField email;
    @FXML private PasswordField password;
    @FXML private Button loginButton;
    @FXML private Label error;

    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        error.setVisible(false);
    }

    //    when login button clicked -> validate email and attempt to login
    public void loginButtonClicked() {
        // validating email
        clearError();
        if (!validate()) {
            System.out.println(email.getText() + " " + password.getText());
            setError("Invalid Details!");
        } else {
            String errorMessage = "Unable to Login";
            try {
                if (!login()) {
                    // fail login. setting error message
                    errorMessage = "Unable to login.";
                    password.setText("");
                    System.out.println("Login failure");
                } else {
                    System.out.println("Login success ");
                    // login successful. continue with app
                    // TODO
                    test();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                errorMessage = "Unable to login: server Error";
                e.printStackTrace();
            } finally {
                System.out.println(errorMessage);
                setError(errorMessage);
            }
        }
    }

    //    when register link clicked -> load the REGISTER scene
    public void registerButtonClicked() throws IOException {

        Scene scene = loginButton.getScene();
        VBox root = FXMLLoader.load(getClass().getResource("../views/MainPanel.fxml"));
        scene.getStylesheets().add(getClass().getResource("/assets/css/login.css").toExternalForm());
        scene.setRoot(root);
        System.out.println("MainPanel.fxml opened");
    }

    //    check weather the email is empty or not
    private boolean validate() {
        if (!email.getText().equals("") && email.getText() != null &&
                !password.getText().equals("") && password.getText() != null
                ) {
            System.out.println("validated");
            return true;
        }
        return false;
    }

    private boolean login() throws SQLException {
        DBConnection conn = new DBConnection();
            String sql = "SELECT user_id FROM users WHERE email = ? AND password = ? AND role = 1";
            ResultSet rs = conn.loginUser(sql, email.getText(), MD5.getHash(password.getText()));

            // counting the resultSet
            int rowcount = 0;
            if (rs.last()) {
                rowcount = rs.getRow();
                rs.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
            }

            // return true if logged in
            return rowcount == 1;
    }

    private void setError(String msg) {
        error.setText(msg);
        error.setVisible(true);
    }

    private void clearError() {
        error.setVisible(false);
    }

    @FXML private void test () throws IOException {
        Scene scene = loginButton.getScene();
        VBox root = FXMLLoader.load(getClass().getResource("/views/MainPanel.fxml"));
        scene.setRoot(root);
    }


}
