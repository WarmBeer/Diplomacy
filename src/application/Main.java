package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneBuilder;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

        private static String GAME_VIEW = "/resources/GameView.fxml";
        private static String STYLESHEET_FILE = "/resources/style.css";

        public static void main(String[] args) {
                launch(args);
        }

        @Override
        public void start(Stage stage) throws Exception {
                initGui(stage);
        }

        private void initGui(Stage stage) throws IOException {
                Parent root = FXMLLoader.load(getClass().getResource(GAME_VIEW));
                Scene scene = SceneBuilder.create().root(root).width(500).height(530)
                        .fill(Color.GRAY).build();
                scene.getStylesheets().add(STYLESHEET_FILE);
                stage.setScene(scene);
                stage.setTitle("Diplomacy");
                //stage.getIcons().add(ICON);
                stage.setFullScreen(true);
                stage.show();
        }
}