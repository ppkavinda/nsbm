package controllers.admin;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import models.Lab;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LabController implements Initializable {
    @FXML private Button addButton, removeButton, editButton;
    @FXML private TextField nameField;
    @FXML private TableView<Lab> labTable;

    private Lab lab = new Lab();
    private Lab selectedRow;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        drawTable();

    }

    @FXML
    private void removeLab () {
        lab.remove(selectedRow.getLab_id());
        refreshTable();
    }

    @FXML
    private void editLab () {
        lab.update(selectedRow.getLab_id(), nameField.getText());
        refreshTable();
        nameField.setText("");
        System.out.println("updated");
    }

    @FXML
    protected void selectRow (MouseEvent event) {
        selectedRow = labTable.getSelectionModel().getSelectedItem();
        nameField.setText(selectedRow.getName());

        System.out.println(selectedRow.getName());
    }

    @FXML
    private void addLab () {
        lab.add(nameField.getText());
        refreshTable();
        System.out.println("Inserted");
    }

    private void refreshTable () {
        labTable.getItems().clear();
        drawTable();
    }

    private void drawTable () {
        ResultSet rs = lab.get();
        try {
            while (rs.next() ) {
                ObservableList<Lab> data = labTable.getItems();

                //  add records(labs) to the table
                data.add(new Lab(
                    rs.getInt("lab_id"),
                    rs.getString("name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
