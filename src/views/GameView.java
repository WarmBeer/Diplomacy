package views;

import controllers.GameController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import observers.GameObservable;
import observers.GameObserver;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.LogManager;

public class GameView implements GameObserver, Initializable {

    private Parent content;
    private GameController gamecontroller;
    public final String GAME_VIEW = "/resources/views/GameView.fxml";
    public final String STYLESHEET_FILE = "/Resources/style.css";

    public GameView(Stage stage){
        chatboxLaunch(stage);
        gamecontroller = new GameController();
        gamecontroller.registerViewObserver(this);
    }


    public void chatboxLaunch(Stage primaryStage) {
        try{
            URL url = getClass().getResource(GAME_VIEW);
            FXMLLoader loader = new FXMLLoader(url);
            loader.setController(this);
            content = loader.load();

            primaryStage.setTitle("Diplomacy v0.2");
            primaryStage.setScene(new Scene(content));
            primaryStage.setFullScreen(true);
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
    private void afsluitenController(){
    }

    @FXML
    public Button button;

    @FXML
    public Button border;

    @FXML
    public ComboBox comboxAction;

    @FXML
    public ComboBox comboxProv1;

    @FXML
    public ComboBox comboxProv2;

    @FXML
    public ListView lvOrders;

    @FXML
    public TextField tfMessage; // Value injected by FXMLLoader

    @FXML
    public TextArea taUpdates; // Value injected by FXMLLoader

    @FXML
    public void clickedAddOrder() {
        try {
            String action = comboxAction.getValue().toString();
            String prov1 = comboxProv1.getValue().toString();
            String prov2 = comboxProv2.getValue().toString();
            gamecontroller.addOrderIsClicked(action, prov1, prov2);
        }
        catch (Exception e) {
            System.out.println("In GameView is iets fout gegaan tijdens het toevoegen van een order...");
            e.printStackTrace();
        }
    }

    public void updateOrderlist(ArrayList<String> orderList){
        lvOrders.getItems().clear();
        for(String order : orderList){
            lvOrders.getItems().add(order);
            LogManager.getLogManager().reset();
        }
    }

    @Override
    public void update(GameObservable gameobservable) {
        updateOrderlist(gameobservable.getOrderList());
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        comboxAction.getItems().addAll("Move", "Support", "Embark", "Hold");
        comboxProv1.getItems().addAll("Province1a", "Province1b", "Province1c", "Province1d", "Province1e");
        comboxProv2.getItems().addAll("Province2a", "Province2b", "Province2c", "Province2d", "Province2e");
    }


}

