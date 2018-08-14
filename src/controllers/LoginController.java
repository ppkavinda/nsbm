package controllers;

import db.DBConnection;
import db.DbSingleton;
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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label error;

    private Stage stage;
    private DbSingleton conn = DbSingleton.getInstance();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        error.setVisible(false);
    }

    //    when login button clicked -> validate emailField and attempt to login
    public void loginButtonClicked() {
        // validating emailField
        if (!validate()) {
            System.out.println(emailField.getText() + " " + passwordField.getText());
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Data", ButtonType.OK);
            alert.showAndWait();
        } else {
            try {
                if (!login()) {
                    // fail login. setting error message
                    passwordField.setText("");
                    System.out.println("Login failure");
                } else {
                    System.out.println("Login success ");
                    // login successful. continue with app
                    // TODO
                    toMainPanel();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.showAndWait();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
                alert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    //    check weather the emailField is empty or not
    private boolean validate() {
        if (!emailField.getText().equals("") && emailField.getText() != null &&
                !passwordField.getText().equals("") && passwordField.getText() != null
                ) {
            System.out.println("validated");
            return true;
        }
        return false;
    }

    private boolean login() throws SQLException {
            ResultSet rs = conn.loginUser(emailField.getText(), MD5.getHash(passwordField.getText()));

            // counting the resultSet
            int rowCount = 0;
            if (rs.last()) {
                rowCount = rs.getRow();
                rs.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
            }

            // return true if logged in
            return rowCount == 1;
    }

    @FXML private void toMainPanel () throws IOException {
        Scene scene = loginButton.getScene();
        VBox root = FXMLLoader.load(getClass().getResource("/views/MainPanel.fxml"));
        scene.setRoot(root);
    }


}
