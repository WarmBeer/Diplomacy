package views;

import controllers.GameController;
import controllers.MainController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import observers.MainMenuObservable;
import observers.MainMenuObserver;

import java.io.IOException;
import java.net.URL;

import static application.Main.MAIN_MENU;

public class MainMenuView implements MainMenuObserver {

    private Stage primaryStage;
    private String sceneFile;
    private MainController mainController;

    public MainMenuView(Stage primaryStage, MainController mainController) {
        this.primaryStage = primaryStage;
        this.sceneFile = MAIN_MENU;
        this.mainController = mainController;
    }

    @FXML
    void clickedJoinGame() {
        mainController.clickJoinedGame(primaryStage);
    }

    @FXML
    void clickedHostGame() {
        mainController.clickedHostGame();
    }

    @FXML
    void clickedOptions() {
        mainController.clickedOptions();
    }



    public void launchMainMenu() {
        try {
            URL url = getClass().getResource(sceneFile);
            FXMLLoader loader = new FXMLLoader(url);
            loader.setController(this);
            Parent content = loader.load();

            primaryStage.setTitle("Welkom!");
            primaryStage.setScene(new Scene(content));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(MainMenuObservable mainMenuObservable) {

    }
}
