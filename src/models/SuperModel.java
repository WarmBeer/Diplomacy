package models;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SuperModel implements Model {

    public final String MAIN_MENU = "/resources/views/MainMenu.fxml"; //DIT MOET WEG UITEINDELIJK

    @FXML
    public void show(Stage stage) throws Exception{
        Parent pane = FXMLLoader.load(
                getClass().getResource(MAIN_MENU));

        Scene scene = new Scene( pane );
        stage.setScene(scene);
    }

}
