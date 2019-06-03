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
    private Pane TotaleChatbox;

    @FXML
    private TextArea berichtInput;

    @FXML
    private Button VerzendButton;

    @FXML
    private VBox VerticaleBox;

    @FXML
    private Scrollbar Scrollbarretje;

    @FXML
    private void verzendString(ActionEvent event) {
        System.out.println("Verzendbutton Gedrukt");
        //firebaseservice.addMessage(); --> Send messaage to firebase, than update the chat and get all messages from firebase

    }
}

