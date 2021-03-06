package controllers;

import application.Main;
import com.google.cloud.firestore.FirestoreException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import domains.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import models.ChatBox;
import models.GameModel;
import observers.ChatObserver;
import observers.OrderObserver;
import services.FirebaseService;
import utilities.KeyHandler;
import views.GameView;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Class that handles retrieving, saving and parsing the game.
 */

public class GameController  {

    private GameModel gameModel;
    private ChatBox chatbox;
    private MainController mainController;
    private FirebaseService fb;
    private List<Unit> orderedUnits;
    private final int CHARACTERLIMIT = 50;
    private ArrayList<String> messageArraylist;
    private ListenState listenState;

    private enum ListenState {
        NON,
        LOBBY,
        GAME
    }

    public GameController(Stage stage, MainController mainController){
        orderedUnits = new ArrayList<>();
        fb = FirebaseService.getInstance();
        this.chatbox = new ChatBox(fb);
        this.gameModel = new GameModel(stage, this, chatbox);
        this.mainController = mainController;
        this.listenState = ListenState.NON;
    }

    public GameModel getGamemodel(){
        return this.gameModel;
    }

    /**
     * Create lobby as player.
     * @author Mick van Dijke
     */

    public void createLobby(String gameName, int turnTime) {
        gameModel.createLobby(gameName, turnTime);
        chatbox.makeNewChat(gameModel.getActiveGame().getGameUID());
        String playerKey = Main.getKEY();
        Player player = new Player();
        player.setUID(Main.getKEY());
        player.setId(0);
        player.setName("Player " + player.getId() + " - " + mainController.gameController.fb.playerUIDtoPlayername(playerKey));
        player.setCountry(GameModel.Countries.GERMANY);
        gameModel.getActiveGame().addPlayer(player);
        saveToFirebase();
        listenState = ListenState.LOBBY;
    }

    public void startLobbyListener() {
        fb.startLobbyListener(gameModel.getActiveGame().getGameUID(), mainController.getSuperModel());
    }

    public void stopLobbyListener() {
        fb.stopLobbyListener();
    }

    public void saveToFirebase() {
        GameJSON gameJSON = saveGameToJSON();
        fb.saveGame(gameJSON);
    }

    public GameModel.Countries giveAvailableCountry(String gameID){
        GameModel.Countries country = gameModel.giveAvailableCountry(gameID);
        return country;
    }

    public void addUnitOrder(Unit unit) {
        if (orderedUnits.indexOf(unit) < 0) {
            orderedUnits.add(unit);
        }
    }

    public void clickedEndTurn() {
        processOrders();
    }

/**
* Haalt de game op van Firestore.
*@author Mick van Dijke
*@version June 2019
*/

    public GameJSON retrieveGameJSON(String gameUID) {

        GameJSON gameJSON = fb.getGame(gameUID);

        /*
        Reader reader = new BufferedReader(new InputStreamReader(
                this.getClass().getResourceAsStream("/" + "Diplomacy.json")));
        Gson gson = new GsonBuilder().create();
        GameJSON gameJSON = gson.fromJson(reader, GameJSON.class);
        */
        return gameJSON;
    }

    private List<GameModel.Countries> getFreeCountries(GameJSON gameJSON) {
        List<GameModel.Countries> freeCountries = new ArrayList<>();
        List<GameModel.Countries> takenCountries = new ArrayList<>();

        for (Player player : gameJSON.Players) {
            takenCountries.add(player.getCountry());
        }

        for (GameModel.Countries countries : GameModel.Countries.values()) {
            if (countries == GameModel.Countries.INDEPENDENT) {continue;}
            if (takenCountries.indexOf(countries) < 0) {
                freeCountries.add(countries);
            }
        }

        return freeCountries;
    }

    public void gameFirebaseUpdated(GameJSON gameJSON, FirestoreException e) {
        //this means the host increased the turn by 1
        if(gameModel.getActiveGame() != null&&
                gameJSON.turn != gameModel.getActiveGame().getTurn()) {
            requestLoadGame(gameModel.getActiveGame().getGameUID());
        }
    }

/**
* Maakt van JSON een Game object.
*@author Mick van Dijke
*@version June 2019
*/

