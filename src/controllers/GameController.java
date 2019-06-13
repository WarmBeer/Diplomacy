package controllers;

import models.ChatBox;
import models.GameModel;
import observers.ChatObserver;
import observers.OrderObserver;

public class GameController  {

    private GameModel gamemodel = new GameModel();
    private ChatBox chatbox;

    public GameController(String gameID){
        this.chatbox = new ChatBox(gameID);
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
}