package controllers.admin;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import models.Faculty;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class FacultyController implements Initializable {
    @FXML private Label titleLabel;
    @FXML private TableView<Faculty> facultyTable;
    @FXML private Button mainMenuButton, addButton, editButton, removeButton;
    @FXML private TextField nameField;

    private ObservableList<Faculty> data;
    Faculty selectedRow;

    private Faculty faculty = new Faculty();

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
    private void removeButton(ActionEvent actionEvent) {
        ObservableList<Faculty> selectedItems = facultyTable.getSelectionModel().getSelectedItems();
        selectedItems.forEach(data::remove);
        faculty.remove(selectedRow.getFaculty_id());
    }
    @FXML
    protected void selectRow (MouseEvent event) {
        Faculty row = facultyTable.getSelectionModel().getSelectedItem();
        nameField.setText(row.getName());
        selectedRow = row;
    }
    @FXML
    protected void editFaculty (ActionEvent e) {
        faculty.update(selectedRow.getFaculty_id(), nameField.getText());
        nameField.setText("");
        refreshTable();
    }

    @FXML
    protected void addFaculty(ActionEvent event) {
        faculty.add(nameField.getText());
        nameField.setText("");
        refreshTable();
    }

    private void refreshTable () {
        facultyTable.getItems().clear();
        drawTable();
    }

    private void drawTable() {
        ResultSet rs = faculty.get();

        try {
            while (rs.next()) {
                data = facultyTable.getItems();
                data.add(new Faculty(
                    rs.getInt(1),
                    rs.getString(2)
                ));

                System.out.println("Getting Records - table :faculty" + rs.getString(2));
            }
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }
}
