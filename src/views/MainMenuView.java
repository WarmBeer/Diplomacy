package views;

import application.Main;
import controllers.MainController;
import domains.Game;
import domains.GameJSON;
import domains.Player;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import observers.MainMenuViewObservable;
import observers.MainMenuViewObserver;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogManager;

/**
 * Class that handles input and output of the FXML file (view) of the main menu
 */

public class MainMenuView implements MainMenuViewObserver {
    private Stage stage;
    private Scene scene;

    private static String VIEW_FILE = "/resources/views/MainMenu.fxml";
    private MainController mainController;
    private ArrayList<String> gameIDS;
    private ArrayList<Label> playerLabelsLobby = new ArrayList<>();
    private ArrayList<Label> countryLabelsLobby = new ArrayList<>();
    private String playerNamee;
    private States state = States.NONE;
    private Game game;

    enum States {
        LOBBY,
        NONE
    }

    //FXML Variables
    @FXML private Label player1;
    @FXML private Label player2;
    @FXML private Label player3;
    @FXML private Label player4;
    @FXML private Label player5;
    @FXML private Label player6;
    @FXML private Label player7;
    @FXML private Label country1;
    @FXML private Label country2;
    @FXML private Label country3;
    @FXML private Label country4;
    @FXML private Label country5;
    @FXML private Label country6;
    @FXML private Label country7;
    @FXML private Button ReturnLobby;
    @FXML private Button startGameHost;
    @FXML private AnchorPane lobbyAnchor;
    @FXML private AnchorPane loginScreenAnchor;
    @FXML private ComboBox aantalTijd;
    @FXML private Text gameNaam;
    @FXML private Button ReturnMenu;
    @FXML private Button hostGameReturn;
    @FXML private AnchorPane hostGameAnchor;
    @FXML private AnchorPane listGamesAnchor;
    @FXML private ListView listGames;
    @FXML private ToggleButton geluidsKnop;
    @FXML private AnchorPane gameOpties;
    @FXML private Button optionsButton;
    @FXML private Button joinGameButton;
    @FXML private Button hostGameButton;
    @FXML private Button afsluitenButton;
    @FXML private Button createCustomGame;
    @FXML private Button startGameLoginScreen;
    @FXML public TextField textFieldUserName;
    @FXML private TextField gameName;
    @FXML private ComboBox turnTime;


    @FXML
    public void clickedOptions() {
        gameOpties.setVisible(!gameOpties.isVisible());
    }

    /**
     * Sets the sound on/off, with the use of a public method, so it can be accessed from both Main Menu and in_game.
     * @author: Sebas & Edwin
     * @version: June 2019
     */
    @FXML
    private void geluidUitAan() {
        if (geluidsKnop.isSelected()) {
            geluidsKnop.setText("Uit");
            mainController.toggleMusic(false);
        }
        else {
            geluidsKnop.setText("Aan");
            mainController.toggleMusic(true);
        }
    }

    @FXML
    public void configureStartGame() {
        if (mainController.gameController.getGamemodel().getActiveGame().getHost().equals(Main.getKEY())) {
            startGameHost.setVisible(true);
        } else {
            startGameHost.setVisible(false);
        }
    }

    @FXML
    public void clickedStartGameHost() {
        this.state = States.NONE;
        mainController.gameController.startLobby();
        mainController.gameController.stopLobbyListener();
    }

    public MainMenuView(Stage primaryStage, MainController mainController) throws IOException {
        this.mainController = mainController;
        this.stage = primaryStage;

        URL url = getClass().getResource(VIEW_FILE);
        FXMLLoader loader = new FXMLLoader(url);
        loader.setController(this);
        Parent content = loader.load();

        primaryStage.setTitle("Diplomacy v1.0f");
        scene = new Scene( content, 1280, 720 );
        primaryStage.setResizable(false);
    }

