package models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

class Staff {
    private final SimpleIntegerProperty staff_id = new SimpleIntegerProperty(0);
    private  final SimpleStringProperty fname = new SimpleStringProperty("");
    private final SimpleStringProperty lname = new SimpleStringProperty("");
    private final SimpleStringProperty email = new SimpleStringProperty("");
    private final SimpleStringProperty gender = new SimpleStringProperty("");



//    CONSTRUCTORS  GETTERS SETTERS

    public Staff () {
        this (0, "", "", "", "");
    }

    public Staff (int staff_id, String fname, String lname, String email, String gender) {
        setStaff_id(staff_id);
        setFname(fname);
        setLname(lname);
        setEmail(email);
        setGender(gender);
    }

    public int getStaff_id() {
        return staff_id.get();
    }

    public SimpleIntegerProperty staff_idProperty() {
        return staff_id;
    }

    public void setStaff_id(int staff_id) {
        this.staff_id.set(staff_id);
    }

    public String getFname() {
        return fname.get();
    }

    public SimpleStringProperty fnameProperty() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname.set(fname);
    }

    public String getLname() {
        return lname.get();
    }

    public SimpleStringProperty lnameProperty() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname.set(lname);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getGender() {
        return gender.get();
    }

    public SimpleStringProperty genderProperty() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }
}
