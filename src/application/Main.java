package application;

import javafx.application.Application;
import javafx.stage.Stage;
import views.GameView;

/**
 * Main Class that launches the application "Diplomacy".
 */

public class Main extends Application {

        private GameView game;

        public static void main(String[] args) {
                launch(args);
        }

        /**
         * Starts the application!
         * Inizialize the variables, and the gameView.
         * @param stage PrimayStage for javaFX.
         * @author Thomas Zijl
         */
        @Override
        public void start(Stage stage) {
                //Maak variabele aan voor de chatbox (die normaal al beschikbaar zijn in het spel)
                String userName = "Thomas";
                String gameID = "Game4";
                boolean host = true;

                game = new GameView(stage,userName,gameID,host);
        }

}