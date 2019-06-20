package views;

import controllers.MainController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.GameModel;
import observers.MainMenuViewObservable;
import observers.MainMenuViewObserver;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.LogManager;

public class MainMenuView implements MainMenuViewObserver {
    private Stage stage;
    private Scene scene;

    private static String VIEW_FILE = "/resources/views/MainMenu.fxml";
    private static String STYLESHEET_FILE = "/resources/MainMenu.css";
    private MainController mainController;
    private ArrayList<String> gameIDS;
    ArrayList<Label> playerLabelsLobby = new ArrayList<>();
    ArrayList<Label> countryLabelsLobby = new ArrayList<>();

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
    @FXML private Button startGameHost;
    @FXML private AnchorPane lobbyAnchor;
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

    @FXML
    public void clickedOptions() {
        mainController.clickedOptions();
    }

    @FXML
    public void clickedJoinGame() {

    }

    @FXML
    public void clickedHostGame() {
        lobbyAnchor.setVisible(true);
        mainController.clickedHostGame();
    }

    @FXML
    public void clickedStartGameHost() {
        System.out.println("hal");
        boolean isHost = true;

        if(isHost == true){
            lobbyAnchor.setVisible(false);
            int gameIDIndex = listGames.getSelectionModel().getSelectedIndex();
            gameIDS = mainController.getGameIDS();
            String gameID = gameIDS.get(gameIDIndex);

            System.out.println(gameID);
            mainController.clickedJoinGame(gameID);
        }
        else{
            System.out.println("Nope");
        }

    }

    @FXML
    public void showOptions() {
        gameOpties.setVisible(!gameOpties.isVisible());
    }

    public MainMenuView(Stage primaryStage, MainController mainController) throws IOException {
        this.mainController = mainController;
        this.stage = primaryStage;

        URL url = getClass().getResource(VIEW_FILE);
        FXMLLoader loader = new FXMLLoader(url);
        loader.setController(this);
        Parent content = loader.load();

        primaryStage.setTitle("Diplomacy v0.2");
        scene = new Scene( content, 1280, 720 );
        primaryStage.setResizable(false);


    }

    public void show() {
        //stage.hide();
        //stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void update(MainMenuViewObservable mainMenuViewObservable) {
        if(mainMenuViewObservable.doShowMainMenu()) {
            this.show();
        }
    }

    //Dit sluit het spel af.
    @FXML
    private void afsluitenView() {
        System.exit(0);
    }

    @FXML
    private void showGamesList() {
        listGamesAnchor.setVisible(!listGamesAnchor.isVisible());
        listGames.setVisible(!listGames.isVisible());
        ReturnMenu.setVisible(!ReturnMenu.isVisible());

        listGames.getItems().clear();
        gameIDS = mainController.getFullGameName();

        for(String game : gameIDS){
            listGames.getItems().add(listGames.getItems().size(), game);
            listGames.scrollTo(game);
            LogManager.getLogManager().reset();
        }
    }



    // TODO: 19-6-2019 Parameter zijn nu nog hardcoded, moet gefixt worden (om game te starten)
    @FXML
    public void handleMouseClick(MouseEvent arg0) {
        String gameID = getChooseGameID();

        lobbyAnchor.setVisible(true);
        mainController.passGameModel().joinLobby(gameID);

        //playerJoined moet hier
        GameModel.Countries test = GameModel.Countries.RUSSIA;
        mainController.passGameModel().playerJoined(gameID,"Thomas", test);

        initLobbyLabels();

        //Vul labels met shit
        updateJoinedPlayersinformation();

        //mainController.clickedJoinGame(gameID);
    }

    public String getChooseGameID(){
        int gameIDIndex = listGames.getSelectionModel().getSelectedIndex();
        gameIDS = mainController.getGameIDS();
        String gameID = gameIDS.get(gameIDIndex);
        System.out.println(gameID);
        return gameID;
    }

    @FXML
    private void showHostGame() {
        hostGameAnchor.setVisible(!hostGameAnchor.isVisible());
        aantalTijd.getItems().addAll("", "5 min", "10 min", "15 min", "20 min");
        aantalTijd.getSelectionModel().select(0);
    }

    public void updateJoinedPlayersinformation() {
        String gameUID = getChooseGameID();
        ArrayList<Map> playerinfo = mainController.getPlayersList(gameUID);

        for(int x = 0; x < playerinfo.size(); x++){
            String playername = (String) playerinfo.get(x).get("name");
            String country = (String) playerinfo.get(x).get("country");
            playerLabelsLobby.get(x).setText(playername);
            countryLabelsLobby.get(x).setText(country);

        }
    }

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
