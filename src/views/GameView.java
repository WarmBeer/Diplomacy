package views;

import application.Main;
import controllers.GameController;
import domains.Player;
import domains.Province;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import observers.*;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.LogManager;

/**
 * Class that handles input and output of the FXML file (view) of the game
 */

public class GameView implements OrderObserver, ChatObserver, Initializable, GameViewObserver {

    //Game setup variables
    private Parent content;
    public final String GAME_VIEW = "/views/GameView.fxml";
    public final String STYLESHEET_FILE = "GameView.css";
    private static String SPEL_REGELS = "/views/Spelregels.fxml";



    //Ingame variables
    private Group root; //Kaart en UI render groep
    private Group troops; //Troepen render groep
    private Group points; //Provincie punt render groep
    private GameController gameController;
    private Stage stage;
    private Province selectedProvince;

    @FXML
    private Button endTurn;

    // COLORS (country/player)
    private Color colAustria = Color.SANDYBROWN;
    private Color colEngland = Color.STEELBLUE;
    private Color colFrance = Color.SKYBLUE;
    private Color colGermany = Color.SLATEGRAY;
    private Color colItaly = Color.OLIVEDRAB;
    private Color colRussia = Color.DARKGRAY;
    private Color colTurkey = Color.DARKGOLDENROD;

    public GameView(Stage stage, GameController gameController){
        this.gameController = gameController;
        this.stage = stage;
    }

    public void init() {
        chatboxLaunch(stage);
        pressedStart();
        updatePlayerInformation();
        configureButtons();
    }

    private void pressedStart() {
        gameController.registerOrderObserver(this);
        gameController.registerChatObserver(this);
        gameController.registerGameObserver(this);
    }

