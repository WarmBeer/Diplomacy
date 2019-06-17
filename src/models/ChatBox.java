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
    }


    public void makeChat(String GameUID){
        firebaseservice.makeChatInFirebase(GameUID);
    }


    private String makeNewMessage(String message, String userName){
        String systemNameAndTimestamp = ("(" + (new SimpleDateFormat("HH:mm:ss").format(new Date())) + ") " + userName);
        String newMessage = systemNameAndTimestamp + ": " + message;
        return newMessage;
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


    public void addChatMessage(String nieuwBericht, String userName, String GameUID) {
        String newMessage = makeNewMessage(nieuwBericht, userName);
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

}