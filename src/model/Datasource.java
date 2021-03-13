package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raweshau
 * on 22.10.2020
 */

public class Datasource {

    /**
     * SSG = Student Subject Grades Abbreviation
     */

    public static final String DATABASE_NAME = "\\gradinglog.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:src\\database" + DATABASE_NAME;

    public static final String TABLE_STUDENTS = "Students";
    public static final String COLUMN_STUDENT_ID = "IDStudent";
    public static final String COLUMN_STUDENT_NAME = "StudentName";

    public static final String TABLE_GRADES = "Grades";
    public static final String COLUMN_GRADE_ID = "IDGrade";
    public static final String COLUMN_GRADE_VALUE = "Value";

    public static final String TABLE_SUBJECTS = "Subjects";
    public static final String COLUMN_SUBJECT_ID = "IDSubject";
    public static final String COLUMN_SUBJECT_NAME = "SubjectName";

    public static final String TABLE_STUDENT_SUBJECT_GRADES = "StudentSubjectGrades";
    public static final String COLUMN_SSG_ID = "IDStudentSubjectGrades";
    public static final String COLUMN_SSG_STUDENT_ID = "StudentID";
    public static final String COLUMN_SSG_SUBJECT_ID = "SubjectID";
    public static final String COLUMN_SSG_GRADE_ID = "GradeID";

    public static final String TABLE_STUDENT_SUBJECT_GRADE_VIEW = "list_studentsSubjectsGrades";
    public static final String COLUMN_LIST_SSG_STUDENT_NAME = "StudentName";
    public static final String COLUMN_LIST_SSG_SUBJECT_NAME = "SubjectName";

    public static final String QUERY_ALL_STUDENT_SUBJECT_GRADE = "SELECT * FROM " + TABLE_STUDENT_SUBJECT_GRADE_VIEW;

    public static final String QUERY_ALL_SUBJECT = "SELECT DISTINCT SubjectName FROM " + TABLE_SUBJECTS;

    public static final String QUERY_ALL_STUDENT = "SELECT DISTINCT StudentName FROM " + TABLE_STUDENTS;

    public static final String QUERY_ALL_GRADE = "SELECT DISTINCT " + COLUMN_GRADE_VALUE + " FROM " + TABLE_GRADES;

    public static final String QUERY_STUDENT_GRADES = QUERY_ALL_STUDENT_SUBJECT_GRADE + " WHERE "
            + COLUMN_LIST_SSG_STUDENT_NAME + " = ";

    public static final String QUERY_STUDENT_SUBJECT_FOR_AVERAGE_SCORE_START = "SELECT " + COLUMN_LIST_SSG_STUDENT_NAME
            + ", " + COLUMN_LIST_SSG_SUBJECT_NAME + ", CAST (round(avg(StudentSubjectGrades),2) AS TEXT) FROM "
            + TABLE_STUDENT_SUBJECT_GRADE_VIEW + " WHERE " + COLUMN_LIST_SSG_STUDENT_NAME + " = ";

    public static final String INSERT_INTO_STUDENT = "INSERT INTO " + TABLE_STUDENTS + "(" + COLUMN_STUDENT_NAME + ") SELECT (?) " +
            " WHERE NOT EXISTS (SELECT " + COLUMN_STUDENT_NAME + " FROM " + TABLE_STUDENTS + " WHERE " + COLUMN_STUDENT_NAME + " = ?)";

    public static final String INSERT_INTO_SUBJECT = "INSERT INTO " + TABLE_SUBJECTS + " (" + COLUMN_SUBJECT_NAME + ") SELECT (?) " +
            "WHERE NOT EXISTS (SELECT " + COLUMN_SUBJECT_NAME + " FROM " + TABLE_SUBJECTS + " WHERE " + COLUMN_SUBJECT_NAME + " = ?)";

    public static final String QUERY_STUDENT = "SELECT " + COLUMN_STUDENT_ID + " FROM " + TABLE_STUDENTS + " WHERE "
            + COLUMN_STUDENT_NAME + " = ?";

    public static final String QUERY_SUBJECT = "SELECT " + COLUMN_SUBJECT_ID + " FROM " + TABLE_SUBJECTS + " WHERE "
            + COLUMN_SUBJECT_NAME + " = ?";

