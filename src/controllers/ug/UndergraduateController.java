package controllers.ug;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UndergraduateController implements Initializable {
    @FXML private Button mainMenuButton;
    @FXML private TableView<Undergraduate> ugTable;

    private Undergraduate ug = new Undergraduate();
    private UgTable ugt = new UgTable();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            ugt.drawTable(ugTable);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

//    goto the main panel
    @FXML
    private void toMainPanel() throws IOException {
        Scene scene = mainMenuButton.getScene();
        VBox root = FXMLLoader.load(getClass().getResource("/views/MainPanel.fxml"));
        scene.setRoot(root);
    }

//    get the current selected row
    @FXML
    private Undergraduate getSelectedRow() {
        return ugTable.getSelectionModel().getSelectedItem();
    }

//    remove selected UG
    @FXML
    private void removeButtonClicked() {
        try {
            ug.remove(getSelectedRow().getStudent_id());
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a Student", ButtonType.OK);
            alert.showAndWait();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
        ugt.refreshTable(ugTable);
    }

//    trigger edit DialogBox if double click detected on a table row
    @FXML
    private void editSelectedRow(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2 && getSelectedRow() != null) {
            editButtonClicked();
        }
    }

//    trigger edit DialogBox
    @FXML
    private void editButtonClicked() {
        System.out.println("Edit Button Clicked");
        Undergraduate ug = getSelectedRow();
        if (ug == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a Student", ButtonType.OK);
            alert.showAndWait();
        } else {
            showDetailsDialog(ug);
            ugt.refreshTable(ugTable);
        }
    }

//    trigger add DialogBox
    @FXML
    private void addButtonClicked() {
        System.out.println("Add Button Clicked");
        showDetailsDialog(null);   // null,  because it is a new student registration
        ugt.refreshTable(ugTable);
    }

//    when add or edit button clicked - open up a dialog box with a form to register students
    private void showDetailsDialog(Undergraduate ug) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UgDetailsDialog.fxml"));

        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setMaximized(true);
        stage.initModality(Modality.APPLICATION_MODAL);

        try {
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("/assets/css/login.css").toExternalForm());
            stage.setScene(scene);

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }

//      calling the setup method of the controller of dialog box FXML
        UgDetailsDialogController controller = loader.getController();
        System.out.println(ug);
        controller.initData(ug);
        stage.showAndWait();

    }
}
