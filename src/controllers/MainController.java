package controllers;

import javafx.fxml.FXML;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.GameModel;
import models.SuperModel;
import services.FirebaseService;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class MainController {

    private SuperModel superModel;
    public GameController gameController;
    private FirebaseService fb;
    private String currentGameUID;
    private boolean gameUIDIsReady = false;

    String gameSoundFile = "src/resources/chipz.mp3";
    Media gameSound = new Media(new File(gameSoundFile).toURI().toString());
    MediaPlayer mediaplayer = new MediaPlayer(gameSound);

    public GameModel.Countries  getAvailableCountry(String gameUID){
        GameModel.Countries  availableCountry = gameController.giveAvailableCountry(gameUID);
        return availableCountry;
    }

    public MainController(Stage primaryStage) {
        superModel = new SuperModel(primaryStage, this);
        this.gameController = new GameController(primaryStage, this);
        this.fb = FirebaseService.getInstance();
    }

    public SuperModel getSuperModel() {
        return this.superModel;
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
        this.superModel.hide();
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

    public void startMusic() {
        mediaplayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mediaplayer.seek(Duration.ZERO);
                mediaplayer.play();
            }
        });
        mediaplayer.setAutoPlay(true);
    }

    public void toggleMusic(boolean turnOn) {
        if(turnOn) { mediaplayer.play(); }
        else { mediaplayer.pause(); }
    }
}
