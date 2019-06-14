package application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import domains.GameJSON;
import javafx.application.Application;
import javafx.stage.Stage;
import utilities.KeyHandler;
import views.GameView;

import java.io.*;
import java.util.Scanner;


public class Main extends Application {

    private static final String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static String KEY = "";
    private GameView game;
    public static enum unitType {ARMY, FLEET};


    public static void main(String[] args) {
            launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        //Maak variabele aan voor de chatbox (die normaal al beschikbaar zijn in het spel)
        String userName = "Thomas";
        String gameID = "Game4";
        boolean host = true;
        game = new GameView(primaryStage,userName,gameID,host);

//            this.gameModel = new GameModel();
//            this.superModel = new SuperModel();
//
//            Parent panel;
//            panel = FXMLLoader.load(getClass().getResource(GAME_VIEW));
//            Scene scene = new Scene(panel);
//            Stage stage = new Stage();
//            stage.setScene(scene);
//            stage.setTitle("Diplomacy v0.1");
//            stage.setMaximized(true);
//            stage.show();
//
//            setup();
//            gameModel.show(stage);
//
//            loadGame();
    }

    public void loadGame() {


    }

    public void setup() {

        String jarLocation = KeyHandler.getJarLocation();

        print( jarLocation );

        File file = new File(jarLocation + File.separator + "KEY.txt");

        if (file.exists()) {
            try {
                KEY = new Scanner(file).useDelimiter("\\Z").next();
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

    public static void print(Object o) {
        System.out.println(o);
    }
}