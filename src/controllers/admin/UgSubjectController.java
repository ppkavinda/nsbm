package controllers.admin;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import models.Course;
import models.Faculty;
import models.Subject;
import models.Undergraduate;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UgSubjectController {

    private Subject subject = new Subject();
    private Undergraduate ug = new Undergraduate();

    private int totalSem1Credits = 10;
    private int totalSem2Credits = 10;

    protected void configSemList (int student_id, ListView<Subject> sem1SubList, ListView<Subject> sem2SubList, Text sem1CreditsLabel, Text sem2CreditsLabel) {
        ObservableList<Subject> sem1List = sem1SubList.getItems();
        ObservableList<Subject> sem2List = sem2SubList.getItems();

        if (student_id == 0 ) {
            sem1List.clear();
            sem2List.clear();
        } else {

            ResultSet rs = ug.getSubjects(student_id);

            try {
                sem1List.clear();
                sem2List.clear();
                while (rs.next()) {
                    if (rs.getInt("sem") == 1) {
                        totalSem1Credits += rs.getInt("credits");
                        sem1List.add(new Subject(
                                rs.getInt("subject_code"),
                                rs.getInt("credits"),
                                rs.getInt("sem"),
                                rs.getString("name"),
                                rs.getDouble("fee")
                        ));
                    } else {
                        totalSem2Credits += rs.getInt("credits");
                        sem2List.add(new Subject(
                                rs.getInt("subject_code"),
                                rs.getInt("credits"),
                                rs.getInt("sem"),
                                rs.getString("name"),
                                rs.getDouble("fee")
                        ));
                    }
                    sem1CreditsLabel.setText(String.valueOf(totalSem1Credits));
                    sem2CreditsLabel.setText(String.valueOf(totalSem2Credits));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    protected void configSubBoxes (ComboBox<Subject> sem1SubBox, ComboBox<Subject> sem2SubBox) {
        ResultSet rs = subject.get();

        try {
            ObservableList<Subject> sem1box = sem1SubBox.getItems();
            ObservableList<Subject> sem2box = sem2SubBox.getItems();
            while (rs.next()) {
                if (rs.getInt("compulsory") != 1) {

                    if (rs.getInt("sem") == 1) {
                        sem1box.add(new Subject(
                                rs.getInt("subject_code"),
                                rs.getInt("credits"),
                                rs.getInt("sem"),
                                rs.getString("name"),
                                rs.getDouble("fee")
                        ));
                    } else {
                        sem2box.add(new Subject(
                                rs.getInt("subject_code"),
                                rs.getInt("credits"),
                                rs.getInt("sem"),
                                rs.getString("name"),
                                rs.getDouble("fee")
                        ));
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
