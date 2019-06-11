package application;

import controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import views.MainMenu;
import javafx.collections.ObservableList;

import javax.print.DocFlavor;
import java.io.IOException;
import java.util.ResourceBundle;
import models.GameModel;
import models.Model;
import models.SuperModel;

/**
 * Bevat alle code om het programma te kunnen starten.
 * Dit is een online spel waarvoor internetverbinding vereist is.
 * Het kan met maximaal 7 mensen gespeeld worden.
 *
 * @author Edwin
 */
public class Main extends Application {

        public static String GAME_VIEW = "/resources/views/GameView.fxml";
        public static String MAIN_MENU = "/resources/views/MainMenu.fxml";
        public static String STYLESHEET_FILE = "/Resources/style.css";
        private Stage stage;

        private Model gameModel;
        private Model superModel;

        public static void main(String[] args) {
                launch(args);
        }

        /**
         * Hello World,
         * by calling this, the application will be started.
         */

        @Override
        public void start(Stage primaryStage) throws Exception {

                this.gameModel = new GameModel();
                this.superModel = new SuperModel();

                Parent panel;
                panel = FXMLLoader.load(getClass().getResource(GAME_VIEW));
                Scene scene = new Scene(panel);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Diplomacy v0.1");
                stage.setMaximized(true);
                //stage.setResizable(false);
                stage.show();

//                gameModel.show(stage);
        }

        private Circle MakeCircles() {
                Circle circle1 = new Circle(100.0f, 100.0f, 50.f);
                circle1.setFill(Color.BLUE);
                circle1.setStroke(Color.RED);
                circle1.setStrokeWidth(3);
                return circle1;
        }
}