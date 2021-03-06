package models;

import controllers.MainController;
import domains.GameJSON;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import observers.MainMenuViewObservable;
import observers.MainMenuViewObserver;
import views.MainMenuView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Class That mostly manages the main menu view
 */
public class SuperModel implements Model, MainMenuViewObservable {

    ArrayList<MainMenuViewObserver> MainMenuViewObservers = new ArrayList<>();
    private MainMenuView mainMenuView;
    private boolean showMainMenu = false;
    private GameJSON gameJSON;

    public SuperModel(Stage primaryStage, MainController mainController) {
        try {
            mainMenuView = new MainMenuView(primaryStage, mainController);
            registerMainMenuViewObserver(mainMenuView);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setAppIcon(primaryStage);
    }

    public void setGameJSON(GameJSON gameJSON) {
        this.gameJSON = gameJSON;
    }

    public GameJSON getGameJSON() {
        return gameJSON;
    }

    public void onLobbyEvent(GameJSON gameJSON) {
        setGameJSON(gameJSON);
        notifyMainMenuViewObservers();
    }

    private void setAppIcon(Stage primaryStage) {
        Image icon = new Image("icon.png");
        primaryStage.getIcons().add(icon);
    }

    public void show() {
        showMainMenu = true;
        notifyMainMenuViewObservers();
    }

    public void hide() {
        showMainMenu = false;
    }

    public void dontShowMainMenu() {
        this.showMainMenu = false;
    }

    @Override
    public void registerMainMenuViewObserver(MainMenuViewObserver mainMenuViewObserver) {
        MainMenuViewObservers.add(mainMenuViewObserver);
    }

    @Override
    public void unregisterMainMenuViewObserver(MainMenuViewObserver mainMenuViewObserver) {
        MainMenuViewObservers.remove(mainMenuViewObserver);
    }

    @Override
    public void notifyMainMenuViewObservers() {
        for(MainMenuViewObserver MainMenuViewObserver : MainMenuViewObservers) {
            MainMenuViewObserver.update(this);
        }
    }

    @Override
    public boolean doShowMainMenu() {
        return showMainMenu;
    }

    public void showLoginScreenModel() {
        mainMenuView.showLoginScreen();
    }
}
