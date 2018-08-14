package controllers.pg;

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

public class PostgraduateController implements Initializable {

    @FXML private Button mainMenuButton;
    @FXML private TableView<Postgraduate> pgTable;

    private Postgraduate pg = new Postgraduate();
    private PgTable pgt = new PgTable();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            pgt.drawTable(pgTable);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    private void toMainPanel() throws IOException {
        Scene scene = mainMenuButton.getScene();
        VBox root = FXMLLoader.load(getClass().getResource("/views/MainPanel.fxml"));
        scene.setRoot(root);
    }

    @FXML
    private Postgraduate getSelectedRow() {
        return pgTable.getSelectionModel().getSelectedItem();
    }

    //    remove selected UG
    @FXML
    private void removeButtonClicked() {
        try {
            pg.remove(getSelectedRow().getStudent_id());
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a Student", ButtonType.OK);
            alert.showAndWait();
        }
        pgt.refreshTable(pgTable);
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
        Postgraduate pg = getSelectedRow();
        if (pg == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a Student", ButtonType.OK);
            alert.showAndWait();
        } else {
            showDetailsDialog(pg);
            pgt.refreshTable(pgTable);
        }
    }

    //    trigger add DialogBox
    @FXML
    private void addButtonClicked() {
        System.out.println("Add Button Clicked");
        showDetailsDialog(null);   // null,  because it is a new student registration
        pgt.refreshTable(pgTable);
    }

    //    when add or edit button clicked - open up a dialog box with a form to register students
    private void showDetailsDialog(Postgraduate pg) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/PgDetailsDialog.fxml"));

        Stage stage = new Stage(StageStyle.DECORATED);
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
        PgDetailsDialogController controller = loader.getController();
        System.out.println(pg);
        controller.initData(pg);
        stage.showAndWait();

    }

}
