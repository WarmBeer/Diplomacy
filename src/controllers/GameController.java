package controllers;

import models.ChatBox;
import observers.GameObserver;

public class GameController {

    private static GameController gameController;
    private ChatBox chatbox;

    private GameController(String gameID) {
        this.chatbox = new ChatBox(gameID);
    }


    // Singleton Pattern.
    // now we can call: SpelbordController.getInstance()  from everywhere
    // AND it guarantees there is only 1 instance.
    public static GameController getInstance(String gameID) {
        if (gameController == null) {
            gameController = new GameController(gameID);
        }
        return gameController;
    }


    public void createChat(){
        chatbox.makeChat();
    }


    public void startUpdatingChat(){
        chatbox.startAutoUpdatingChat();
    }


    public void registerObserver(GameObserver sbv) {
        System.out.println(sbv);
        chatbox.register(sbv);
    }


    public void addMessage(String message, String userName){
        chatbox.addChatMessage(message, userName);
    }


}
