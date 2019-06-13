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
    private final int UPDATETIMEINSECONDS = 2;

    public ChatBox(String gameID){
        firebaseservice = firebaseservice.getInstance(gameID);
    }


    public void makeChat(){
        firebaseservice.makeChatInFirebase();
    }


    private String makeNewMessage(String message, String userName){
        String systemNameAndTimestamp = ("(" + (new SimpleDateFormat("HH:mm:ss").format(new Date())) + ") " + userName);
        String newMessage = systemNameAndTimestamp + ": " + message;
        return newMessage;
    }


    private void updateArrayListWithMessages(){
        try{
            updatedMessageArraylist = firebaseservice.getMessages();
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


    public void addChatMessage(String nieuwBericht, String userName) {
        String newMessage = makeNewMessage(nieuwBericht, userName);
        firebaseservice.addMessage(newMessage);
        updateArrayListWithMessages();
        notifyChatObservers();
    }


    public void startAutoUpdatingChat(){
        //System.out.println("Autoupdate niet gestart. Check chatbox.java voor meer info.");
//        Runnable helloRunnable = new Runnable() {
//            public void run() {
//                Platform.runLater(new Runnable() {
//                    public void run() {
//                        updateArrayListWithMessages();
//                        notifyAllObservers();
//                    }
//                });
//            }
//        };
//
//        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
//        executor.scheduleAtFixedRate(helloRunnable, 0, UPDATETIMEINSECONDS, TimeUnit.SECONDS);
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