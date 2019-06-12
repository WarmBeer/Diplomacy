package application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import domains.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
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

    private static final String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        public static enum unitType {
                ARMY,
                FLEET
        };

        public static String GAME_VIEW = "/Resources/views/GameView.fxml";
        public static String MAIN_MENU = "/Resources/views/MainMenu.fxml";
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
                panel = FXMLLoader.load(getClass().getResource(MAIN_MENU));
                Scene scene = new Scene(panel);
                stage = new Stage();
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

            URL jarLocationUrl = Main.class.getProtectionDomain().getCodeSource().getLocation();
            String jarLocation = new File(jarLocationUrl.toString()).getParent().substring(6);

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
                createKeyFile(jarLocation);
            }
        }

        public void createKeyFile(String location) {
            String key = generateKey(16);

            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(location + File.separator + "KEY.txt"));
                writer.write(key);

                writer.close();
            } catch (IOException io) {
                io.printStackTrace();
            }
            print("Saved key: " + key);
        }

        public static String generateKey(int count) {
            StringBuilder builder = new StringBuilder();
            while (count-- != 0) {
                int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
                builder.append(ALPHA_NUMERIC_STRING.charAt(character));
            }
            return builder.toString();
        }
}