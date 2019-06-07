package views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class GameView {

    private static String GAME_VIEW = "/resources/GameViewXML.fxml";
    private static String STYLESHEET_FILE = "/resources/style.css";
    private Parent content;

    public GameView(Stage stage){
        chatboxLaunch(stage);

    }

    public void chatboxLaunch(Stage primaryStage) {
        String sceneFile = GAME_VIEW;

        try{
            URL url = getClass().getResource(sceneFile);
            FXMLLoader loader = new FXMLLoader(url);
            loader.setController(this);
            content = loader.load();

            primaryStage.setTitle("Welkom!");
            primaryStage.setScene(new Scene(content, 400, 500));
            primaryStage.show();
        }
        catch(IOException IOE){
            IOE.printStackTrace();
        }
        catch(Exception E){
            E.printStackTrace();
        }

    }

    @FXML
    private VBox MainMenu;

    @FXML
    private Button OpslaanKnop;

    @FXML
    private Button LaadKnop;

    @FXML
    private Button SpelregelsKnop;

    @FXML
    private Button OptiesKnop;

    @FXML
    private Button AfsluitenKnop;

    @FXML
    private Button in_GameMenuKnop;

    @FXML
    private void OpenMenu() {
//        if (!MainMenu.isVisible()) {
//            MainMenu.setVisible(true);
//        }
//        else if (MainMenu.isVisible()) {
//            MainMenu.setVisible(false);
//        }

        MainMenu.setVisible(!MainMenu.isVisible());

//        MainMenu.setVisible(true);

//        boolean MainMenuFalse = false;
//        if (MainMenu.equals(MainMenuFalse) == MainMenuFalse) {
//            MainMenu.setVisible(true);
//        }
//        else if (MainMenu.equals(MainMenuFalse) == true) {
//            MainMenu.setVisible(false);
//        }
    }
}

