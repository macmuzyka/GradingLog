package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import model.Datasource;
import model.Grade;
import model.Student;
import model.Subject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raweshau
 * on 25.10.2020
 */

public class NewStudentSubjectGrade {

    @FXML
    public ChoiceBox<String> studentChoiceBox;
    @FXML
    public ChoiceBox<String> subjectChoiceBox;
    @FXML
    public ChoiceBox<Integer> gradeChoiceBox;

    public void initialize() {
        List<Subject> subjects = new ArrayList<>(Datasource.getInstance().listSubjects());
        for (Subject subject : subjects) {
            subjectChoiceBox.getItems().add(subject.getSubjectName());
        }
        List<Student> students = new ArrayList<>(Datasource.getInstance().listStudents());
        for (Student student : students) {
            studentChoiceBox.getItems().add(student.getStudentName());
        }
        List<Grade> grades = new ArrayList<>(Datasource.getInstance().listGrades());
        for (Grade grade : grades) {
            gradeChoiceBox.getItems().add(grade.getValue());
        }
    }

    @FXML
    public void processStudentSubjectGradeResults() {
        String student = studentChoiceBox.getValue();
        String subject = subjectChoiceBox.getValue();
        String grade = String.valueOf(gradeChoiceBox.getValue());

        Datasource.getInstance().insertStudentSubjectGrade(student, subject, grade);
    }

    public ChoiceBox<String> getStudentChoiceBox() {
        return studentChoiceBox;
    }

    public ChoiceBox<String> getSubjectChoiceBox() {
        return subjectChoiceBox;
    }

    public ChoiceBox<Integer> getGradeChoiceBox() {
        return gradeChoiceBox;
    }
}
