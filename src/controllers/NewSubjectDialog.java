package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.Datasource;

/**
 * Created by Raweshau
 * on 25.10.2020
 */

public class NewSubjectDialog {

    @FXML
    private TextField newSubjectTextField;

    @FXML
    public void processSubjectResults() {
        String subjectName = newSubjectTextField.getText().toUpperCase().trim();
        Datasource.getInstance().insertSubject(subjectName);
        System.out.println(subjectName + "  successfully added!");
    }


    public TextField getNewSubjectTextField() {
        return newSubjectTextField;
    }

}
