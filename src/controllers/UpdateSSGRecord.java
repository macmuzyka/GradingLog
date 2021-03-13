package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raweshau
 * on 26.10.2020
 */

public class UpdateSSGRecord {

    @FXML
    private TextField oldStudent;
    @FXML
    private TextField oldSubject;
    @FXML
    private TextField oldGrade;
    @FXML
    private ChoiceBox<String> newStudent;
    @FXML
    private ChoiceBox<String> newSubject;
    @FXML
    private ChoiceBox<Integer> newGrade;

    public void initialize(StudentSubjectGrade record) {
        List<Subject> subjects = new ArrayList<>(Datasource.getInstance().listSubjects());
        for (Subject subject : subjects) {
            newSubject.getItems().add(subject.getSubjectName());
        }
        List<Student> students = new ArrayList<>(Datasource.getInstance().listStudents());
        for (Student student : students) {
            newStudent.getItems().add(student.getStudentName());
        }
        List<Grade> grades = new ArrayList<>(Datasource.getInstance().listGrades());
        for (Grade grade : grades) {
            newGrade.getItems().add(grade.getValue());
        }

        oldStudent.setText(record.getStudentName());
        oldSubject.setText(record.getSubjectName());
        oldGrade.setText(record.getGradeValue());
    }

    public StudentSubjectGrade processSSGRecordUpdate() {
        String newStudentName = newStudent.getValue();
        String newSubjectName = newSubject.getValue();
        String newGradeValue = String.valueOf(newGrade.getValue());

        String oldStudentName = oldStudent.getText();
        String oldSubjectName = oldSubject.getText();
        String oldGradeValue = oldGrade.getText();

        if (Datasource.getInstance().updateSSGRecord(oldStudentName, oldSubjectName, oldGradeValue,
                                                    newStudentName, newSubjectName, newGradeValue)) {
            System.out.println("Successfully updated record");
            System.out.println("===Debug purpose below===");
            System.out.println("Old values: ");
            System.out.println("Name was: " + oldStudent.getText());
            System.out.println("Subject was: " + oldSubject.getText());
            System.out.println("Grade was: " + oldGrade.getText());
            System.out.println("=========================");
            System.out.println("New values: ");
            System.out.println("Name is: " + newStudentName);
            System.out.println("Subject is: " + newSubjectName);
            System.out.println("Grade is: " + newGradeValue);
            StudentSubjectGrade updatedRecord = new StudentSubjectGrade();
            updatedRecord.setStudentName(newStudentName);
            updatedRecord.setSubjectName(newSubjectName);
            updatedRecord.setGradeValue(newGradeValue);

            return updatedRecord;
        } else {
            System.out.println("Error updating record...");
            return null;
        }
    }

    public ChoiceBox<String> getNewStudent() {
        return newStudent;
    }

    public ChoiceBox<String> getNewSubject() {
        return newSubject;
    }

    public ChoiceBox<Integer> getNewGrade() {
        return newGrade;
    }
}
