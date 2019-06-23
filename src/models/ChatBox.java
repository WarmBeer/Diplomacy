package models;

import observers.ChatObservable;
import observers.ChatObserver;
import services.FirebaseService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Class that serves as model for the chatbox.
 * All input and output is being processed in this class.
 */

public class ChatBox implements ChatObservable {

    private List<ChatObserver> observers = new ArrayList<>();
    private ArrayList<String> updatedMessageArraylist = new ArrayList<>();
    private FirebaseService firebaseservice;

    public ChatBox(FirebaseService fb){
        firebaseservice = fb;
        notifyChatObservers();
    }


    public void makeNewChat(String GameUID){
        firebaseservice.makeChatInFirebase(GameUID);
    }

    public void listenToChat(String GemUID) {
        firebaseservice.listenToChat(GemUID, this);
    }

    public String makeNewMessage(String fromUID, String toPlayer, String message, String userName){
        String systemNameAndTimestamp = ("(" + (new SimpleDateFormat("HH:mm:ss").format(new Date())) + ") " + userName);
        String newMessage = fromUID + "_" + toPlayer + "_" + systemNameAndTimestamp + ": " + message;
        return newMessage;
    }

    public String makeFirstMessage(String userName){
        String systemMessage = ("System: Player  " + userName + "  joined the game!");
        return systemMessage;
    }


    private void updateArrayListWithMessages(String GameUID){
        try{
            updatedMessageArraylist = firebaseservice.getMessages(GameUID);
        }
        catch (ExecutionException EE){
            System.out.println("Chatbox model is kapot");
            EE.printStackTrace();
        }
        catch(InterruptedException IE){
            System.out.println("Chatbox model is kapot");
            IE.printStackTrace();
        }
    }


    public void addChatMessage(String fromUID, String toPlayer, String nieuwBericht, String userName, String GameUID) {
        String newMessage = makeNewMessage(fromUID, toPlayer, nieuwBericht, userName);
        firebaseservice.addMessage(newMessage, GameUID);
        //updateArrayListWithMessages(GameUID);
        //notifyChatObservers();
    }

    public void addFirstMessage(String userName, String GameUID) {
        String newMessage = makeFirstMessage(userName);
        firebaseservice.addMessage(newMessage, GameUID);
        updateArrayListWithMessages(GameUID);
        notifyChatObservers();
    }

    public void giveLimitWarning(){

    }

    @Override
    public void registerChatObserver(ChatObserver chatobserver) {
        observers.add(chatobserver);
    }

    @Override
    public void unregisterChatObserver(ChatObserver chatobserver) {
        observers.remove(chatobserver);
    }

    @Override
    public void notifyChatObservers() {
        for (ChatObserver s : observers) {
            s.update(this);
        }
    }

    @Override
    public ArrayList<String> getArrayListWithMessages() {
        return updatedMessageArraylist;
    }

    public void updateChat(ArrayList<String> messages) {
        this.updatedMessageArraylist = messages;
        this.notifyChatObservers();
    }
}