    private void configureButtons() {
        if (gameController.getGamemodel().getActiveGame().getHost().equals(Main.getKEY())) {
            endTurn.setVisible(true);
        } else {
            endTurn.setVisible(false);
        }
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
            scene.getStylesheets().add (GameView.class.getResource(STYLESHEET_FILE).toExternalForm());
            root.getChildren().addAll(content, troops, points);
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.show();
        }
        catch(IOException IOE){
            IOE.printStackTrace();
        }
        catch(Exception E){
            E.printStackTrace();
        }
    }

    //FXML Variables
    @FXML private Button returnSpelRegelsButton;
    @FXML private Button returnInGameMenuKnop;
    @FXML private ToggleButton geluidsKnop;
    @FXML private AnchorPane gameOpties;
    @FXML private VBox box;
    @FXML public Pagination paginationrules;
    @FXML private TabPane tabPane;
    @FXML private Tab tabChat;
    @FXML private Pane pChatMessage;
    @FXML private TextField textInput;
    @FXML private Button sendButton;
    @FXML private ListView messagesList;
    @FXML private AnchorPane MainMenu;
    @FXML private Button OpslaanKnop;
    @FXML private Button LaadKnop;
    @FXML private Button SpelregelsKnop;
    @FXML private Button OptiesKnop;
    @FXML private Button AfsluitenKnop;
    @FXML private Button inGameMenuButton;
    @FXML public Button button;
    @FXML public Button border;
    @FXML private Pane pOrderSettings;
    @FXML private ComboBox comboxAction;
    @FXML private ComboBox comboxProv1;
    @FXML private ComboBox comboxPrivateChat;
    @FXML private ListView lvOrders;
    @FXML public TextField tfMessage; // Value injected by FXMLLoader
    @FXML public TextArea taUpdates; // Value injected by FXMLLoader
    @FXML private MediaPlayer mediaplayer;
    @FXML private ListView playersList;
    @FXML public Label timer;
    @FXML private Button sendOrderButton;
    @FXML private Button bOrder;

    //FXML Methodes / Listeners
    @FXML private void afsluitenController(){
    }

    @FXML
    private void addChatMessage(ActionEvent event) {
        sendChatMessage();
    }

    private void sendChatMessage() {
        if (textInput.getText().length() > 0) {
            String newMessage = (textInput.getText());
            String toPrivate = comboxPrivateChat.getValue().toString();
            gameController.addMessage(application.Main.getKEY(), toPrivate, newMessage);

            textInput.clear();
        }
    }

    public void firstMessage(){
        gameController.sendFirstMessage();
    }

    /**
     * Gets messages and update the listview with messages
     *@version June 2019
     */
    private void updateMessages(ArrayList<String> messageArraylist) {

        messagesList.getItems().clear();
        for (String bericht : messageArraylist) {
            Color privateColor = Color.BLACK;
            if (bericht.contains("_")) {
                String[] berichtSplit = bericht.split("_");
                String fromPlayerUID = berichtSplit[0];

                Player fromPlayer = null;
                List<Player> players = gameController.getGamemodel().getActiveGame().getPlayers();
                for(Player player : players) {
                    if(player.getUID().equals(fromPlayerUID)) {
                        fromPlayer =  player;
                    }
                }

                String toPlayer = berichtSplit[1];

                Player thisPlayer = gameController.getGamemodel().getThisPlayer();
                if(thisPlayer == null){
                    System.out.println("uhm de player is null? Kan het bericht nu niet plaatsten ");
                    return;
                }

                if(thisPlayer.getUID().equals(fromPlayerUID) && !toPlayer.equals("ALL")) {
                    toPlayer = thisPlayer.getCountry().toString();
                }

                bericht = berichtSplit[2];
                if(fromPlayer != null)
                    bericht = bericht.replace(fromPlayerUID, fromPlayer.getCountry().name());

                switch (toPlayer) {
                    case "ALL":
                        break;
                    case "AUSTRALIA":
                        privateColor = colAustria;
                        break;
                    case "ENGLAND":
                        privateColor = colEngland;
                        break;
                    case "FRANCE":
                        privateColor = colFrance;
                        break;
                    case "GERMANY":
                        privateColor = colGermany;
                        break;
                    case "ITALY":
                        privateColor = colItaly;
                        break;
                    case "RUSSIA":
                        privateColor = colRussia;
                        break;
                    case "TURKEY":
                        privateColor = colTurkey;
                        break;
                }

                if (toPlayer.equals("ALL") ||
                        toPlayer.equals(thisPlayer.getCountry().name())) {
                    Label berichtLabel = new Label(bericht);
                    berichtLabel.setStyle("-fx-text-inner-background: green; -fx-text-fill: rgb(" + toRGB(privateColor) + ");");
                    messagesList.getItems().add(berichtLabel);
                    messagesList.scrollTo(berichtLabel);
                }

                LogManager.getLogManager().reset();
            }
        }
    }

    public String toRGB(Color color) {
        int r = (int) Math.round(color.getRed() * 255);
        int g = (int) Math.round(color.getGreen() * 255);
        int b = (int) Math.round(color.getBlue() * 255);
        return r + "," + g + "," + b;
    }

    @FXML
    public void clickedSendOrder() {
        sendOrderButton.setDisable(true);
        comboxAction.setDisable(true);
        comboxProv1.setDisable(true);
        bOrder.setDisable(true);
        lvOrders.setDisable(true);
        gameController.clickedSendOrder(lvOrders);
    }

    @FXML
    public void clickedEndTurn() {
        gameController.clickedEndTurn();
    }

    @FXML
    public void clickedAddOrder() {
        if(selectedProvince == null)
            return;
        try {
            int actionIndex = comboxAction.getSelectionModel().getSelectedIndex();

            if (actionIndex != 0) {
                String action = comboxAction.getValue().toString();
                String prov1 = comboxProv1.getValue().toString();
                String order= null;
                if(actionIndex == 3) {
                    order = action + "_" + selectedProvince.getName();
                } else {
                    order = action + "_" + selectedProvince.getName() + "_" + prov1;
                }
                gameController.checkOrder(lvOrders, order);
                //checkDuplicateUnitOrder(order);
                lvOrders.getItems().add(order);
            }
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
        // Vullen van Order dropdowns.
        comboxAction.getItems().addAll("Hold", "Support", "Move");
        comboxProv1.getItems().addAll("Select Province");
        comboxPrivateChat.getItems().addAll("All");

        // Set all dropdowns to first item.
        comboxAction.getSelectionModel().select(0);
        comboxProv1.getSelectionModel().select(0);
        comboxProv1.setDisable(true);
        comboxPrivateChat.getSelectionModel().select(0);
        // Enable DEL key to delete selected orders from list.
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
        // Enable ENTER key to send text easily, same as Send button click.
        textInput.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            public void handle(final KeyEvent keyEvent )
            {
                if (keyEvent.getCode().equals(KeyCode.ENTER) )
                {
                    sendChatMessage();
                }
            }
        });

        tabChat.setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event t) {
                if (tabChat.isSelected()) {
                    pChatMessage.setDisable(false);
                }
                else { pChatMessage.setDisable(true); }
            }
        });

    }

    // Temporary variable to REPRESENT a clicked unit.
    private String selectedUnit = "Player2";
    public void setOrderMenu() {
        if (!selectedUnit.equals("currentPlayer")) {
            pOrderSettings.setDisable(true);
        }
    }
    // Event fired when Action combobox Value is changed.
    public void checkAction() {

        comboxProv1.setDisable(true);
        switch (comboxAction.getValue().toString()) {
            case "Action":
                comboxProv1.getSelectionModel().select(0);
                break;
            case "Move":
            case "Support":
                comboxProv1.getSelectionModel().select(1);
                comboxProv1.setDisable(false);
                break;
            case "Hold":
                comboxProv1.getSelectionModel().select(1);
                break;
        }

        if(selectedProvince != null)
            updateComboBoxes(selectedProvince);
    }


    @Override
    public void update(GameViewObservable gameViewObservable) {
        troops.getChildren().removeAll(troops.getChildren());
        troops.getChildren().addAll(gameViewObservable.getTroopsGroup());

        pOrderSettings.setDisable(gameViewObservable.getDisableOrderMenu());

        if(gameViewObservable.firstUpdate()){
            comboxPrivateChat.getItems().removeAll(comboxPrivateChat.getItems());
            comboxPrivateChat.getItems().setAll(gameViewObservable.privateChatValues());
            comboxPrivateChat.getSelectionModel().select(0);
        }

        //if(gameViewObservable.pointsChanged()){
            points.getChildren().removeAll(points.getChildren());
            points.getChildren().addAll(gameViewObservable.getPointsGroup());
            addProvinceEvents(gameViewObservable.getProvinces());
       // }

        if(gameViewObservable.doRemoveAllPoints()) {
            troops.getChildren().removeAll(troops.getChildren());
            points.getChildren().removeAll(points.getChildren());
        }

        if(gameViewObservable.hasComboBoxes()) {
            fillComboBox(comboxProv1, true, gameViewObservable.getComboBox1Values());
        }
    }

    private void fillComboBox(ComboBox comboBox, boolean selectFirst, ArrayList<String> items) {
        comboBox.getItems().removeAll(comboBox.getItems());
        comboBox.getItems().addAll(items);
        if(selectFirst)
            comboBox.getSelectionModel().select(0);
    }

    private void fillComboBox(ComboBox comboBox, boolean selectFirst, String... items) {
        this.fillComboBox(comboBox, selectFirst, new ArrayList<>(Arrays.asList(items)));
    }

    private void addProvinceEvents(List<Province> provinces) {
        for(Province province : provinces) {
            province.setOnMouseClicked(event -> {

                for(Province province1 : provinces) {
                    province1.setScaleX(0.3);
                    province1.setScaleY(0.3);
                }
                this.selectedProvince = (Province) event.getTarget();
                this.updateComboBoxes((Province) event.getTarget());
            });
        }
    }

    private void updateComboBoxes(Province target) {
        target.setScaleX(0.5);
        target.setScaleY(0.5);
        gameController.changedComboBox(comboxAction.getValue().toString(), selectedProvince, comboxProv1);
    }

    @FXML
    private void OpenMenu() {
        MainMenu.setVisible(!MainMenu.isVisible());
        gameController.hideVisualPoints(MainMenu.isVisible());
    }

    /**
     * Sets the sound on/off, with the use of a public method, so it can be accessed from both Main Menu and in_game.
     * @author: Sebas & Edwin
     * @version: June 2019
     */
    @FXML
    private void geluidAanUit() {
        if (geluidsKnop.isSelected()) {
            geluidsKnop.setText("Uit");
            geluidsKnop.setAlignment(Pos.CENTER);
            gameController.getMainController().toggleMusic(false);
        } else {
            geluidsKnop.setText("Aan");
            geluidsKnop.setAlignment(Pos.CENTER);
            gameController.getMainController().toggleMusic(true);
        }
    }

    @FXML
    private void returnInGameMenu() {
        gameOpties.setVisible(!gameOpties.isVisible());
        MainMenu.setVisible(!MainMenu.isVisible());
    }

    //Shows the in_game options and hides the in_game options.
    @FXML
    private void inGameOpties() {
        gameOpties.setVisible(!gameOpties.isVisible());
        MainMenu.setVisible(!MainMenu.isVisible());
    }

    //Dit sluit het spel af.
    @FXML
    private void afsluitenView() {
        System.exit(0);
    }

    /**
     * Creates the pages and sets every page of the rulebook to a page.
     * @return Places the created pages inside the VBox.
     * @author Sebas
     * @version June 2019
     */
    @FXML
    public VBox createPage(int pageIndex) {
        VBox box = new VBox();
        final ArrayList<String> imagesRules = new ArrayList<>();
        for (int i = 1; i <= 24; i++) {
            imagesRules.add("/resources/rules/rulebook-" + i + ".png");
        }
        box.setAlignment(Pos.CENTER);
        ImageView iv = new ImageView(String.valueOf(imagesRules.get(pageIndex)));
        box.getChildren().addAll(iv);
        return box;
    }

    /**
     * This shows Pagination and gives it to createPage.
     * @throws IOException
     * @author Sebas
     * @version June 2019
     */
    @FXML
    private void spelRegelsView() throws IOException {
        box.setVisible(!box.isVisible());
        paginationrules.setVisible(!paginationrules.isVisible());
        inGameMenuButton.setDisable(true);

        paginationrules.setPageFactory((Integer pageIndex) -> createPage(pageIndex));
    }

    //Returns you back to the Main Menu from within the active game.
    @FXML
    private void returnToMainMenu() {
        gameController.returnToMain();
    }

    //Hides the gamerules and enables the menu button again.
    @FXML
    private void returnSpelRegels() {
        box.setVisible(!box.isVisible());
        paginationrules.setVisible(!paginationrules.isVisible());
        inGameMenuButton.setDisable(false);
    }


    public void updatePlayerInformation() {
        List<Player> playerlist = gameController.getPlayersList();

        for(Player player : playerlist){
            String playerinfo = player.getName() + "  plays as:   " + player.getCountry();
            playersList.getItems().add(playerinfo);
        }
    };

    public void startChatWithGame(){
        ArrayList<String> messageArraylist = gameController.getAllMessages();
        updateMessages(messageArraylist);
    }
}

