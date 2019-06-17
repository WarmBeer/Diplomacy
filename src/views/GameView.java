package views;

import controllers.GameController;
import domains.Province;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import observers.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.LogManager;

public class GameView implements OrderObserver, ChatObserver, Initializable, GameViewObserver {

    //Game setup variables
    private Parent content;
    private GameController gamecontroller;
    public final String GAME_VIEW = "/resources/views/GameView.fxml";
    public final String STYLESHEET_FILE = "/Resources/style.css";
    private static String SPEL_REGELS = "/views/Spelregels.fxml";

    //Ingame variables
    private Group root; //Kaart en UI render groep
    private Group troops; //Troepen render groep
    private Group points; //Provincie punt render groep
    private GameController gameController;
    private Stage stage;

    public GameView(Stage stage, GameController gameController){
        this.gameController = gameController;
        this.stage = stage;
    }

    public void init() {
        chatboxLaunch(stage);
        pressedStart();
    }

    private void pressedStart() {
        gameController.createChat();
        gameController.registerOrderObserver(this);
        gameController.registerChatObserver(this);
        gameController.registerGameObserver(this);
        gameController.requestLoadGame("11111111");
    }

    public void chatboxLaunch(Stage primaryStage) {
        try{
            root = new Group();
            troops = new Group();
            points = new Group();

            URL url = getClass().getResource(GAME_VIEW);
            FXMLLoader loader = new FXMLLoader(url);
            loader.setController(this);
            content = loader.load();

            primaryStage.setTitle("Diplomacy v0.2");
            Scene scene = new Scene( root, 1920, 1080 );
            root.getChildren().addAll(content, troops, points);
            primaryStage.setScene(scene);
            primaryStage.setFullScreen(true);
            primaryStage.show();

        }
        catch(IOException IOE){
            IOE.printStackTrace();
        }
        catch(Exception E){
            E.printStackTrace();
        }

        /*
        //Als host true is, maak dan een chatsavelocatie aan in firebase. Sorry Henk.
        if(host){
            gamecontroller.createChat();

        }

         */

        //Start updaten van nieuwe berichten
        gameController.startUpdatingChat();

    }

    //FXML Variables
    @FXML private Button returnInGameMenuKnop;
    @FXML private ToggleButton geluidsKnop;
    @FXML private AnchorPane gameOpties;
    @FXML private VBox box;
    @FXML public Pagination paginationrules;
    @FXML private Pane chatPane;
    @FXML private TextArea textInput;
    @FXML private Button sendButton;
    @FXML private ListView messagesList;
    @FXML private VBox MainMenu;
    @FXML private Button OpslaanKnop;
    @FXML private Button LaadKnop;
    @FXML private Button SpelregelsKnop;
    @FXML private Button OptiesKnop;
    @FXML private Button AfsluitenKnop;
    @FXML private Button in_GameMenuKnop;
    @FXML public Button button;
    @FXML public Button border;
    @FXML private Pane pOrderSettings;
    @FXML private ComboBox comboxAction;
    @FXML private ComboBox comboxProv1;
    @FXML private ComboBox comboxProv2;
    @FXML private ListView lvOrders;
    @FXML public TextField tfMessage; // Value injected by FXMLLoader
    @FXML public TextArea taUpdates; // Value injected by FXMLLoader
    @FXML private MediaPlayer mediaplayer;


    //FXML Methodes / Listeners
    @FXML private void afsluitenController(){
    }

    @FXML
    private void addChatMessage(ActionEvent event) {
        String nieuwBericht = (textInput.getText());
        gameController.addMessage(nieuwBericht);
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

    @FXML
    public void clickedAddOrder() {
        try {
            String action = comboxAction.getValue().toString();
            String prov1 = comboxProv1.getValue().toString();
            String prov2 = comboxProv2.getValue().toString();
            gameController.addOrderIsClicked(action, prov1, prov2);
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
    public void update(OrderObservable orderobservable) {
        updateOrderlist(orderobservable.getOrderList());
    }

    @Override
    public void update(ChatObservable chatobservable) {
        updateMessages(chatobservable.getArrayListWithMessages());
    }


    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        //Testdata om dropdownmenu te testen
        comboxAction.getItems().addAll("Action", "Move", "Support", "Hold");
        comboxProv1.getItems().addAll("Province1", "Province1a", "Province1b", "Province1c", "Province1d", "Province1e");
        comboxProv2.getItems().addAll("Province2", "Province2a", "Province2b", "Province2c", "Province2d", "Province2e");
        comboxAction.getSelectionModel().select(0);
        comboxProv1.getSelectionModel().select(0);
        comboxProv2.getSelectionModel().select(0);
        lvOrders.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            public void handle(final KeyEvent keyEvent )
            {
                if (lvOrders.getSelectionModel().getSelectedItem() != null )
                {
                    if (keyEvent.getCode().equals(KeyCode.DELETE ) )
                    {
                        lvOrders.getItems().remove(lvOrders.getSelectionModel().getSelectedItem());
                    }
                }
            }
        });
    }

