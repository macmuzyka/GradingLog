package controllers;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Datasource;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("startWindow.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Grading Log");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        super.init();
        if (!Datasource.getInstance().open()) {
            System.out.println("Error connecting to the database!");
            Platform.exit();
        } else {
            System.out.println("Successfully connected to the database!");
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Datasource.getInstance().close();
        System.out.println("Closed connection to the database!");
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
