package models;

import javafx.application.Platform;
import observers.GameObservable;
import observers.GameObserver;
import services.FirebaseService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ChatBox implements GameObservable {

    private List<GameObserver> observers = new ArrayList<>();
    private ArrayList<String> updatedMessageArraylist = new ArrayList<>();
    private FirebaseService firebaseservice;
    private int updatetimeInSeconds = 2;

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
        notifyAllObservers();
    }


    public void startAutoUpdatingChat(){
        Runnable helloRunnable = new Runnable() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        notifyAllObservers();
                        System.out.println("Auto updated");
                    }
                });
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(helloRunnable, 0, updatetimeInSeconds, TimeUnit.SECONDS);
    }


    public void addSecondToUpdateTime(){
        updatetimeInSeconds++;
    }


    public void removeSecondToUpdateTime(){
        updatetimeInSeconds--;
    }


    public int getUpdateTime(){
        return updatetimeInSeconds;
    }


    @Override
    public void register(GameObserver observer){
        System.out.println(observer);
        observers.add(observer);
    }


    @Override
    public void notifyAllObservers(){
        for (GameObserver s : observers) {
            s.update(this);
        }
    }


    @Override
    public ArrayList<String> getArrayListWithMessages(){
        return updatedMessageArraylist;
    }


}
