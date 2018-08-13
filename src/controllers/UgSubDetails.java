package controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import models.Course;
import models.Faculty;
import models.Subject;
import models.Undergraduate;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UgSubDetails {

    public void sem1SubButtonClicked(ActionEvent actionEvent) {
    }

    public void sem1RemoveButtonClicked(ActionEvent actionEvent) {
    }

    public void sem2SubButtonClicked(ActionEvent actionEvent) {
    }

    public void sem2RemoveButtonClicked(ActionEvent actionEvent) {
    }

    //    SUBJECT SELECT VIEW ************************
//    @FXML
//    private void registerButtonClicked() {
////        if credits not enough: display error msg
//        if (Integer.valueOf(sem1CreditsLabel.getText()) > 30 && Integer.valueOf(sem2CreditsLabel.getText()) > 30) {
//            errorLabel.setText("Error: Not Enough credits for Semester");
//        } else {
//            ObservableList<Subject> data = sem1SubList.getItems();
//            ObservableList<Subject> data2 = sem2SubList.getItems();
////            insert UG into DB
//            try {
//                int sid = saveStudent();
////            insert subject into table
////                data2.forEach(sub -> st.addSubject(sub.getSubject_code(), sid));
////                data.forEach(sub -> st.addSubject(sub.getSubject_code(), sid));
//
//            } catch (NullPointerException e) {
//                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Input. Please fill all fields correctly!", ButtonType.OK);
//                alert.showAndWait();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//            data.forEach(sub -> System.out.println(sub.getName()));
//        }
//    }

    void configSubBox (ComboBox<Subject> box1, ComboBox<Subject> box2, ResultSet rs) throws SQLException {
        ObservableList<Subject> data1 = box1.getItems();
        ObservableList<Subject> data2 = box2.getItems();

        while (rs.next()) {
            if (rs.getInt("sem") == 1) {
                data1.add(new Subject(
                        rs.getInt("subject_code"),
                        rs.getInt("credits"),
                        new Course(rs.getInt("course_id"),
                                rs.getString("name"),
                                rs.getInt("duration"),
                                rs.getInt("credit_limit"),
                                rs.getString("type"),
                                new Faculty()
                        ),
                        rs.getInt("sem"),
                        rs.getString("name"),
                        rs.getDouble("fee"),
                        rs.getInt("compulsory"),
                        rs.getString("type")
                ));
            } else {
                data2.add(new Subject(
                        rs.getInt("subject_code"),
                        rs.getInt("credits"),
                        new Course(rs.getInt("course_id"),
                                rs.getString("name"),
                                rs.getInt("duration"),
                                rs.getInt("credit_limit"),
                                rs.getString("type"),
                                new Faculty()
                        ),
                        rs.getInt("sem"),
                        rs.getString("name"),
                        rs.getDouble("fee"),
                        rs.getInt("compulsory"),
                        rs.getString("type")
                ));
            }
        }
    }

//    add subjects into Subject ListViews
void configSubList(ListView<Subject> listView1, ListView<Subject> listView2, ResultSet rs) throws SQLException {
        ObservableList<Subject> data1 = listView1.getItems();
        ObservableList<Subject> data2 = listView2.getItems();
        while (rs.next()) {
            if (rs.getInt("sem") == 1) {
                data1.add(new Subject(
                        rs.getInt("subject_code"),
                        rs.getInt("credits"),
                        new Course(rs.getInt("course_id"),
                                rs.getString("name"),
                                rs.getInt("duration"),
                                rs.getInt("credit_limit"),
                                rs.getString("type"),
                                new Faculty()
                        ),
                        rs.getInt("sem"),
                        rs.getString("name"),
                        rs.getDouble("fee"),
                        rs.getInt("compulsory"),
                        rs.getString("type")
                ));
            } else {
                data2.add(new Subject(
                        rs.getInt("subject_code"),
                        rs.getInt("credits"),
                        new Course(rs.getInt("course_id"),
                                rs.getString("name"),
                                rs.getInt("duration"),
                                rs.getInt("credit_limit"),
                                rs.getString("type"),
                                new Faculty()
                        ),
                        rs.getInt("sem"),
                        rs.getString("name"),
                        rs.getDouble("fee"),
                        rs.getInt("compulsory"),
                        rs.getString("type")
                ));
            }
        }
    }
}
