package controllers;

import application.Main;
import com.google.cloud.firestore.DocumentSnapshot;
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

    public void createLobby(String gameName, int turnTime) {
        gameModel.createLobby(gameName, turnTime);
        chatbox.makeNewChat(gameModel.getActiveGame().getGameUID());
        Player player = new Player();
        player.setUID(Main.getKEY());
        player.setId(0);
        player.setName("Player " + player.getId());
        player.setCountry(GameModel.Countries.GERMANY);
        gameModel.getActiveGame().addPlayer(player);
        saveToFirebase();
        listenState = ListenState.LOBBY;
        fb.startLobbyListener(gameModel.getActiveGame().getGameUID(), mainController.getSuperModel());
    }

    public void gameFirebaseUpdated(GameJSON gameJSON, FirestoreException e) {
        System.out.println("FIREBASE GAME UPDATED");
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


    public GameJSON saveGameToJSON() {

        String gameSave = "";

        GameJSON gameJSON = new GameJSON();
        gameJSON.gameUID = gameModel.getActiveGame().getGameUID();
        gameJSON.host = Main.getKEY();
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

    public void requestLoadGame(String gameUID){
        try{
            gameModel.show();
            GameJSON gameJSON = retrieveGameJSON(gameUID);

            gameModel.initGame(gameJSON);
            chatbox.notifyChatObservers();
            //sendFirstMessage();

        }
        catch(Exception E){
            System.out.println("Exception while request to load a game");
            E.printStackTrace();
        }

        //gameModel.createUnitsPerPlayer();
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
            if(gameModel.getProvinceFromName(province1Name).getUnit() == null) {
                System.out.println("Unit is null! you added an order from a province without unit");
            } else {
                gameModel.getProvinceFromName(province1Name).getUnit().addOrder(order_type, gameModel.getProvinceFromName(province2Name));
                System.out.println(gameModel.getProvinceFromName(province1Name).getUnit().getCurrentOrder() + gameModel.getProvinceFromName(province2Name).getAbbreviation());
                addUnitOrder(gameModel.getProvinceFromName(province1Name).getUnit());
            }

        }
        sendOrders();
    }


    private void processOrders() {

        try {
            requestLoadGame(gameModel.getActiveGame().getGameUID());
        } finally {
//            for(Province province : gameModel.getProvinces()) {
//                if(province.getUnit() != null)
//                    continue;
//
//                for(Unit unit : gameModel.getActiveGame().getUnits()) {
//
//                    if(unit.province.getOwner().getLeader().getName().equals("Stefan")) {
//                        if(province.getCountry() == GameModel.Countries.FRANCE && Math.random() > 0.5){
//                            System.out.println(province.getName() + " unit: " + unit);
//                            province.addUnit(null);
//                            Country c = new Country(GameModel.Countries.INDEPENDENT);
//                            province.setOwner(c);
//                            }
//                    }
//                }
//            }

            orderedUnits = new ArrayList<>();
            for (int i = 0; i < gameModel.getActiveGame().getUnits().size(); i++) {
                orderedUnits.add(gameModel.getActiveGame().getUnits().get(i));
            }

            List<Unit> supportOrders = new ArrayList<>();
            List<Unit> moveOrders = new ArrayList<>();
            for (int i = 0; i < orderedUnits.size(); i++) {
                switch (orderedUnits.get(i).getCurrentOrder()) {
                    case SUPPORT:
                        supportOrders.add(orderedUnits.get(i));
                        break;
                    case MOVE:
                        moveOrders.add(orderedUnits.get(i));
                        break;
                }
            }

            for (int i = 0; i < supportOrders.size(); i++) {
                supportOrders.get(i).doOrder();
            }
            for (int i = 0; i < moveOrders.size(); i++) {
                moveOrders.get(i).doOrder();
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

    public void refresChat(){
        chatbox.notifyChatObservers();
    }

    public MainController getMainController() {
        return mainController;
    }

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

//    public void addPlayersAndCountriesLobby() {
//        gameModel.startLobby();
//    }

    public void joinLobby(String gameUID) {
        fb.addPlayer(gameUID, Main.getKEY());
        //fb.startLobbyListener(gameUID);
    }


}