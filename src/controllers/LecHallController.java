package controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import models.LecHall;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LecHallController implements Initializable {

    @FXML private TableView<LecHall> lecHallTable;
    @FXML private TextField nameField;
    @FXML private Button mainMenuButton;

    private LecHall lecHall = new LecHall();
    private LecHall selectedRow;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        drawTable();
    }

    @FXML
    private void toMainPanel () throws IOException {
        Scene scene = mainMenuButton.getScene();
        VBox root = FXMLLoader.load(getClass().getResource("/views/MainPanel.fxml"));
        scene.setRoot(root);

    }

    @FXML
    private void removeHall () {
        lecHall.remove(selectedRow.getHall_id());
        refreshTable();
    }

    @FXML
    private void editHall () {
        lecHall.update(selectedRow.getHall_id(), nameField.getText());
        refreshTable();
        System.out.println("row " + selectedRow.getHall_id() + " updated");
    }

    @FXML
    private void getSelectedRow () {
        selectedRow = lecHallTable.getSelectionModel().getSelectedItem();
        nameField.setText(selectedRow.getName());

        System.out.println(selectedRow.getName() + " selected");
    }
    @FXML
    private void addHall () {
        lecHall.add(nameField.getText());
        refreshTable();
    }

    private void refreshTable () {
        lecHallTable.getItems().clear();
        drawTable();
    }

    private void drawTable () {
        ResultSet rs = lecHall.get();

        try {
            while (rs.next()) {
                ObservableList<LecHall> data = lecHallTable.getItems();
                data.add( new LecHall(
                    rs.getInt("hall_id"),
                    rs.getString("name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
