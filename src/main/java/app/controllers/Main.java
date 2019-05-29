package app.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static javafx.fxml.FXMLLoader.load;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        System.out.print("test");

            Parent root = FXMLLoader.load(getClass().getResource("../views/app.fxml"));
            primaryStage.setTitle("yeet World");
            primaryStage.setScene(new Scene(root, 300, 275));
            primaryStage.show();
            System.out.print("test");
    }

    public static void main(String[] args) {
        launch(args);
    }


}
