package views;

import controllers.GameController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import observers.GameObservable;
import observers.GameObserver;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.LogManager;


public class GameView implements GameObserver {

    //MVC Controllertje
    private GameController gamecontroller;

    //Variables for javaFX
    private static String GAME_VIEW = "/resources/ChatBoxFXML.fxml";
    private Parent content;

    //Iput variables for models
    private String userName;
    private String gameID;
    private boolean host;

    @FXML
    private Pane chatPane;

    @FXML
    private TextArea textInput;

    @FXML
    private Button sendButton;

    @FXML
    private ListView messagesList;

    //Geef de Username, GameID en boolean Host mee, normaal zouden deze al bekend zijn in game
    public GameView(Stage stage, String userName, String gameID, boolean host){
        gamecontroller = gamecontroller.getInstance(gameID);
        gamecontroller.registerObserver(this);
        this.host = host;
        this.userName = userName;
        this.gameID = gameID;
        launchChatbox(stage);
    }


    //Launch chatbox
    private void launchChatbox(Stage stage){
        makeLayout(stage);

        //Sorry henk
        if(host){
            gamecontroller.createChat();

        }

        gamecontroller.startUpdatingChat();

    }


    //Make layout
    private void makeLayout(Stage primaryStage) {
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
    }


    @FXML
    private void addChatMessage(ActionEvent event) {
        String nieuwBericht = (textInput.getText());
        gamecontroller.addMessage(nieuwBericht, this.userName);
        textInput.clear();
    }


    private void updateMessages(ArrayList<String> messageArraylist){
        messagesList.getItems().clear();

        for(String bericht : messageArraylist){
            messagesList.getItems().add(messagesList.getItems().size(), bericht);
            messagesList.scrollTo(bericht);
            LogManager.getLogManager().reset();
        }
    }


    @Override
    public void update(GameObservable chatbox) {
        updateMessages(chatbox.getArrayListWithMessages());
    }
}
