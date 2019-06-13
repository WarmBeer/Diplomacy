package models;

import observers.GameObservable;
import observers.GameObserver;
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

public class ChatBox implements GameObservable {

    private List<GameObserver> observers = new ArrayList<>();
    private ArrayList<String> updatedMessageArraylist = new ArrayList<>();
    private FirebaseService firebaseservice;
    private final int UPDATETIMEINSECONDS = 2;

    public ChatBox(String gameID){
        firebaseservice = firebaseservice.getInstance(gameID);
    }


    /**
     * Give the firebase service a signal make a save location for the chat
     * @author Thomas Zijl
     */
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


    /**
     * Gives firebase order to save a new messages.
     * @param nieuwBericht New message as String.
     * @param userName Username from sender as String.
     * @author Thomas Zijl
     * @version V1 (12-6-2019)
     */
    public void addChatMessage(String nieuwBericht, String userName) {
        String newMessage = makeNewMessage(nieuwBericht, userName);
        firebaseservice.addMessage(newMessage);
        updateArrayListWithMessages();
        notifyAllObservers();
    }


    /**
     * Makes a threat that automatically adds recieved messages to the chat.
     * @author Thomas Zijl
     * @version V1 (12-6-2019)
     */
    public void startAutoUpdatingChat(){
        System.out.println("Autoupdate niet gestart. Check chatbox.java voor meer info.");
//        Niet gestart omdat er anders steeds een threat loopt, en dat is niet nice met testen
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


    /**
     * Registers (adds) the given observer in an arraylist.
     * So it can be stored there, for when observers need to be notified.
     * @author Thomas Zijl
     */
    @Override
    public void register(GameObserver observer){
        System.out.println(observer);
        observers.add(observer);
    }


    /**
     * When something is change, this methode is called to notify registerd observers.
     * @author Thomas Zijl
     */
    @Override
    public void notifyAllObservers(){
        for (GameObserver s : observers) {
            s.update(this);
        }
    }


    /**
     * A observer can get the arraylist with messages when notified.
     * @author Thomas Zijl
     */
    @Override
    public ArrayList<String> getArrayListWithMessages(){
        return updatedMessageArraylist;
    }


}
