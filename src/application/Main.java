package application;

import javafx.application.Application;
import javafx.stage.Stage;
import views.GameView;

public class Main extends Application {

        private GameView game;

        public static void main(String[] args) {
                launch(args);
        }

        @Override
        public void start(Stage stage) {
                String userName = "Thomas";
                String gameID = "Game4";
                boolean host = true;
                game = new GameView(stage,userName,gameID,host);
        }

}