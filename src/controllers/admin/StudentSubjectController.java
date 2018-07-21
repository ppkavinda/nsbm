package controllers.admin;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import models.Subject;
import models.Undergraduate;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentSubjectController {

    private Subject subject = new Subject();
    private Undergraduate ug = new Undergraduate();

//    REMOVE SELECTED SUBJECT FROM SUBJECT LIST VIEW
    protected void removeSubject (ListView<Subject> subList, ComboBox<Subject> subBox, Text creditsLabel) {
        Subject selectedSub = subList.getSelectionModel().getSelectedItem();
        if (selectedSub != null) {
            subList.getItems().remove(selectedSub);
            // add it back to the combo box
            subBox.getItems().add(selectedSub);

            // decrease total credits
            int credits = Integer.parseInt(creditsLabel.getText());
            credits -= selectedSub.getCredits();
            creditsLabel.setText(String.valueOf(credits));
        }
    }

//    ADD SELECTED SUBJECT (ON SUBJECTS COMBO) TO SUBJECTS LIST VIEW
    protected void addSubject (ListView<Subject> subList, ComboBox<Subject> subBox, Text creditsLabel) {
        ObservableList<Subject> data = subList.getItems();
        Subject selectedSub = subBox.getSelectionModel().getSelectedItem();
        if (selectedSub != null) {
            data.add(selectedSub);

            // remove it from combo box
            subBox.getItems().remove(selectedSub);

            // increase total credits
            int credits = Integer.parseInt(creditsLabel.getText());
            credits += selectedSub.getCredits();
            creditsLabel.setText(String.valueOf(credits));
        }
    }

//    CLEAR LIST VIEWS AND ADD COMPULSORY SUBJECTS (WHEN ADD BUTTON CLICKED)
    protected void clearSemList (ListView<Subject> sem1SubList, ListView<Subject> sem2SubList, Text sem1CreditsLabel, Text sem2CreditsLabel, String subType) {
        configSemList(0, sem1SubList, sem2SubList, sem1CreditsLabel, sem2CreditsLabel, subType);
    }

//    INSERT SELECTED SUBJECTS INTO LIST VIEWS (EDIT BUTTON CLICKED)

    /**
     * @param subType   UG or PG
     */
    protected void configSemList (int student_id, ListView<Subject> sem1SubList, ListView<Subject> sem2SubList, Text sem1CreditsLabel, Text sem2CreditsLabel, String subType) {
        int totalSem1Credits = 0;
        int totalSem2Credits = 0;
        ObservableList<Subject> sem1List = sem1SubList.getItems();
        ObservableList<Subject> sem2List = sem2SubList.getItems();
        sem1List.clear();
        sem2List.clear();

        ResultSet rs = ug.getSubjects(student_id, subType);

        try {
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

    protected void configSubBoxes (ComboBox<Subject> sem1SubBox, ComboBox<Subject> sem2SubBox, String subType) {
        ResultSet rs = subject.get();

        try {
            ObservableList<Subject> sem1box = sem1SubBox.getItems();
            ObservableList<Subject> sem2box = sem2SubBox.getItems();
            sem1box.clear();
            sem2box.clear();

            while (rs.next()) {
                if (rs.getInt("compulsory") != 1 && rs.getString("type").equals(subType)) {

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
