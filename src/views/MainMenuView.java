package views;

import controllers.MainController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import observers.MainMenuViewObservable;
import observers.MainMenuViewObserver;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.LogManager;

public class MainMenuView implements MainMenuViewObserver {
    private Stage stage;
    private Scene scene;

    private static String VIEW_FILE = "/resources/views/MainMenu.fxml";
    private static String STYLESHEET_FILE = "/resources/MainMenu.css";
    private MainController mainController;
    private ArrayList<String> gameIDS;

    @FXML
    private Button ReturnMenu;

    @FXML
    private Button hostGameReturn;

    @FXML
    private Button HostGameButton;

    @FXML
    private AnchorPane hostGameAnchor;

    @FXML
    private AnchorPane listGamesAnchor;

    @FXML
    private ListView listGames;

    @FXML
    private ToggleButton geluidsKnop;

    @FXML
    private AnchorPane gameOpties;

    @FXML
    private Button optionsButton;

    @FXML
    private Button joinGameButton;

    @FXML
    private Button hostGameButton;

    @FXML
    private Button AfsluitenButton;

    @FXML
    public void clickedOptions() {
        mainController.clickedOptions();
    }

    @FXML
    public void clickedJoinGame() {

    }

    @FXML
    public void clickedHostGame() {
        mainController.clickedHostGame();
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

    //Player klikt op GameID, deze functie opend het bijbehordende gamescherm
    @FXML
    public void handleMouseClick(MouseEvent arg0) {
        int gameIDIndex = listGames.getSelectionModel().getSelectedIndex();
        gameIDS = mainController.getGameIDS();
        String gameID = gameIDS.get(gameIDIndex);

        System.out.println(gameID);
        mainController.clickedJoinGame(gameID);
    }

    @FXML
    private void showHostGame() {
        hostGameAnchor.setVisible(!hostGameAnchor.isVisible());
    }
}
