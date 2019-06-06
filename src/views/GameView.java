package views;

import com.google.cloud.firestore.Firestore;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import services.FirebaseService;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.logging.LogManager;


public class GameView {

    //Variables for javaFX
    private static String GAME_VIEW = "/resources/GameViewXML.fxml";
    private Parent content;

    //Variables for firebase usage
    Firestore firebaseConnection;
    private String userName;
    private String gameName;
    private FirebaseService fb = new FirebaseService();
    private ArrayList<String> messageArraylist = new ArrayList<String>();

    @FXML
    private Pane TotaleChatbox;

    @FXML
    private TextArea berichtInput;

    @FXML
    private Button VerzendButton;

    @FXML
    private ListView berichtenLijst;


    public GameView(Stage stage, String userName, String gameName){
        this.userName = userName;
        this.gameName = gameName;
        launchChatbox(stage);
    }


    private void launchChatbox(Stage stage){
        try{
            makeLayout(stage);
            firebaseConnection =  fb.makeFirebaseConnection(userName,gameName);
            fb.makeSaveLocationChat(firebaseConnection);
            updateMessages();
        }
        catch (IOException IOE){
            IOE.printStackTrace();
        }
    }


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
        catch(Exception E){
            E.printStackTrace();
        }
    }


    @FXML
    private void verzendString(ActionEvent event) {
        String nieuwBericht = (berichtInput.getText());
        addMessageToFirebase(nieuwBericht);
        updateMessages();
        berichtInput.clear();
    }


    private void updateMessages(){
        berichtenLijst.getItems().clear();
        ArrayList<String> updatedMessageArraylist = getUpdatedArraylistFB();

        for(String bericht : updatedMessageArraylist){
            berichtenLijst.getItems().add(berichtenLijst.getItems().size(), bericht);
            berichtenLijst.scrollTo(bericht);
            LogManager.getLogManager().reset();
        }
    }


    private ArrayList<String> getUpdatedArraylistFB(){
        try{
            ArrayList<String> updatedMessageArraylist = fb.getData(firebaseConnection);
            return updatedMessageArraylist;
        }
        catch (ExecutionException EE){
            EE.printStackTrace();
            return null;
        }
        catch(InterruptedException IE){
            IE.printStackTrace();
            return null;
        }

    }


    private void addMessageToFirebase(String message){
        fb.addMessageToChat(firebaseConnection,message);
    }

}

