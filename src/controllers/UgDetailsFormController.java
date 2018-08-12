package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class UgDetailsFormController implements Initializable {

    @FXML private Label labl;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    void initData (String name) {
        labl.setText(name);
    }
}
