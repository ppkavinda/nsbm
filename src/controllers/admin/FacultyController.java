package controllers.admin;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import models.Faculty;

public class FacultyController implements Initializable {
    @FXML private Label titleLabel;
    @FXML private TableView<Faculty> facultyTable;
    @FXML private Button mainMenuButton, addButton, editButton, removeButton;
    @FXML private TextField nameField;

    private ObservableList<Faculty> data;
    private Faculty faculty = new Faculty();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        drawTable();
    }

    @FXML
    private void toMainPanel() throws IOException {
        Scene scene = mainMenuButton.getScene();
        VBox root = FXMLLoader.load(getClass().getResource("/views/MainPanel.fxml"));
        scene.setRoot(root);
    }

    @FXML
    private void removeButton(ActionEvent actionEvent) {
        ObservableList<Faculty> selectedItems = facultyTable.getSelectionModel().getSelectedItems();
        try {
            faculty.remove(getSelectRow().getFaculty_id());
            selectedItems.forEach(data::remove);
            nameField.setText("");
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot delete Faculty. Students are assigned to the Faculty");
            alert.showAndWait();
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a Faculty", ButtonType.OK);
            alert.showAndWait();
        }
    }

    private Faculty getSelectRow() {
        return facultyTable.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void setSelectedRow () {
        nameField.setText(getSelectRow().getName());
    }

    @FXML
    protected void editFaculty() {
        try {
            faculty.update(getSelectRow().getFaculty_id(), nameField.getText());
            nameField.setText("");
            refreshTable();
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a Faculty", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    protected void addFaculty(ActionEvent event) {
        faculty.add(nameField.getText());
        nameField.setText("");
        refreshTable();
    }

    private void refreshTable() {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