    public static final String QUERY_GRADE = "SELECT " + COLUMN_GRADE_ID + " FROM " + TABLE_GRADES + " WHERE "
            + COLUMN_GRADE_VALUE + " = ?";

    public static final String INSERT_STUDENT_SUBJECT_GRADE = "INSERT INTO " + TABLE_STUDENT_SUBJECT_GRADES
            + "(" + COLUMN_SSG_STUDENT_ID + ", "
            + COLUMN_SSG_SUBJECT_ID + ", "
            + COLUMN_SSG_GRADE_ID + ") VALUES (?, ?, ?)";

    public static final String DELETE_SSG_RECORD = "DELETE FROM " + TABLE_STUDENT_SUBJECT_GRADES + " WHERE "
            + COLUMN_SSG_STUDENT_ID + " = ? AND " + COLUMN_SSG_SUBJECT_ID + " = ? AND " + COLUMN_SSG_GRADE_ID + " = ?";

    public static final String QUERY_SSG_ID = "SELECT " + COLUMN_SSG_ID + " FROM " + TABLE_STUDENT_SUBJECT_GRADES + " WHERE "
            + COLUMN_SSG_STUDENT_ID + " = ? AND " + COLUMN_SSG_SUBJECT_ID + " = ? AND " + COLUMN_SSG_GRADE_ID + " = ?";

    public static final String UPDATE_SSG = "UPDATE " + TABLE_STUDENT_SUBJECT_GRADES + " SET " + COLUMN_SSG_STUDENT_ID
            + " = ?, " + COLUMN_SSG_SUBJECT_ID + " = ?, " + COLUMN_SSG_GRADE_ID + " = ? WHERE " + COLUMN_SSG_ID + " = ?";


    private Connection conn;

    private PreparedStatement insertStudent;
    private PreparedStatement insertSubject;
    private PreparedStatement queryStudent;
    private PreparedStatement querySubject;
    private PreparedStatement queryGrade;
    private PreparedStatement insertStudentSubjectGrade;
    private PreparedStatement deleteStudentSubjectGradeRecord;
    private PreparedStatement querySSGId;
    private PreparedStatement updateSSGRecord;

    private static final Datasource instance = new Datasource();

    private Datasource() {
    }

    public static Datasource getInstance() {
        return instance;
    }

    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            insertStudent = conn.prepareStatement(INSERT_INTO_STUDENT);
            insertSubject = conn.prepareStatement(INSERT_INTO_SUBJECT);
            queryStudent = conn.prepareStatement(QUERY_STUDENT);
            querySubject = conn.prepareStatement(QUERY_SUBJECT);
            queryGrade = conn.prepareStatement(QUERY_GRADE);
            insertStudentSubjectGrade = conn.prepareStatement(INSERT_STUDENT_SUBJECT_GRADE);
            deleteStudentSubjectGradeRecord = conn.prepareStatement(DELETE_SSG_RECORD);
            querySSGId = conn.prepareStatement(QUERY_SSG_ID);
            updateSSGRecord = conn.prepareStatement(UPDATE_SSG);

