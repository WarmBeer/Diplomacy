package models;

import controllers.MainController;
import javafx.stage.Stage;
import observers.MainMenuViewObservable;
import observers.MainMenuViewObserver;
import views.MainMenuView;

import java.io.IOException;
import java.util.ArrayList;

public class SuperModel implements Model, MainMenuViewObservable {

    public final String MAIN_MENU = "/views/MainMenu_OLD.fxml"; //DIT MOET WEG UITEINDELIJK
    ArrayList<MainMenuViewObserver> MainMenuViewObservers = new ArrayList<>();
    private MainMenuView mainMenuView;
    private boolean showMainMenu = false;

    public SuperModel(Stage primaryStage, MainController mainController) {
        try {
            mainMenuView = new MainMenuView(primaryStage, mainController);
            registerMainMenuViewObserver(mainMenuView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void show() {
        showMainMenu = true;
        notifyMainMenuViewObservers();
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
}