    public void show() {
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void update(MainMenuViewObservable mainMenuViewObservable) {
        if (state == States.LOBBY) {
            updateJoinedPlayersinformation();
            if (!mainController.gameController.inLobby()) {
                this.state = States.NONE;
                mainController.gameController.requestLoadGame(mainController.gameController.getGamemodel().getActiveGame().getGameUID());
            }
        } else {
            mainController.gameController.stopLobbyListener();
        }
        if(mainMenuViewObservable.doShowMainMenu()) {
            this.show();
        }
    }

    @FXML
    private void afsluitenView() {
        System.exit(0);
    }

    /**
     * Starts lobby and announces it to Firestore.
     * @author Mick van Dijke
     * @version June 2019
     */

    @FXML
    private void createCustomGame() {
        lobbyAnchor.setVisible(true);
        int turn_time = (turnTime.getValue() != null) ? (int)turnTime.getValue() : 5;
        String game_name = (gameName.getText() != null) ? gameName.getText() : "This is a game of Diplomacy!";
        mainController.gameController.createLobby(game_name, turn_time);
        initLobbyLabels();
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        updateJoinedPlayersinformation();
        mainController.gameController.startLobbyListener();
        this.state = States.LOBBY;
        configureStartGame();
    }

    /**
     * Makes the available games visible and invisible on click.
     * This method is added to the Join Game and Return Menu button.
     * @author: Sebas
     * @version: June 2019
     */
    @FXML
    private void showGamesList() {
        listGamesAnchor.setVisible(!listGamesAnchor.isVisible());
//        listGames.setVisible(!listGames.isVisible());
//        ReturnMenu.setVisible(!ReturnMenu.isVisible());

        listGames.getItems().clear();
        gameIDS = mainController.getFullGameName();

        for(String game : gameIDS){
            listGames.getItems().add(listGames.getItems().size(), game);
            listGames.scrollTo(game);
            LogManager.getLogManager().reset();
        }
    }

    private boolean playerInGame(GameJSON gameJSON) {
        boolean player_in_game = false;
        for (Player player : gameJSON.Players) {
            if (player.getUID().equals(Main.getKEY())) {
                player_in_game = true;
            }
        }
        return player_in_game;
    }


    @FXML
    public void handleMouseClick(MouseEvent arg0) {

        String gameID = getChooseGameID();

        GameJSON gameJSON = mainController.gameController.retrieveGameJSON(gameID);

        if (!gameJSON.inLobby && playerInGame(gameJSON)) {
            mainController.gameController.getGamemodel().initGame(gameJSON);
            mainController.gameController.requestLoadGame(gameID);
        } else if (gameJSON.Players.size() < 7){
            this.state = States.LOBBY;
            mainController.gameController.getGamemodel().initGame(gameJSON);
            lobbyAnchor.setVisible(true);
            mainController.gameController.joinLobby(gameID);
            initLobbyLabels();
            updateJoinedPlayersinformation();
            mainController.gameController.startLobbyListener();
            configureStartGame();
        }
    }

    @FXML
    private void returnLobby() {
        this.state = States.NONE;
        lobbyAnchor.setVisible(false);
        mainController.gameController.stopLobbyListener();
    }

    @FXML
    public void createUser() {
        // Alles schowen op de achtergrond, key aanmaken voor de persoon, hierna de naam + key koppelen aan firebase.
        playerNamee = textFieldUserName.getText();
        loginScreenAnchor.setVisible(false);
        if (loginScreenAnchor.isVisible() == false) {
            joinGameButton.setDisable(false);
            hostGameButton.setDisable(false);
            afsluitenButton.setDisable(false);
        }
        String getKEY = Main.getKEY();
        mainController.addPlayerToGame(getKEY, playerNamee);

    }

    // TODO: 20-6-2019
    @FXML
    public void showLoginScreen() {
//        mainController.createKey();
        loginScreenAnchor.setVisible(!loginScreenAnchor.isVisible());
        if (loginScreenAnchor.isVisible() == true) {
            joinGameButton.setDisable(true);
            hostGameButton.setDisable(true);
            afsluitenButton.setDisable(true);
        }
    }

    public String getChooseGameID(){
        int gameIDIndex = listGames.getSelectionModel().getSelectedIndex();
        gameIDS = mainController.getGameIDS();
        String gameID = gameIDS.get(gameIDIndex);
        return gameID;
    }

    @FXML
    private void toggleHostGame() {
        hostGameAnchor.setVisible(false);
    }

    @FXML
    private void clickedHostGame() {
        hostGameAnchor.setVisible(!hostGameAnchor.isVisible());
        turnTime.getItems().addAll(5, 10, 15, 20);
        turnTime.getSelectionModel().select(0);
    }


    /**
     * refresh labeltext with playerinformation
     * @author Thomas Zijl
     */
    public void updateJoinedPlayersinformation() {
        List<Player> playerInfo = mainController.gameController.getPlayersList();

        for(Player player : playerInfo){
            playerLabelsLobby.get(player.getId()).setText(player.getName());
            countryLabelsLobby.get(player.getId()).setText(player.getCountry().toString());

        }
    }


    /**
     * add lobby labels to an arraylist and gives them default text.
     * @author Thomas Zijl
     */
    public void initLobbyLabels(){
        playerLabelsLobby.add(player1);
        playerLabelsLobby.add(player2);
        playerLabelsLobby.add(player3);
        playerLabelsLobby.add(player4);
        playerLabelsLobby.add(player5);
        playerLabelsLobby.add(player6);
        playerLabelsLobby.add(player7);
        countryLabelsLobby.add(country1);
        countryLabelsLobby.add(country2);
        countryLabelsLobby.add(country3);
        countryLabelsLobby.add(country4);
        countryLabelsLobby.add(country5);
        countryLabelsLobby.add(country6);
        countryLabelsLobby.add(country7);

        for(int x = 0; x < playerLabelsLobby.size(); x++){
            final String DEFAULTPLAYERNAME =  "Still empty";
            final String DEFAULTCOUNTRYNAME = "Still empty";
            playerLabelsLobby.get(x).setText(DEFAULTPLAYERNAME);
            countryLabelsLobby.get(x).setText(DEFAULTCOUNTRYNAME);

        }
    }

}
