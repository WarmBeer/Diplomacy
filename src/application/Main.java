package application;

import controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneBuilder;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import views.MainMenu;

import java.io.IOException;

public class Main extends Application {

        private static String GAME_VIEW = "/resources/GameView.fxml";
        private static String STYLESHEET_FILE = "/resources/style.css";

        public static void main(String[] args) {
                launch(args);
        }

    /**
     * @param stage
     * @throws Exception
     */
    @Override
        public void start(Stage stage) throws Exception {
                initGui(stage);
        }

    /**
     *
     * @param stage
     * @throws IOException
     */
    private void initGui(Stage stage) throws IOException {
            Parent root = FXMLLoader.load(getClass().getResource(GAME_VIEW));
            Scene scene = SceneBuilder.create().root(root).width(1280).height(720)
                    .fill(Color.GRAY).build();
            scene.getStylesheets().add(STYLESHEET_FILE);
            stage.setScene(scene);
            stage.setTitle("Diplomacy");
            //stage.getIcons().add(ICON);
            stage.show();

            MainController mainController = new MainController();

            MainMenu mainMenu = new MainMenu(stage, mainController);
            mainMenu.show();
        }
}