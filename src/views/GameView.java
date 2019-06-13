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

/**
 * Class that shows the ingame GUI and recieves the input from the user.
 */


public class GameView implements GameObserver {

    private GameController gamecontroller;
    private static String GAME_VIEW = "/resources/views/GameView.fxml";
    private Parent content;
    private String userName;
    private String gameID;
    private boolean host;

    @FXML private Pane chatPane;
    @FXML private TextArea textInput;
    @FXML private Button sendButton;
    @FXML private ListView messagesList;


    /**
     * Creates Gameview.
     * Create the gamecontroller instance and delclares the variables.
     * @param stage Primary Stage given by the Main.
     * @param userName Name from player as String.
     * @param gameID Game ID as string, important for firebase.
     * @param host Is this player the host? Important for firebase.
     * @author Thomas Zijl
     *
     */
    public GameView(Stage stage, String userName, String gameID, boolean host){
        gamecontroller = gamecontroller.getInstance(gameID);
        gamecontroller.registerObserver(this);

        this.host = host;
        this.userName = userName;
        this.gameID = gameID;

        launchChatbox(stage);
    }


    private void launchChatbox(Stage primaryStage){
        //Make FXML layout
        try{
            URL url = getClass().getResource(GAME_VIEW);
            FXMLLoader loader = new FXMLLoader(url);
            loader.setController(this);
            content = loader.load();
            primaryStage.setTitle("Diplomacy");
            primaryStage.setScene(new Scene(content));
            primaryStage.show();
        }
        catch(IOException IOE){
            IOE.printStackTrace();
        }

        //Als host true is, maak dan een chatsavelocatie aan in firebase
        if(host){
            gamecontroller.createChat();

        }

        //Start updaten van nieuwe berichten
        gamecontroller.startUpdatingChat();
    }

    @FXML
    public void afsluitenController(){

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


    /**
     * Gets all String from arraylist and shows them as individual messages.
     * @param chatbox chatbox object from the class Chatbox, given as GameObserable (because it implents this interface).
     * @author Thomas Zijl
     */
    @Override
    public void update(GameObservable chatbox) {
        updateMessages(chatbox.getArrayListWithMessages());
    }
}
