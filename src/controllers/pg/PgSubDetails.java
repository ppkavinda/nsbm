package controllers.pg;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import models.*;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PgSubDetails {
    private SimpleIntegerProperty sem1TotalCredits = new SimpleIntegerProperty(0);
    private SimpleIntegerProperty sem2TotalCredits = new SimpleIntegerProperty(0);

    public int getSem1TotalCredits() {
        return sem1TotalCredits.get();
    }

    public SimpleIntegerProperty sem1TotalCreditsProperty() {
        return sem1TotalCredits;
    }

    public void setSem1TotalCredits(int sem1TotalCredits) {
        this.sem1TotalCredits.set(sem1TotalCredits);
    }

    public int getSem2TotalCredits() {
        return sem2TotalCredits.get();
    }

    public SimpleIntegerProperty sem2TotalCreditsProperty() {
        return sem2TotalCredits;
    }

    public void setSem2TotalCredits(int sem2TotalCredits) {
        this.sem2TotalCredits.set(sem2TotalCredits);
    }



    //    save selected subject
    void subAdd (Subject sub, Postgraduate pg) {
        pg.addSubject(sub.getSubject_code(), pg.getStudent_id());

//        increment the total credit label value
        if (sub.getSem() == 1) {
            sem1TotalCredits.setValue(sem1TotalCredits.add(sub.getCredits()).getValue());
        } else {
            sem2TotalCredits.setValue(sem2TotalCredits.add(sub.getCredits()).getValue());
        }

        System.out.println("Subject selected.");
    }

    //    de-select selected subject
    void subRemove (Subject sub, Postgraduate pg) {
        pg.removeSubject(sub.getSubject_code(), pg.getStudent_id());

//        decrement the total credit label value
        if (sub.getSem() == 1) {
            sem1TotalCredits.setValue(sem1TotalCredits.subtract(sub.getCredits()).getValue());
        } else {
            sem2TotalCredits.setValue(sem2TotalCredits.subtract(sub.getCredits()).getValue());
        }
        System.out.println("subject de-selected.");
    }

    //    add selectable subjects into subject (initialize) ComboBox
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

    //    add subjects into Subject (initialize) ListViews
    void configSubList(ListView<Subject> listView1, ListView<Subject> listView2, ResultSet rs) throws SQLException {
        ObservableList<Subject> data1 = listView1.getItems();
        ObservableList<Subject> data2 = listView2.getItems();
        while (rs.next()) {
            if (rs.getInt("sem") == 1) {
//                increment the total credit label
                sem1TotalCredits.setValue(sem1TotalCredits.add(rs.getInt("credits")).getValue());

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
//                increment total credit label
                sem2TotalCredits.setValue(sem2TotalCredits.add(rs.getInt("credits")).getValue());

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
