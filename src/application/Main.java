package application;

import controllers.MainController;
import javafx.application.Application;
import javafx.stage.Stage;
import utilities.KeyHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Main extends Application {

    private static String KEY = "";
    private MainController mainController;
    public enum unitType {ARMY, FLEET}


    public static void main(String[] args) {
            launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.mainController = new MainController(primaryStage);
        setup();
        mainController.showMainMenu();
        mainController.TurnMusicOn(true);

    }

    public static String getKEY() {
        return  KEY;
    }

    public static void setKEY(String key) {
        KEY = key;
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
            mainController.createLoginName();
        }
    }

    public static void print(Object o) {
        System.out.println(o);
    }
}