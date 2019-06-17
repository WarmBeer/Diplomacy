package views;

import controllers.MainController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneBuilder;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import observers.MainMenuViewObservable;
import observers.MainMenuViewObserver;

import java.io.IOException;
import java.net.URL;

public class MainMenuView implements MainMenuViewObserver {
    private Stage stage;
    private Scene scene;

    private static String VIEW_FILE = "/resources/views/MainMenu.fxml";
    private static String STYLESHEET_FILE = "/resources/MainMenu.css";
    private MainController mainController;

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
        mainController.clickedJoinGame();
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
        //primaryStage.setScene(scene);

        //FXMLLoader loader = new FXMLLoader(getClass().getResource(VIEW_FILE));
//        Parent root = loader.load();
//        this.scene = SceneBuilder.create().root(root).width(1280).height(720)
//                .fill(Color.GRAY).build();
//        scene.getStylesheets().add(STYLESHEET_FILE);

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
}
