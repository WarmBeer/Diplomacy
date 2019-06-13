package controllers;

import models.ChatBox;
import observers.GameObserver;

/**
 * Class that serves as router between the views and models.
 * This class gives the inputs to the right models.
 */

public class GameController {

    private static GameController gameController;
    private ChatBox chatbox;


    private GameController(String gameID) {
        this.chatbox = new ChatBox(gameID);
    }


    /**
     * Singleton Pattern.
     * We can call SpelbordController.getInstance() from everywhere
     * And there is only 1 instance.
     * @param gameID
     * @return A instance of the class FirebaseService.
     * @author Thomas Zijl
     */
    public static GameController getInstance(String gameID) {
        if (gameController == null) {
            gameController = new GameController(gameID);
        }
        return gameController;
    }


    /**
     * Tell the chatbox model that there needs a chat savespace beging created.
     * @author Thomas Zijl
     */
    public void createChat(){
        chatbox.makeChat();
    }


    /**
     * Tell the chatbox model that autoupdating must be stated.
     * @author Thomas Zijl
     */
    public void startUpdatingChat(){
        chatbox.startAutoUpdatingChat();
    }


    /**
     * Register the GameView as observer in the chatbox.
     * @param sbv The instance of Chatbox as interface Gameobserver.
     * @author Thomas Zijl
     */
    public void registerObserver(GameObserver sbv) {
        System.out.println(sbv);
        chatbox.register(sbv);
    }


    /**
     * Tells the Chatboxmodel to add a chat message in firebase.
     * @param message Message of the player as String.
     * @param userName Username of player as String.
     * @author Thomas Zijl
     */
    public void addMessage(String message, String userName){
        chatbox.addChatMessage(message, userName);
    }

}