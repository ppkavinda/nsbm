package controllers.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;
import models.*;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SubjectController implements Initializable {
    @FXML private TableColumn codeCol;
    @FXML private TableColumn nameCol;
    @FXML private TableColumn courseCol;
    @FXML private TextField nameField;
    @FXML private ComboBox<Course> courseBox;
    @FXML private TextField codeField;
    @FXML private TextField creditsField;
    @FXML private TextField feeField;
    @FXML private ComboBox<String> semBox;
    @FXML private TableView<Subject> subTable;

    private ObservableList<Course> courseBoxData = FXCollections.observableArrayList();
    private Subject subject = new Subject();
    private Course course = new Course();
    private Subject selectedRow;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configCourseBox();
        drawTable();
//        courseCol.setCellValueFactory(
//            new PropertyValueFactory<Course,String>("course_id")
//        );
    }

    @FXML
    private void removeSubject () {
        subject.remove(selectedRow.getSubject_code());
        refreshTable();
    }

    @FXML
    private void editSubject () {
        subject.update(
            Integer.parseInt(codeField.getText()),
            Integer.parseInt(creditsField.getText()),
            courseBox.getSelectionModel().getSelectedItem().getCourse_id(),
            semBox.getSelectionModel().getSelectedIndex() + 1,
            nameField.getText(),
            Double.parseDouble(feeField.getText()),
            selectedRow.getSubject_code()
        );
        refreshTable();
        clearInputs();
    }

    @FXML
    private void getSelectedRow () {
        selectedRow = subTable.getSelectionModel().getSelectedItem();
        codeField.setText(String.valueOf(selectedRow.getCou().getCourse_id()));
        nameField.setText(selectedRow.getName());
        codeField.setText(String.valueOf(selectedRow.getSubject_code()));
        feeField.setText(String.valueOf(selectedRow.getFee()));
        courseBox.getSelectionModel().select(selectedRow.getCou());
        semBox.getSelectionModel().select(selectedRow.getSem() - 1);
        creditsField.setText(String.valueOf(selectedRow.getCredits()));
        System.out.println(selectedRow.getSem());
    }

    @FXML
    private void addSubject () {
        subject.add(Integer.parseInt(codeField.getText()),
            Integer.parseInt(creditsField.getText()),
            courseBox.getSelectionModel().getSelectedItem().getCourse_id(),
            semBox.getSelectionModel().getSelectedIndex() + 1,
            nameField.getText(),
            Double.parseDouble(feeField.getText())
        );
        refreshTable();
        clearInputs();
    }

    private void refreshTable () {
        subTable.getItems().clear();
        drawTable();
    }
    private void drawTable() {
        ResultSet rs = subject.get();
        try {
            ObservableList<Subject> data = subTable.getItems();
            while (rs.next()) {
                data.add(new Subject(
                    rs.getInt("subject_code"),
                    rs.getInt("credits"),
//                    rs.getString("course.name"),
                    new Course(rs.getInt("course.course_id"),
                        rs.getString("course.name"),
                        rs.getInt("course.duration"),
                        rs.getInt("course.credit_limit"),
                        rs.getString("course.type"),
                        new Faculty()
//                        rs.getString("course.faculty_id")
                    ),
                    rs.getInt("sem"),
                    rs.getString("name"),
                    rs.getDouble("fee")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void configCourseBox () {
        ResultSet rs = course.get();

        try {
            System.out.println("course combBox");

            while (rs.next()) {
                courseBoxData.add(new Course(
                    rs.getInt(1),   // id
                    rs.getString(2),   //  name
                    rs.getInt(3),   //  Duration
                    rs.getInt(4),   //  Credit Limit
                    rs.getString(5),    //  Type
                    new Faculty()   //  Faculty
                ));

                System.out.println("faculty: " + rs.getInt(1) + ": " + rs.getString(2));
            }
            courseBox.setItems(courseBoxData);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        StringConverter<Course> converter = new StringConverter<Course>() {

            @Override
            public String toString(Course object) {
                return object.getName();
            }

            @Override
            public Course fromString(String string) {
                return courseBox.getItems().stream().filter(ap ->
                        ap.getName().equals(string)).findFirst().orElse(null);
            }
        };

//        courseBox.setConverter(converter);

        courseBox.valueProperty().addListener((obs, oldval, newval) -> {
            if(newval != null)
                System.out.println("Selected course: " + newval.getName()
                        + ". ID: " + newval.getCourse_id());
        });

//            nameCol.setOnEditCommit(
//                (EventHandler<TableColumn.CellEditEvent<Course, String>>) t
//                    -> ((Course) t.getTableView().getItems()
//                        .get(t.getTablePosition().getRow())
//                        )
//                        .setName(t.getNewValue())
//
//            );

//            courseCol.setCellFactory(ComboBoxTableCell.forTableColumn(con, c));
    }

    private void clearInputs () {
        codeField.setText("");
        nameField.setText("");
        creditsField.setText("");
        feeField.setText("");
    }
}