    public GameJSON saveGameToJSON() {

        String gameSave = "";

        GameJSON gameJSON = new GameJSON();
        gameJSON.gameUID = gameModel.getActiveGame().getGameUID();
        gameJSON.host = gameModel.getActiveGame().getHost();
        gameJSON.name = gameModel.getActiveGame().getName();
        gameJSON.turn = gameModel.getActiveGame().getTurn();
        gameJSON.turnTime = gameModel.getActiveGame().getTurnTime();
        gameJSON.Players = gameModel.getActiveGame().getPlayers();
        gameJSON.Provinces = new ArrayList<>();
        gameJSON.freeCountries = getFreeCountries(gameJSON);
        gameJSON.inLobby = gameModel.getActiveGame().getLobby();

        for (Province province : gameModel.getActiveGame().getProvinces()) {
            ProvinceJSON provinceJSON = new ProvinceJSON();
            provinceJSON.abbr = province.getAbbreviation();
            provinceJSON.owner = province.getOwner().getName();
            provinceJSON.provinceType = province.getProvinceType();

            UnitJSON unitJSON = new UnitJSON();

            if (province.getUnit() != null) {
                unitJSON.unitType = (province.getProvinceType() == Province.ProvinceType.SEA) ? Main.unitType.FLEET : Main.unitType.ARMY;
                unitJSON.orderType = province.getUnit().getCurrentOrder();
                unitJSON.orderTarget = province.getUnit().getTargetProvince().getAbbreviation();
            }

            if (unitJSON.unitType == null) {
                provinceJSON.stationed = null;
            } else {
                provinceJSON.stationed = unitJSON;
            }
            gameJSON.Provinces.add(provinceJSON);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            String jarLocation = KeyHandler.getJarLocation();
            String saveFile = gson.toJson(gameJSON);
            JsonElement jsonElement =  new JsonParser().parse(saveFile);
            BufferedWriter writer = new BufferedWriter(new FileWriter(jarLocation + File.separator + "Save.json"));
            gameSave = gson.toJson(jsonElement);
            writer.write(gson.toJson(jsonElement));
            writer.close();
        } catch (IOException io) {
            io.printStackTrace();
        }

        return gameJSON;
    }

    public void show() {
        this.mainController.dontShowMainMenu();
        this.gameModel.show();
    }

/**
* Loads game from Firestore and shows it on screen.
*@author Mick van Dijke
*@version June 2019
*/

    public void requestLoadGame(String gameUID){
        try{
            mainController.getSuperModel().hide();
            gameModel.show();
            GameJSON gameJSON = retrieveGameJSON(gameUID);

            gameModel.initGame(gameJSON);
            chatbox.notifyChatObservers();
            orderedUnits = new ArrayList<>();
            //sendFirstMessage();

        }
        catch(Exception E){
            System.out.println("Exception while request to load a game");
            E.printStackTrace();
        }
        fb.setGameListener(gameUID, this);
        //gameModel.createUnitsPerPlayer();
    }

    public boolean inLobby() {
        GameJSON gameJSON = fb.getGame(gameModel.getActiveGame().getGameUID());
        return gameJSON.inLobby;
    }

    public void checkOrder(ListView lvOrders, String order) {
        gameModel.checkDuplicateUnitOrder(lvOrders, order);
    }

    public void registerOrderObserver(OrderObserver orderObserver){
        this.gameModel.registerOrderObserver(orderObserver);
    }

    public void registerChatObserver(ChatObserver chatObserver){
        chatbox.registerChatObserver(chatObserver);
    }

    public void addOrderIsClicked(String action, String prov1, String prov2){
        gameModel.addOrder(action, prov1, prov2);
    }

    private void createChat(){
        chatbox.makeNewChat(gameModel.getActiveGame().getGameUID());
    }

    public void addMessage(String fromUID, String sendTo, String message) {
        chatbox.addChatMessage(fromUID, sendTo, message, Main.getKEY(), gameModel.getActiveGame().getGameUID());
    }

    public void sendFirstMessage() {
        chatbox.addFirstMessage(Main.getKEY(), gameModel.getActiveGame().getGameUID());
    }


/**
* Verstuurt alle orders naar Firestore.
*@author Mick van Dijke
*@version June 2019
*/

    public void sendOrders() {
        fb.sendOrders(gameModel.getActiveGame().getGameUID(), orderedUnits);
    }

    public void hideVisualPoints(Boolean hide) {
        if (hide) {
            gameModel.hideVisualPoints();
        }
        else {
            gameModel.showVisualPoints();
        }
    }

    public void showVisualPoints() {
        gameModel.showVisualPoints();
    }

    public void registerGameObserver(GameView gameView) {
        gameModel.registerGameViewObserver(gameView);
    }

    //LATER INVOEGEN IN VIEW
    public String cleanMessage(String message){
        String temp_data = message.replaceAll("\\$", "").replaceAll(" ", "");
        return message;
    }

    //LATER INVOEGEN IN VIEW
    public boolean bellowCharacterLimit(String message){
        if(message.length() < CHARACTERLIMIT){
            return true;
        }
        else{
            return false;
        }
    }

    public void returnToMain() {
        this.mainController.showMainMenu();
    }

    public void changedComboBox(String action, Province selectedProvince, ComboBox comboBox1) {
        gameModel.changedComboBox(action, selectedProvince, comboBox1);
    }

