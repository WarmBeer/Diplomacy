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

    public GameModel.Countries  getAvailableCountry(String gameUID){
        GameModel.Countries  availableCountry = gameController.giveAvailableCountry(gameUID);
        return availableCountry;
    }

    public MainController(Stage primaryStage) {
        superModel = new SuperModel(primaryStage, this);
        this.gameController = new GameController(primaryStage, this);
        this.fb = FirebaseService.getInstance();
    }

    public void clickedJoinGame(String gameUID) {
        setGameID(gameUID);
        gameController.requestLoadGame(this.currentGameUID);
    }

    private void setGameID(String gameUID){
        currentGameUID = gameUID;
        gameUIDIsReady = true;
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

    public void createLoginName() {
        superModel.showLoginScreenModel();
    }

//    public void createKey() {
//        String jarLocation = KeyHandler.getJarLocation();
//        KeyHandler.createKeyFile(jarLocation);
//    }

    public void addPlayerToGame(String getKEY, String playerName) {
        System.out.println(getKEY + playerName);
        fb.addNewPlayerInFirebase(getKEY, playerName);
    }

}
