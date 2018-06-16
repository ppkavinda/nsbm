package controllers.admin;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import models.LecHall;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LecHallController implements Initializable {

    public TableView<LecHall> lecHallTable;
    public TextField nameField;
    private LecHall lecHall = new LecHall();
    private LecHall selectedRow;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        drawTable();
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