    // Temporary variable to REPRESENT a clicked unit.
    private String selectedUnit = "currentPlayer";
    public void setOrderMenu() {
        if (!selectedUnit.equals("currentPlayer")) {
            pOrderSettings.setDisable(true);
        }
    }
    // Event fired when Action combobox Value is changed.
    public void checkAction() {
        //System.out.println("Checking value of comboxAction... " + comboxAction.getValue());
        switch (comboxAction.getValue().toString()) {
            case "Action":
                comboxProv1.getSelectionModel().select(0);
                comboxProv2.getSelectionModel().select(0);
                comboxProv1.setDisable(false);
                comboxProv2.setDisable(false);
                break;
            case "Move":
                comboxProv1.getSelectionModel().select(1);
                comboxProv1.setDisable(true);
                comboxProv2.getSelectionModel().select(1);
                comboxProv2.setDisable(false);
                break;
            case "Support":
                comboxProv1.setDisable(false);
                comboxProv2.setDisable(false);
                break;
            case "Hold":
                comboxProv1.getSelectionModel().select(1);
                comboxProv1.setDisable(true);
                comboxProv2.getSelectionModel().select(0);
                comboxProv2.setDisable(true);
                break;
        }
    }


    @Override
    public void update(GameViewObservable gameViewObservable) {
        troops.getChildren().removeAll(troops.getChildren());
        troops.getChildren().addAll(gameViewObservable.getTroopsGroup().getChildren());
        points.getChildren().removeAll(points.getChildren());
        points.getChildren().addAll(gameViewObservable.getPointsGroup().getChildren());
    }


    @FXML
    private void OpenMenu() {

        MainMenu.setVisible(!MainMenu.isVisible());
    }

    @FXML
    private void geluidAanUit() {
        if (geluidsKnop.isSelected() == true) {
            geluidsKnop.setText("Uit");
            geluidsKnop.setAlignment(Pos.CENTER);
            mediaplayer.pause();


        }
        else {
            if (geluidsKnop.isSelected() == false ) {
                geluidsKnop.setText("Aan");
                geluidsKnop.setAlignment(Pos.CENTER);
                mediaplayer.play();
            }
        }
    }

    @FXML
    private void inGameOpties() {
        gameOpties.setVisible(!gameOpties.isVisible());
        MainMenu.setVisible(!MainMenu.isVisible());
    }

    @FXML
    private void returnInGameMenu() {
        gameOpties.setVisible(!gameOpties.isVisible());
        MainMenu.setVisible(!MainMenu.isVisible());
    }

    //Dit sluit het spel af.
    @FXML
    private void afsluitenView() {
        System.exit(0);
    }

    //Hier worden de pagina's gecreërd met de plaatjes erin.
    @FXML
    public VBox createPage(int pageIndex) {
        box = new VBox();
        final ArrayList<String> imagesRules = new ArrayList<>();
        for (int i = 1; i <= 24; i++) {
            imagesRules.add("/resources/rules/rulebook-" + i + ".png");
        }
        box.setAlignment(Pos.CENTER);
        ImageView iv = new ImageView(String.valueOf(imagesRules.get(pageIndex)));
        box.getChildren().addAll(iv);
        return box;
    }

    //Hier wordt de FXML file ingeladen en voegt hij de methode createPage eraan toe.
    @FXML
    private void spelRegelsView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(SPEL_REGELS));
        fxmlLoader.setController(this);
        Parent contentRegels = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(contentRegels));
        stage.show();
        stage.setTitle("Spelregels");

        paginationrules.setPageFactory((Integer pageIndex) -> createPage(pageIndex));
    }

}

