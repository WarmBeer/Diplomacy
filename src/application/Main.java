package application;

import controllers.MainController;
import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import utilities.KeyHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Main extends Application {

    private static String KEY = "";
    private MainController mainController;
    public enum unitType {ARMY, FLEET}
    public MediaPlayer mediaplayer;


    public static void main(String[] args) {
            launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.mainController = new MainController(primaryStage);
        setup();
        mainController.showMainMenu();
//        mainController.startMusic();

    }

    public static String getKEY() {
        return  KEY;
    }

    public static void setKEY(String key) {
        KEY = key;
    }


    /**
     * Regeelt de inlog aan de hand van een sleutel.
     * Slaat een sleutel op in KEY.txt indien niet aanwezig.
     * @author Mick van Dijke
     * @version June 2019
     */

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
            mainController.createLoginName();
        }
    }

    public static void print(Object o) {
        System.out.println(o);
    }
}