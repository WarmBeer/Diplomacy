package app.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("views/app.fxml"));
        primaryStage.setTitle("yeet World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        int i = 1;

        while(i == 1){
            System.out.println("Rekt son");
        }

    }


    public static void main(String[] args) {
        launch(args);
    }
}
