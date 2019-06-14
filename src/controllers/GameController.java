package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import domains.GameJSON;
import models.ChatBox;
import models.GameModel;
import observers.ChatObserver;
import observers.OrderObserver;
import views.GameView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;

public class GameController  {

    private GameModel gamemodel = new GameModel();
    private ChatBox chatbox;

    public GameController(String gameID){
        this.chatbox = new ChatBox(gameID);
        gamemodel = new GameModel();
    }

    public void loadGameFromJSON() {
        Reader reader = new BufferedReader(new InputStreamReader(
                this.getClass().getResourceAsStream("/" + "Diplomacy.json")));
        Gson gson = new GsonBuilder().create();
        GameJSON gameJSON = gson.fromJson(reader, GameJSON.class);

        gamemodel.initGame(gameJSON);
    }

    public void requestLoadGame(){
        try{
            gamemodel.loadGame();
        }
        catch(Exception E){
            System.out.println("Exception while request to load a game");
            E.printStackTrace();
        }
    }

    public void registerOrderObserver(OrderObserver orderObserver){
        gamemodel.registerOrderObserver(orderObserver);
    }

    public void registerChatObserver(ChatObserver chatObserver){
        chatbox.registerChatObserver(chatObserver);
    }

    public void addOrderIsClicked(String action, String prov1, String prov2){
        gamemodel.addOrder(action, prov1, prov2);
    }

    public void createChat(){
        chatbox.makeChat();
    }

    public void startUpdatingChat(){
        chatbox.startAutoUpdatingChat();
    }

    public void addMessage(String message, String userName){
        chatbox.addChatMessage(message, userName);
    }

    public void registerGameObserver(GameView gameView) {
        gamemodel.registerGameViewObserver(gameView);
    }
}