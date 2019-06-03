package application;

import javafx.application.Application;
import javafx.stage.Stage;
import views.GameView;

public class Main extends Application {

        GameView game;

        public static void main(String[] args) {
                launch(args);
        }

        @Override
        public void start(Stage stage) throws Exception {
                game = new GameView(stage);
        }

}