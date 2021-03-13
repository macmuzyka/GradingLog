package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import model.Datasource;

/**
 * Created by Raweshau
 * on 24.10.2020
 */

public class NewStudentDialog {

    @FXML
    private TextField newStudentTextField;

    @FXML
    public void processStudentResults() {
        String studentName = newStudentTextField.getText().toUpperCase().trim();
        Datasource.getInstance().insertStudent(studentName);
        System.out.println(studentName + "  successfully added!");
    }


    public TextField getNewStudentTextField() {
        return newStudentTextField;
    }
}