    public void clickedSendOrder(ListView ordersList) {
        for(Object order : ordersList.getItems()) {
            String[] orderSplit = order.toString().split("_");
            Unit.orderType order_type = Unit.orderType.valueOf(orderSplit[0].toUpperCase());
            String province1Name = orderSplit[1];
            String province2Name = "";
            if(orderSplit.length > 2) {
                province2Name = orderSplit[2];
            }
            if(gameModel.getProvinceFromName(province1Name).getUnit() != null) {
                gameModel.getProvinceFromName(province1Name).getUnit().addOrder(order_type, gameModel.getProvinceFromName(province2Name));
                addUnitOrder(gameModel.getProvinceFromName(province1Name).getUnit());
            }

        }
        sendOrders();
    }


/**
* Haalt de game op van Firestore, verwerkt alle orders en slaat de game vervolgens weer op in Firestore.
*@author Mick van Dijke
*@version June 2019
*/

    private void processOrders() {

        try {
            requestLoadGame(gameModel.getActiveGame().getGameUID());
        } finally {
            orderedUnits = new ArrayList<>();
            for (int i = 0; i < gameModel.getActiveGame().getUnits().size(); i++) {
                orderedUnits.add(gameModel.getActiveGame().getUnits().get(i));
            }

            List<Unit> supportOrders = new ArrayList<>();
            List<Unit> moveOrders = new ArrayList<>();
            List<Unit> attackOrders = new ArrayList<>();
            for (int i = 0; i < orderedUnits.size(); i++) {
                switch (orderedUnits.get(i).getCurrentOrder()) {
                    case SUPPORT:
                        supportOrders.add(orderedUnits.get(i));
                        break;
                    case MOVE:
                        if (orderedUnits.get(i).getTargetProvince().getUnit() != null) {
                            attackOrders.add(orderedUnits.get(i));
                        } else {
                            moveOrders.add(orderedUnits.get(i));
                        }
                        break;
                }
            }

            for (int i = 0; i < supportOrders.size(); i++) {
                supportOrders.get(i).doOrder();
            }
            for (int i = 0; i < moveOrders.size(); i++) {
                moveOrders.get(i).doOrder();
            }
            for (int i = 0; i < attackOrders.size(); i++) {
                attackOrders.get(i).doOrder();
            }

            for (int i = 0; i < gameModel.getActiveGame().getUnits().size(); i++) {
                gameModel.getActiveGame().getUnits().get(i).reset();
            }

            if (gameModel.getActiveGame().getTurn() % 3 == 0) {
                for (int i = 0;i< gameModel.getActiveGame().getProvinces().size();i++) {
                    gameModel.getProvinces().get(i).spawnUnit();
                }
            }

            orderedUnits = new ArrayList<>();
            gameModel.setPointsChanged();
            gameModel.notifyGameViewObservers();
            gameModel.getActiveGame().nextTurn();
            saveToFirebase();
            requestLoadGame(gameModel.getActiveGame().getGameUID());
        }
    }

    public MainController getMainController() {
        return mainController;
    }

    /**
     * Get list of players in game.
     * @author Thomas Zijl, Mick van Dijke
     */
    public List<Player> getPlayersList(){
        String gameUID = gameModel.getActiveGame().getGameUID();
        List<Player> playerlist = fb.getPlayersFromFB(gameUID);
        return playerlist;
    }

    public void updatePlayers() {
        List<Integer> ids = new ArrayList<>();
        GameJSON gameJSON = retrieveGameJSON(gameModel.getActiveGame().getGameUID());

        for (Player player : gameModel.getActiveGame().getPlayers()) {
            ids.add(player.getId());
        }

        for (Player player : gameJSON.Players) {
            if (ids.indexOf(player.getId()) < 0) {
                gameModel.getActiveGame().addPlayer(player);
            }
        }
    }

    /**
     * Starts the game lobby.
     * @author Mick van Dijke
     */

    public void startLobby() {
        updatePlayers();
        gameModel.startLobby();
        saveToFirebase();
        requestLoadGame(gameModel.getActiveGame().getGameUID());
    }

    public ArrayList<String> getAllMessages(){
        try{
            String gameUID = mainController.gameController.getGamemodel().getActiveGame().getGameUID();
            messageArraylist = fb.getMessages(gameUID);
            return messageArraylist;
        } catch(ExecutionException EE) {
            System.out.println("In de gamecontroller is een Excecution Exception opgetreden!");
            EE.printStackTrace();
            return messageArraylist;
        } catch (InterruptedException IE) {
            System.out.println("In de gamecontroller is een Interrupted Exception opgetreden!");
            IE.printStackTrace();
            return messageArraylist;
        }

    }

    /**
     * Join a lobby as player.
     * @author Mick van Dijke
     */

    public void joinLobby(String gameUID) {
        fb.addPlayer(gameUID, Main.getKEY());
        //fb.startLobbyListener(gameUID);
    }


}