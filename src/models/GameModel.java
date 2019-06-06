package models;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static application.Main.GAME_VIEW;

public class GameModel implements Model {

    @FXML
    public void show(Stage stage) throws Exception{
        Parent pane = FXMLLoader.load(
                getClass().getResource(GAME_VIEW));

        Scene scene = new Scene( pane );
        stage.setScene(scene);
    }

}
