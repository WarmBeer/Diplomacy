package application;

import javafx.application.Application;
import javafx.stage.Stage;
import views.GameView;


public class Main extends Application {

        private static final String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        private GameView game;
        public static enum unitType {ARMY, FLEET};


        public static void main(String[] args) {
                launch(args);
        }


        @Override
        public void start(Stage primaryStage) throws Exception {
            game = new GameView(primaryStage);
        }


        public static void print(Object o) {
            System.out.println(o);
        }

}