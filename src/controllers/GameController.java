package controllers;

import application.Main;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static application.Main.print;

public class GameController  {

    private GameModel gameModel;
    private ChatBox chatbox;
    public MainController mainController;
    private FirebaseService fb;
    private List<Unit> orderedUnits;

    public GameController(Stage stage){
        orderedUnits = new ArrayList<>();
        fb = new FirebaseService();
        this.chatbox = new ChatBox(fb);
        this.gameModel = new GameModel(stage, this);
    }

    public void saveToFirebase() {
        GameJSON gameJSON = saveGameToJSON();
        fb.saveGame(gameJSON);
    }

    public void addUnitOrder(Unit unit) {
        orderedUnits.add(unit);
    }

    private GameJSON retrieveGameJSON(String gameUID) {

        GameJSON gameJSON = fb.getGame(gameUID);

        if (gameJSON == null) {
            print("Something went wrong getting the game from FB...");
        }
        /*
        Reader reader = new BufferedReader(new InputStreamReader(
                this.getClass().getResourceAsStream("/" + "Diplomacy.json")));
        Gson gson = new GsonBuilder().create();
        GameJSON gameJSON = gson.fromJson(reader, GameJSON.class);
        */
        return gameJSON;
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

        for (Province province : gameModel.getActiveGame().getProvinces()) {
            ProvinceJSON provinceJSON = new ProvinceJSON();
            provinceJSON.abbr = province.getAbbreviation();
            provinceJSON.owner = province.getOwner().getName();

            UnitJSON unitJSON = new UnitJSON();

            if (province.getUnit() != null) {
                unitJSON.unitType = province.getUnit().getUnitType();
                unitJSON.orderType = (Unit.orderType) province.getUnit().getCurrentOrder().get("orderType");
                unitJSON.orderTarget = (String) province.getUnit().getCurrentOrder().get("orderTarget");
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
            GameJSON gameJSON = retrieveGameJSON(gameUID);
            if (gameJSON == null) {
                print("Oh no....");
            }
            print(gameJSON);
            gameModel.initGame(gameJSON);
            createChat();
        }
        catch(Exception E){
            System.out.println("Exception while request to load a game");
            E.printStackTrace();
        }
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
        chatbox.makeChat(gameModel.getActiveGame().getGameUID());
    }

//    public void startUpdatingChat(){
//        chatbox.startAutoUpdatingChat();
//    }

    public void addMessage(String message){
        chatbox.addChatMessage(message, Main.getKEY(), gameModel.getActiveGame().getGameUID());
        sendOrders();
    }

    public void sendOrders() {
        for (Unit unit : orderedUnits) {
            fb.sendOrders(gameModel.getActiveGame().getGameUID(), unit);
        }
    }

    public void registerGameObserver(GameView gameView) {
        gameModel.registerGameViewObserver(gameView);
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
            String orderType = orderSplit[0];
            String province1 = orderSplit[1];
            String province2 = "";
            if(orderSplit.length > 1) {
                province2 = orderSplit[2];
            }

            System.out.println("order type: " + orderType + " from "+province1 + " to " + province2);

        }
    }

    private Boolean provinceExists(Province province) {
        for (Object o : gameModel.getActiveGame().getProvinces()) {
            // use utility function from java.util to deal with nulls
            if (Objects.equals(o, province)) {
                return true;
            }
        }
        return false;
    }

    //TODO: Fix deze shit.

    private void processOrders() {
        List<Orders> orders = new ArrayList<>();
        List<Orders> holdOrders = new ArrayList<>();
        List<Orders> moveOrders = new ArrayList<>();
        Game processedGame = gameModel.getActiveGame();
        //processedGame.resetProvinces();
        for (int i = 0;i<gameModel.getActiveGame().getProvinces().size();i++) {
            if (gameModel.getActiveGame().getProvinces().get(i).getUnit() != null) {

            }
        }
    }
}