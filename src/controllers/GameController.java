package controllers;

import application.Main;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import domains.*;
import javafx.stage.Stage;
import models.ChatBox;
import models.GameModel;
import observers.ChatObserver;
import observers.OrderObserver;
import utilities.KeyHandler;
import views.GameView;

import java.io.*;
import java.util.ArrayList;

import static application.Main.print;

public class GameController  {

    private GameModel gameModel;
    private ChatBox chatbox;

    public GameController(Stage stage){
        createChat("11111111");
        this.gameModel = new GameModel(stage, this);
        gameModel.show();
    }

    public void createChat(String gameUID) {
        this.chatbox = new ChatBox(gameUID);
    }

    public GameJSON retrieveGameJSON(String gameUID) {
        Reader reader = new BufferedReader(new InputStreamReader(
                this.getClass().getResourceAsStream("/" + "Diplomacy.json")));
        Gson gson = new GsonBuilder().create();
        GameJSON gameJSON = gson.fromJson(reader, GameJSON.class);

        return gameJSON;
    }

    public void saveGameToJSON() {

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

        Gson gson = new GsonBuilder().setPrettyPrinting().create();;
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

        print(gameSave);
    }

    public void requestLoadGame(String gameUID){
        try{
            gameModel.initGame(retrieveGameJSON(gameUID));
        }
        catch(Exception E){
            System.out.println("Exception while request to load a game");
            E.printStackTrace();
        }
    }

    public void registerOrderObserver(OrderObserver orderObserver){
        gameModel.registerOrderObserver(orderObserver);
    }

    public void registerChatObserver(ChatObserver chatObserver){
        chatbox.registerChatObserver(chatObserver);
    }

    public void addOrderIsClicked(String action, String prov1, String prov2){
        gameModel.addOrder(action, prov1, prov2);
    }

    public void createChat(){
        chatbox.makeChat();
    }

    public void startUpdatingChat(){
        chatbox.startAutoUpdatingChat();
    }

    public void addMessage(String message){
        chatbox.addChatMessage(message, Main.getKEY());
    }

    public void registerGameObserver(GameView gameView) {
        gameModel.registerGameViewObserver(gameView);
    }
}