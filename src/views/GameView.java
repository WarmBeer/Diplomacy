package views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class GameView {

    private static String GAME_VIEW = "/resources/GameViewXML.fxml";
    private static String STYLESHEET_FILE = "/resources/style.css";
    private Parent content;
    private double vboxHeight = 0;
    private double labelHeight = 20;
    ArrayList<String> firebaseArrayMetBerichten = new ArrayList<String>();
    private String gebruikersnaam = "Thomas";

    @FXML
    private Pane TotaleChatbox;

    @FXML
    private TextArea berichtInput;

    @FXML
    private Button VerzendButton;

    @FXML
    private ListView berichtenLijst;

    public GameView(Stage stage){
        chatboxLaunch(stage);
        updateMenu(firebaseArrayMetBerichten);
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
    private void verzendString(ActionEvent event) {
        System.out.println("Verzendbutton Gedrukt");
        String nieuwBericht = (gebruikersnaam + ": " + berichtInput.getText());
        System.out.println("Bericht ontvangen: " + nieuwBericht);
        addMessageToFirebase(nieuwBericht);
        updateMenu(firebaseArrayMetBerichten);
    }

    private void updateMenu(ArrayList<String> firebaseArrayMetBerichten){
        berichtenLijst.getItems().clear();

        for(String bericht : firebaseArrayMetBerichten){
            berichtenLijst.getItems().add(berichtenLijst.getItems().size(), bericht);
            berichtenLijst.scrollTo(bericht);
            vboxHeight = vboxHeight + labelHeight;
        }
    }

    private Label stringToLabel(String text){
        Label label = new Label(text);
        label.setMinHeight(labelHeight);
        label.maxHeight(labelHeight);
        return label;
    }

    public void addMessageToFirebase(String message){
        firebaseArrayMetBerichten.add(message);
    }

}

