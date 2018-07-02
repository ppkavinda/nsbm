package helpers;

import javafx.scene.control.TextField;

public class Validate {
    public static boolean validateTf (TextField [] tfList) throws NullPointerException {
        for (TextField field: tfList) {
            System.out.println("Validating " + field.getPromptText() + " Field");
            if (field.getText().equals("")) {
                throw new NullPointerException(field.getPromptText() + " cannot be empty!");
            }
        }
        return true;
    }
}
