package app.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static javafx.fxml.FXMLLoader.load;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage){

        System.out.print("test");

        try{
            Parent root = load(getClass().getClassLoader().getResource("app.fxml"));
            primaryStage.setTitle("yeet World");
            primaryStage.setScene(new Scene(root, 300, 275));
            primaryStage.show();
            System.out.print("test");

        }
        catch (Exception e){
            System.out.print(e);
        }

    }

    public static void main(String[] args) {
        launch(args);
    }


}
