package controllers;

import javafx.stage.Stage;
import models.SuperModel;
import services.FirebaseService;

import java.util.ArrayList;

public class MainController {

    private SuperModel superModel;
    public GameController gameController;
    private FirebaseService fb;

    public MainController(Stage primaryStage) {
        superModel = new SuperModel(primaryStage, this);
        this.gameController = new GameController(primaryStage, this);
        this.fb = FirebaseService.getInstance();
    }

    public void clickedOptions() {

    }

    public void clickedJoinGame(String gameUID) {
        gameController.requestLoadGame(gameUID);
    }

    public void clickedHostGame() {
    }

    public void dontShowMainMenu() {
        this.superModel.dontShowMainMenu();
    }

    public void showMainMenu() {
        superModel.show();
    }

    public ArrayList<String> getFullGameName(){
        ArrayList<String> gameNames = fb.getGameName();
        return gameNames;
    }

    public ArrayList<String> getGameIDS(){
        ArrayList<String> GameIDs = fb.getGameIDs();
        return GameIDs;
    }

}
