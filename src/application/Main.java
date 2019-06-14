package application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import domains.*;
import controllers.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utilities.KeyHandler;
import views.MainMenu;
import javafx.collections.ObservableList;

import javax.print.DocFlavor;
import java.io.IOException;
import java.util.ResourceBundle;
import models.GameModel;
import models.SuperModel;

import java.io.*;
import java.net.URL;
import java.util.Scanner;

/**
 * Bevat alle code om het programma te kunnen starten.
 * Dit is een online spel waarvoor internetverbinding vereist is.
 * Het kan met maximaal 7 mensen gespeeld worden.
 *
 * @author Edwin
 */
public class Main extends Application {

        public static enum unitType {
                ARMY,
                FLEET
        };

        public static String GAME_VIEW = "/resources/views/GameView.fxml";
        public static String MAIN_MENU = "/resources/views/MainMenu.fxml";
        public static String STYLESHEET_FILE = "/Resources/style.css";
        private Stage stage;
        private String KEY;

        private GameModel gameModel;
        private SuperModel superModel;

        public static void main(String[] args) {
                launch(args);
        }

        /**
         * Hello World,
         * by calling this, the application will be started.
         */

        public static void print(Object o) {
            System.out.println(o);
        }

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
                stage.show();

                setup();
                gameModel.show(stage);

                loadGame();
        }

        public void loadGame() {

                Reader reader = new BufferedReader(new InputStreamReader(
                        this.getClass().getResourceAsStream("/" + "Diplomacy.json")));
                Gson gson = new GsonBuilder().create();
                GameJSON gameJSON = gson.fromJson(reader, GameJSON.class);

                gameModel.initGame(gameJSON);
        }

        public void setup() {

            String jarLocation = KeyHandler.getJarLocation();

            print( jarLocation );

            File file = new File(jarLocation + File.separator + "KEY.txt");

            if (file.exists()) {
                try {
                    this.KEY = new Scanner(file).useDelimiter("\\Z").next();
                    print("Key found!: " + this.KEY);
                    //retrieveSaves
                } catch (FileNotFoundException fnf) {
                    fnf.printStackTrace();
                }
            } else {
                print("Key not found, creating one for you!");
                KeyHandler.createKeyFile(jarLocation);
            }
        }
}