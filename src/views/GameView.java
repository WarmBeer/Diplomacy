package views;

import com.google.cloud.firestore.Firestore;
import javafx.application.Platform;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
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
    private boolean host = false;
    private int chatUpdateInSeconden = 2;

    @FXML
    private Pane chatPane;

    @FXML
    private TextArea textInput;

    @FXML
    private Button sendButton;

    @FXML
    private ListView messagesList;


    public GameView(Stage stage, String userName, String gameName, boolean host){
        this.host = host;
        this.userName = userName;
        this.gameName = gameName;
        launchChatbox(stage);
    }


    private void launchChatbox(Stage stage){
        try{
            makeLayout(stage);
            firebaseConnection =  fb.makeFirebaseConnection(gameName);

            //Sorry henk
            if(host == true){
                fb.makeSaveLocationChat(firebaseConnection);
            }

            updateMessages();
            keepUpdatingChat();
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
    private void sendMessage(ActionEvent event) {
        String nieuwBericht = (textInput.getText());
        addMessageToFirebase(nieuwBericht);
        updateMessages();
        textInput.clear();
    }


    private void updateMessages(){
        messagesList.getItems().clear();
        ArrayList<String> updatedMessageArraylist = getUpdatedArraylistFB();

        for(String bericht : updatedMessageArraylist){
            messagesList.getItems().add(messagesList.getItems().size(), bericht);
            messagesList.scrollTo(bericht);
            LogManager.getLogManager().reset();
        }

        System.out.println("Chat updated!");
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
        String newMessage = makeNewMessage(message);
        fb.addMessageToChat(firebaseConnection,newMessage);
    }


    private String makeNewMessage(String message){
        String systemNameAndTimestamp = ("(" + (new SimpleDateFormat("HH:mm:ss").format(new Date())) + ") " + userName);
        String newMessage = systemNameAndTimestamp + ": " + message;
        return newMessage;
    }


    private void keepUpdatingChat(){
        Runnable helloRunnable = new Runnable() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        updateMessages();
                    }
                });
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(helloRunnable, 0, chatUpdateInSeconden, TimeUnit.SECONDS);
    }


}

