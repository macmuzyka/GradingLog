package model;

/**
 * Created by Raweshau
 * on 22.10.2020
 */

public class StudentSubjectGrade {

    //

    private int IDStudentSubjectGrade;
    private String studentName;
    private String subjectName;
    private String gradeValue;


    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getGradeValue() {
        return gradeValue;
    }

    public void setGradeValue(String gradeValue) {
        this.gradeValue = gradeValue;
    }
}
