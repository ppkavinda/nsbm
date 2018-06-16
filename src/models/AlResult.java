package models;

import javafx.beans.property.SimpleStringProperty;

public class AlResult {
    private SimpleStringProperty sub1 = new SimpleStringProperty("");
    private SimpleStringProperty sub2 = new SimpleStringProperty("");
    private SimpleStringProperty sub3 = new SimpleStringProperty("");

    public AlResult () {
        this (null, null, null);
    }

    public AlResult (String sub1, String sub2, String sub3) {
        setSub1(sub1);
        setSub2(sub2);
        setSub3(sub3);
    }

    public String getSub1() {
        return sub1.get();
    }

    public SimpleStringProperty sub1Property() {
        return sub1;
    }

    public void setSub1(String sub1) {
        this.sub1.set(sub1);
    }

    public String getSub2() {
        return sub2.get();
    }

    public SimpleStringProperty sub2Property() {
        return sub2;
    }

    public void setSub2(String sub2) {
        this.sub2.set(sub2);
    }

    public String getSub3() {
        return sub3.get();
    }

    public SimpleStringProperty sub3Property() {
        return sub3;
    }

    public void setSub3(String sub3) {
        this.sub3.set(sub3);
    }
}