            return true;
        } catch (SQLException e) {
            System.out.println("Error establishing proper connection to the database: " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            if (updateSSGRecord != null) {
                updateSSGRecord.close();
            }

            if (querySSGId != null) {
                querySSGId.close();
            }

            if (deleteStudentSubjectGradeRecord != null) {
                deleteStudentSubjectGradeRecord.close();
            }

            if (insertStudentSubjectGrade != null) {
                insertStudentSubjectGrade.close();
            }

            if (queryGrade != null) {
                queryGrade.close();
            }

            if (querySubject != null) {
                querySubject.close();
            }

            if (queryStudent != null) {
                queryStudent.close();
            }

            if (insertSubject != null) {
                insertSubject.close();
            }

            if (insertStudent != null) {
                insertStudent.close();
            }

            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Error closing the database: " + e.getMessage());
        }
    }

    public List<StudentSubjectGrade> queryAllGrades() {

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(QUERY_ALL_STUDENT_SUBJECT_GRADE)) {

            List<StudentSubjectGrade> allPossibleGrades = new ArrayList<>();

            while (results.next()) {

                try {
                    Thread.sleep(35);
                } catch (InterruptedException e) {
                    System.out.println("Interrupted: " + e.getMessage());
                }

                StudentSubjectGrade gradeData = new StudentSubjectGrade();
                gradeData.setStudentName(results.getString(1));
                gradeData.setSubjectName(results.getString(2));
                gradeData.setGradeValue(results.getString(3));
                allPossibleGrades.add(gradeData);
            }

            return allPossibleGrades;

        } catch (SQLException e) {
            System.out.println("Error querying all grades: " + e.getMessage());
            return null;
        }
    }

    public List<Grade> listGrades() {

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(QUERY_ALL_GRADE)) {

            List<Grade> grades = new ArrayList<>();

            while (results.next()) {
                Grade grade = new Grade();
                grade.setValue(results.getInt(1));
                grades.add(grade);
            }
            return grades;

        } catch (SQLException e) {
            System.out.println("Error querying for grades: " + e.getMessage());
            return null;
        }
    }

    public List<Subject> listSubjects() {

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(QUERY_ALL_SUBJECT)) {

            List<Subject> subjects = new ArrayList<>();

            while (results.next()) {
                Subject subject = new Subject();
                subject.setSubjectName(results.getString(1));
                subjects.add(subject);
            }

            return subjects;

        } catch (SQLException e) {
            System.out.println("Error querying subjects: " + e.getMessage());
            return null;
        }
    }

    public List<Student> listStudents() {

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(QUERY_ALL_STUDENT)) {

            List<Student> students = new ArrayList<>();

            while (results.next()) {
                Student student = new Student();
                student.setStudentName(results.getString(1));
                students.add(student);
            }

            return students;

        } catch (SQLException e) {
            System.out.println("Error querying students: " + e.getMessage());
            return null;
        }
    }

    public List<StudentSubjectGrade> listStudentGrades(String studentName) {

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(QUERY_STUDENT_GRADES + "\"" + studentName + "\"")) {

            List<StudentSubjectGrade> studentGrades = new ArrayList<>();

            while (results.next()) {
                StudentSubjectGrade student = new StudentSubjectGrade();
                student.setStudentName(results.getString(1));
                student.setSubjectName(results.getString(2));
                student.setGradeValue(results.getString(3));
                studentGrades.add(student);
            }

            return studentGrades;

        } catch (SQLException e) {
            System.out.println("Error querying student grades: " + e.getMessage());
            return null;
        }
    }

    public List<StudentSubjectGrade> listStudentGradesForGivenSubject(String studentName, String subjectName) {
        StringBuilder sb = new StringBuilder(QUERY_STUDENT_GRADES);
        sb.append("\"").append(studentName).append("\"");
        sb.append(" AND ");
        sb.append(COLUMN_LIST_SSG_SUBJECT_NAME);
        sb.append(" = ");
        sb.append("\"").append(subjectName).append("\"");

        String QUERY_STUDENT_GRADES_FOR_SUBJECT = sb.toString();

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(QUERY_STUDENT_GRADES_FOR_SUBJECT)) {

            List<StudentSubjectGrade> studentGradesForSubject = new ArrayList<>();

            while (results.next()) {
                StudentSubjectGrade studentGrades_Subject = new StudentSubjectGrade();
                studentGrades_Subject.setStudentName(results.getString(1));
                studentGrades_Subject.setSubjectName(results.getString(2));
                studentGrades_Subject.setGradeValue(results.getString(3));
                studentGradesForSubject.add(studentGrades_Subject);
            }

            return studentGradesForSubject;

        } catch (SQLException e) {
            System.out.println("Error querying student grades for a given subject: " + e.getMessage());
            return null;
        }
    }

    public List<StudentSubjectGrade> listSubjectGrades(String subject) {
        StringBuilder sb = new StringBuilder(QUERY_ALL_STUDENT_SUBJECT_GRADE);
        sb.append(" WHERE " + COLUMN_LIST_SSG_SUBJECT_NAME + " = ");
        sb.append("\"").append(subject).append("\"");

        String QUERY_SUBJECT_GRADES = sb.toString();

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(QUERY_SUBJECT_GRADES)) {

            List<StudentSubjectGrade> subjectGrades = new ArrayList<>();

            while (results.next()) {
                StudentSubjectGrade subjectGrade = new StudentSubjectGrade();
                subjectGrade.setStudentName(results.getString(1));
                subjectGrade.setSubjectName(results.getString(2));
                subjectGrade.setGradeValue(results.getString(3));
                subjectGrades.add(subjectGrade);
            }

            return subjectGrades;

        } catch (SQLException e) {
            System.out.println("Error querying subject grades: " + e.getMessage());
            return null;
        }
    }

    public List<StudentSubjectGrade> queryForAverage(String student, String subject) {
        StringBuilder sb = new StringBuilder(QUERY_STUDENT_SUBJECT_FOR_AVERAGE_SCORE_START);
        sb.append("\"").append(student).append("\"");
        sb.append(" AND " + COLUMN_LIST_SSG_SUBJECT_NAME + " = ");
        sb.append("\"").append(subject).append("\"");

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {

            List<StudentSubjectGrade> averageScore = new ArrayList<>();

            while (results.next()) {
                StudentSubjectGrade studentAverage = new StudentSubjectGrade();
                studentAverage.setStudentName(results.getString(1));
                studentAverage.setSubjectName(results.getString(2));
                studentAverage.setGradeValue(String.valueOf(results.getDouble(3)));
                averageScore.add(studentAverage);
            }

            return averageScore;

        } catch (SQLException e) {
            System.out.println("Error querying for average score: " + e.getMessage());
            return null;
        }

    }

    public void insertStudent(String studentName) {

        try {
            conn.setAutoCommit(false);

            insertStudent.setString(1, studentName);
            insertStudent.setString(2, studentName);
            int affectedRows = insertStudent.executeUpdate();

            if (affectedRows == 1) {
                conn.commit();
            } else {
                throw new SQLException("Picture insertion failed!");
            }
        } catch (SQLException e) {
            System.out.println("Student insertion exception: " + e.getMessage());
            try {
                System.out.println("Performing rollback");
                conn.rollback();
            } catch (SQLException e2) {
                System.out.println("Rollback failed!" + e2.getMessage());
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Couldn't reset auto commit: " + e.getMessage());
            }
        }

    }

    public void insertSubject(String subjectName) {

        try {
            conn.setAutoCommit(false);

            insertSubject.setString(1, subjectName);
            insertSubject.setString(2, subjectName);
            int affectedRows = insertSubject.executeUpdate();

            if (affectedRows == 1) {
                conn.commit();
            } else {
                throw new SQLException("Subject insertion failed!");
            }

        } catch (SQLException e) {
            System.out.println("Subject insertion exception: " + e.getMessage());
            try {
                System.out.println("Performing rollback");
                conn.rollback();
            } catch (SQLException e2) {
                System.out.println("Rollback failed!" + e.getMessage());
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Couldn't reset auto commit: " + e.getMessage());
            }
        }
    }

    public void deleteSSGRecord(String studentName, String subjectName, String gradeValue) throws SQLException {

        try {
            conn.setAutoCommit(false);
            int studentID = queryStudent(studentName);
            int subjectID = querySubject(subjectName);
            int gradeID = queryGrade(gradeValue);
            deleteStudentSubjectGradeRecord.setInt(1, studentID);
            deleteStudentSubjectGradeRecord.setInt(2, subjectID);
            deleteStudentSubjectGradeRecord.setInt(3, gradeID);
            int affectedRows = deleteStudentSubjectGradeRecord.executeUpdate();
            if (affectedRows == 1) {
                conn.commit();
            } else {
                throw new SQLException("Deleting Student Subject Grade failed!");
            }
        } catch (SQLException e) {
            System.out.println("Error Deleting Student Subject Grade..." + e.getMessage());
            try {
                System.out.println("Performing rollback");
                conn.rollback();
            } catch (SQLException e2) {
                System.out.println("Error performing rollback: " + e2.getMessage());
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Couldn't reset auto commit: " + e.getMessage());
            }
        }
    }

    public void insertStudentSubjectGrade(String studentName, String subjectName, String gradeValue) {

        try {
            conn.setAutoCommit(false);
            int studentID = queryStudent(studentName);
            int subjectID = querySubject(subjectName);
            int gradeID = queryGrade(gradeValue);
            insertStudentSubjectGrade.setInt(1, studentID);
            insertStudentSubjectGrade.setInt(2, subjectID);
            insertStudentSubjectGrade.setInt(3, gradeID);
            int affectedRows = insertStudentSubjectGrade.executeUpdate();
            if (affectedRows == 1) {
                conn.commit();
            } else {
                throw new SQLException("Student Subject Grade insertion failed!");
            }
        } catch (SQLException e) {
            System.out.println("Error inserting Student Subject Grade..." + e.getMessage());
            try {
                System.out.println("Performing rollback");
                conn.rollback();
            } catch (SQLException e2) {
                System.out.println("Error performing rollback: " + e2.getMessage());
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Couldn't reset auto commit: " + e.getMessage());
            }
        }

    }

    public boolean updateSSGRecord(String oldStudentName, String oldSubjectName, String oldGradeValue,
                                   String newStudentName, String newSubjectName, String newGradeValue) {
        try {
            conn.setAutoCommit(false);

            int studentID = queryStudent(newStudentName);
            int subjectID = querySubject(newSubjectName);
            int gradeID = queryGrade(newGradeValue);
            int SSGId = querySSGId(oldStudentName, oldSubjectName, oldGradeValue);
            updateSSGRecord.setInt(1, studentID);
            updateSSGRecord.setInt(2, subjectID);
            updateSSGRecord.setInt(3, gradeID);
            updateSSGRecord.setInt(4, SSGId);
            int affectedRows = updateSSGRecord.executeUpdate();
            if (affectedRows == 1) {
                conn.commit();
                return true;
            } else {
                throw new SQLException("SSG Record update failed");
            }
        } catch (SQLException e) {
            System.out.println("Error updating record: " + e.getMessage());
            try {
                System.out.println("Performing rollback");
                conn.rollback();
            } catch (SQLException e2) {
                System.out.println("Error performing rollback!: " + e2.getMessage());
            }
        } finally {
            try {
                System.out.println("Resetting default commit behaviour");
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Couldn't reset auto commit! " + e.getMessage());
            }
        }
        return false;
    }


    private int querySSGId(String oldStudentName, String oldSubjectName, String oldGradeVale) {
        try {
            querySSGId.setInt(1, queryStudent(oldStudentName));
            querySSGId.setInt(2, querySubject(oldSubjectName));
            querySSGId.setInt(3, queryGrade(oldGradeVale));
            ResultSet results = querySSGId.executeQuery();
            if (results.next()) {
                return results.getInt(1);
            } else {
                querySSGId.setString(1, oldStudentName);
                querySSGId.setString(2, oldSubjectName);
                querySSGId.setString(3, oldGradeVale);
                int affectedRows = querySSGId.executeUpdate();
                if (affectedRows != 1) {
                    throw new SQLException("Couldn't query for SSG ID");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in querySSGId: " + e.getMessage());
        }

        return -1;
    }


    private int queryStudent(String studentName) throws SQLException {
        queryStudent.setString(1, studentName);
        ResultSet results = queryStudent.executeQuery();

        if (results.next()) {
            return results.getInt(1);
        } else {
            queryStudent.setString(1, studentName);
            int affectedRows = queryStudent.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException("Couldn't query for student");
            }
        }

        return -1;
    }

    private int querySubject(String subjectName) throws SQLException {
        querySubject.setString(1, subjectName);
        ResultSet results = querySubject.executeQuery();

        if (results.next()) {
            return results.getInt(1);
        } else {
            querySubject.setString(1, subjectName);
            int affectedRows = queryStudent.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException("Couldn't query for subject");
            }
        }

        return -1;
    }

    private int queryGrade(String gradeValue) throws SQLException {
        queryGrade.setString(1, gradeValue);
        ResultSet results = queryGrade.executeQuery();

        if (results.next()) {
            return results.getInt(1);
        } else {
            queryGrade.setString(1, gradeValue);
            int affectedRows = queryGrade.executeUpdate();
            if (affectedRows != 1) {
                throw new SQLException("Couldn't query for grade");
            }
        }

        return -1;
    }
}

