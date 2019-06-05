package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneBuilder;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

        private static String GAME_VIEW = "/resources/Diplomacy.fxml";
        private static String STYLESHEET_FILE = "/resources/style.css";

        public static void main(String[] args) {
                launch(args);
        }

        @Override
        public void start(Stage primaryStage) throws Exception {
                String fxmlResource = GAME_VIEW;
                Parent panel;
                panel = FXMLLoader.load(getClass().getResource(fxmlResource));
                Scene scene = new Scene(panel);
                Stage stage = new Stage();
                stage.setHeight(1040);
                stage.setScene(scene);
                stage.setResizable(false);
                stage.setAlwaysOnTop(true);
                stage.show();
        }
}