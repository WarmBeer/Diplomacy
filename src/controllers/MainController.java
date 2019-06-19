package controllers;

import javafx.stage.Stage;
import models.GameModel;
import models.SuperModel;
import services.FirebaseService;

import java.util.ArrayList;
import java.util.Map;

public class MainController {

    private SuperModel superModel;
    public GameController gameController;
    private FirebaseService fb;
    private String currentGameUID;
    private boolean gameUIDIsReady = false;

    public GameModel passGameModel(){
        GameModel gamemodel = gameController.giveGameModel();
        return gamemodel;
    }

    public MainController(Stage primaryStage) {
        superModel = new SuperModel(primaryStage, this);
        this.gameController = new GameController(primaryStage, this);
        this.fb = FirebaseService.getInstance();
    }

    public void clickedOptions() {

    }

    public void clickedJoinGame(String gameUID) {
        setGameID(gameUID);
        gameController.requestLoadGame(this.currentGameUID);
    }

    private void setGameID(String gameUID){
        currentGameUID = gameUID;
        gameUIDIsReady = true;
    }

    public String getGameID(){
        return currentGameUID;
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

//    public void fillingLobbyMainControl() {
//        fb.getPlayerInformation(getGameID());
//    }

    // TODO: 19-6-2019 GAME ID IS NOG FIXED DOOR BUG!!! 
    public ArrayList<Map> getPlayersList(){
        String gameUID = getGameID();
        ArrayList<Map> playerlist = fb.getPlayerInformation("11111111");
        return playerlist;
    }

}
