package controllers;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import model.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Controller {

    @FXML
    private TableView<StudentSubjectGrade> allPossibleGradesTable;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private ChoiceBox<String> subjectChoiceBox;
    @FXML
    private ChoiceBox<String> studentChoiceBox;
    @FXML
    private Button studentGradesButton;
    @FXML
    private Button studentSubjectGradesButton;
    @FXML
    private Button studentGradesForSubject;
    @FXML
    private Button averageButton;
    @FXML
    private CheckBox enableHandlerCheckBox;
    @FXML
    private CheckBox disableAutofillCheckBox;
    @FXML
    private Button allGradesButton;
    @FXML
    private BorderPane mainBorderPane;

    private boolean flag;


    public void initialize() {
        listAllGrades();
        populateStudentComboBox();
        populateSubjectComboBox();

        studentChoiceBox.setValue(null);
        subjectChoiceBox.setValue(null);

        studentGradesButton.disableProperty().bind(
                studentChoiceBox.valueProperty().isNull()
        );

        studentGradesForSubject.disableProperty().bind(
                subjectChoiceBox.valueProperty().isNull()
        );

        studentSubjectGradesButton.disableProperty().bind(
                studentChoiceBox.valueProperty().isNull().or
                        (subjectChoiceBox.valueProperty().isNull())
        );

        averageButton.disableProperty().bind(
                studentChoiceBox.valueProperty().isNull().or
                        (subjectChoiceBox.valueProperty().isNull())
        );

        allGradesButton.setEffect(new DropShadow());

        enableHandlerCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if (enableHandlerCheckBox.isSelected()) {
                    System.out.println("Fulfilling choice boxes behavior.");
                    checkedBoxAction();
                } else {
                    System.out.println("Changing behaviour.");
                    disableAutofill();
                }
            }
        });
    }

    @FXML
    public void updateSSG() {
        StudentSubjectGrade recordToUpdate = allPossibleGradesTable.getSelectionModel().getSelectedItem();
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Update SSG Record");
        dialog.setHeaderText("Using this dialog to update SSG Record");
        UpdateSSGRecord controller = new UpdateSSGRecord();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("updateSSGRecord.fxml"));
        fxmlLoader.setController(controller);

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog for Updating SSG Record");
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        UpdateSSGRecord finalController = controller;
        dialog.getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(
                finalController.getNewStudent().valueProperty().isNull().or
                        (finalController.getNewSubject().valueProperty().isNull().or
                                (finalController.getNewGrade().valueProperty().isNull()))
        );

        try {
            controller.initialize(recordToUpdate);
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {

                controller = fxmlLoader.getController();
                StudentSubjectGrade updatedRecord = controller.processSSGRecordUpdate();
                listAllGrades();
                allPossibleGradesTable.getSelectionModel().select(updatedRecord);
            }
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No item selected!");
            alert.setHeaderText("No item was selected to be updated!");
            alert.setContentText("Select item to be updated next time!");
            alert.showAndWait();
        }
    }

    @FXML
    public void deleteSSGRecord() {
        StudentSubjectGrade recordToDelete = allPossibleGradesTable.getSelectionModel().getSelectedItem();
        String studentName = recordToDelete.getStudentName();
        String subjectName = recordToDelete.getSubjectName();
        String gradeValue = recordToDelete.getGradeValue();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Todo Item");
        alert.setHeaderText("Attempting to delete record: \n"
                                        + "[Student]: " + recordToDelete.getStudentName() + "\n"
                                        + "[Subject]: " + recordToDelete.getSubjectName() + "\n"
                                        + "[Grade]: " + recordToDelete.getGradeValue());

        alert.setContentText("Are you sure? Press OK to confirm, or CANCEL");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Datasource.getInstance().deleteSSGRecord(studentName, subjectName, gradeValue);
                listAllGrades();
            } catch (SQLException e) {
                System.out.println("Error deleting record: " + e.getMessage());
            }
        }
    }

    @FXML
    public void addStudentSubjectGrade() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add Student Subject Grade");
        dialog.setHeaderText("Using this dialog to add new Student Subject Grade");
        NewStudentSubjectGrade controller = new NewStudentSubjectGrade();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("newStudentSubjectGrade.fxml"));
        fxmlLoader.setController(controller);
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog for New Student Subject Grade");
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        NewStudentSubjectGrade finalController = controller;
        dialog.getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(
                finalController.getStudentChoiceBox().valueProperty().isNull().or
                        (finalController.getSubjectChoiceBox().valueProperty().isNull().or
                                (finalController.getGradeChoiceBox().valueProperty().isNull())));


        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            controller = fxmlLoader.getController();
            controller.processStudentSubjectGradeResults();
            listAllGrades();
        } else {
            System.out.println("Cancel pressed");
        }
    }

    @FXML
    public void addNewStudentDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add New Student");
        dialog.setHeaderText("Using this dialog to add new Student");
        NewStudentDialog controller = new NewStudentDialog();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("newStudentDialog.fxml"));
        fxmlLoader.setController(controller);
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog for New Student");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        NewStudentDialog finalController = controller;
        dialog.getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(Bindings.createBooleanBinding(
                () -> finalController.getNewStudentTextField().getText().isBlank(),
                finalController.getNewStudentTextField().textProperty()
        ));

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            controller = fxmlLoader.getController();
            controller.processStudentResults();
            populateStudentComboBox();
        } else {
            System.out.println("Cancel pressed");
        }


    }

    @FXML
    public void addNewSubjectDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add New Subject");
        dialog.setHeaderText("Using this dialog to add new Subject");
        NewSubjectDialog controller = new NewSubjectDialog();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("newSubjectDialog.fxml"));
        fxmlLoader.setController(controller);
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog for New Student");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        NewSubjectDialog finalController = controller;
        dialog.getDialogPane().lookupButton(ButtonType.OK).disableProperty().bind(Bindings.createBooleanBinding(
                () -> finalController.getNewSubjectTextField().getText().isBlank(),
                finalController.getNewSubjectTextField().textProperty()
        ));

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            controller = fxmlLoader.getController();
            controller.processSubjectResults();
            populateSubjectComboBox();
        } else {
            System.out.println("Cancel pressed");
        }
    }

    @FXML
    public void checkedBoxAction() {

        flag = true;
        disableAutofillCheckBox.setSelected(false);
        allPossibleGradesTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<StudentSubjectGrade>() {
            @Override
            public void changed(ObservableValue<? extends StudentSubjectGrade> observableValue, StudentSubjectGrade oldValue, StudentSubjectGrade newValue) {
                if (newValue != null && flag) {
                    StudentSubjectGrade item = allPossibleGradesTable.getSelectionModel().getSelectedItem();
                    studentChoiceBox.setValue(item.getStudentName());
                    subjectChoiceBox.setValue(item.getSubjectName());
                }


            }
        });
    }

    @FXML
    public void disableAutofill() {

        flag = false;
        enableHandlerCheckBox.setSelected(false);
        allPossibleGradesTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<StudentSubjectGrade>() {
            @Override
            public void changed(ObservableValue<? extends StudentSubjectGrade> observableValue, StudentSubjectGrade oldValue, StudentSubjectGrade newValue) {
                if (newValue != null) {
                    allPossibleGradesTable.getItems();
                }
            }
        });
    }

    @FXML
    public void listAllGrades() {
        Task<ObservableList<StudentSubjectGrade>> task = new QueryAllGradesTask();

        allPossibleGradesTable.itemsProperty().bind(task.valueProperty());

        progressBar.progressProperty().bind(task.workDoneProperty());

        progressBar.setVisible(true);
        task.setOnSucceeded(e -> progressBar.setVisible(false));
        task.setOnFailed(e -> progressBar.setVisible(false));

        studentChoiceBox.setValue(null);
        subjectChoiceBox.setValue(null);

        new Thread(task).start();
    }

    @FXML
    public void listAllGradesAgain() {
        listAllGrades();
        populateStudentComboBox();
        populateSubjectComboBox();
    }

    @FXML
    public void populateSubjectComboBox() {
        subjectChoiceBox.getItems().retainAll();
        List<Subject> subjects = new ArrayList<>(Datasource.getInstance().listSubjects());
        for (Subject subject : subjects) {
            subjectChoiceBox.getItems().add(subject.getSubjectName());
        }
    }

    @FXML
    public void populateStudentComboBox() {
        studentChoiceBox.getItems().retainAll();
        List<Student> students = new ArrayList<>(Datasource.getInstance().listStudents());
        for (Student student : students) {
            studentChoiceBox.getItems().add(student.getStudentName());
        }
    }

    @FXML
    public void listGivenStudentGrades() {
        String student = studentChoiceBox.getValue();

        Task<ObservableList<StudentSubjectGrade>> task = new Task<>() {
            @Override
            protected ObservableList<StudentSubjectGrade> call() {
                return FXCollections.observableArrayList(Datasource.getInstance().listStudentGrades(student));
            }
        };

        allPossibleGradesTable.itemsProperty().bind(task.valueProperty());

        new Thread(task).start();

        studentChoiceBox.setValue(null);
        subjectChoiceBox.setValue(null);
    }

    @FXML
    public void listGivenStudentGivenSubjectGrades() {
        String student = studentChoiceBox.getValue();
        String subject = subjectChoiceBox.getValue();

        Task<ObservableList<StudentSubjectGrade>> task = new Task<>() {
            @Override
            protected ObservableList<StudentSubjectGrade> call() {
                return FXCollections.observableArrayList(Datasource.getInstance().listStudentGradesForGivenSubject(student, subject));
            }
        };

        allPossibleGradesTable.itemsProperty().bind(task.valueProperty());


        new Thread(task).start();

        studentChoiceBox.setValue(null);
        subjectChoiceBox.setValue(null);
    }

    @FXML
    public void listSubjectGrades() {
        String subject = subjectChoiceBox.getValue();

        Task<ObservableList<StudentSubjectGrade>> task = new Task<>() {
            @Override
            protected ObservableList<StudentSubjectGrade> call() {
                return FXCollections.observableArrayList(Datasource.getInstance().listSubjectGrades(subject));
            }
        };

        allPossibleGradesTable.itemsProperty().bind(task.valueProperty());

        new Thread(task).start();

        studentChoiceBox.setValue(null);
        subjectChoiceBox.setValue(null);
    }

    @FXML
    public void listForAverageStudentSubjectScore() {
        String subject = subjectChoiceBox.getValue();
        String student = studentChoiceBox.getValue();

        Task<ObservableList<StudentSubjectGrade>> task = new Task<>() {
            @Override
            protected ObservableList<StudentSubjectGrade> call() {
                return FXCollections.observableArrayList(Datasource.getInstance().queryForAverage(student, subject));
            }
        };

        allPossibleGradesTable.itemsProperty().bind(task.valueProperty());

        new Thread(task).start();

        studentChoiceBox.setValue(null);
        subjectChoiceBox.setValue(null);
    }

    @FXML
    public void clearAllFields() {
        studentChoiceBox.setValue(null);
        subjectChoiceBox.setValue(null);
    }

    public void closeApp() {
        Platform.exit();
    }

    static class QueryAllGradesTask extends Task<ObservableList<StudentSubjectGrade>> {

        @Override
        protected ObservableList<StudentSubjectGrade> call() {
            return FXCollections.observableArrayList(Datasource.getInstance().queryAllGrades());
        }
    }
}
