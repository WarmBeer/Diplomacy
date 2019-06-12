package services;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Class that sends an recieves data from/to firebase
 */

public class FirebaseService {

    private static FirebaseService firebaseservice;
    private final String ARRAYNAME = "messageArray";
    private final String CHILDPATH = "Chatbox";
    private final String FIRSTMESSAGE = "System: Welkom bij Diplomacy!";
    private String gameID;
    private Firestore db;


    public FirebaseService(String gameID){
        this.gameID = gameID;
        makeFirebaseConnection();
    }


    /**
     * Creates instance of firebaseservice with Singleton Pattern.
     * We can call SpelbordController.getInstance() from everywhere
     * And there is only 1 instance.
     * @param gameID Game ID as String.
     * @return A instance of the class FirebaseService.
     * @author Thomas Zijl
     */
    public static FirebaseService getInstance(String gameID) {
        if (firebaseservice == null) {
            firebaseservice = new FirebaseService(gameID);
        }
        return firebaseservice;
    }


    private void makeFirebaseConnection(){
        try{
            InputStream serviceAccount = new FileInputStream("src/resources/iipsen-database-firebase-adminsdk-rpnmc-2910ea15ab.json");
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(credentials).build();
            FirebaseApp.initializeApp(options);
            db = FirestoreClient.getFirestore();
        }
        catch(IOException IOE){
            IOE.printStackTrace();
        }
    }


    /**
     * Makes a document is firebase for this chat/game session.
     * @author Thomas Zijl
     */
    public void makeChatInFirebase(){
        try{
            Map<String, Object> chatMap = new HashMap<>();

            ArrayList<Object> messageArray = new ArrayList<>();
            Collections.addAll(messageArray, FIRSTMESSAGE);
            chatMap.put(ARRAYNAME, messageArray);

            // Add a new document in collection gameID with id childpath
            ApiFuture<WriteResult> future = db.collection(gameID).document(CHILDPATH).set(chatMap);

            //Console update
            System.out.println("Save location made. - time : " + future.get().getUpdateTime());

        }
        catch(ExecutionException EE){
            System.out.println("In de firebaseservice is een Excecution Exception opgetreden!");
            EE.printStackTrace();
        }
        catch(InterruptedException IE){
            System.out.println("In de firebaseservice is een Interrupted Exception opgetreden!");
            IE.printStackTrace();
        }
    }


    /**
     * Get all messages as an arraylist from the firebase document
     * @author Thomas Zijl
     */
    public ArrayList<String> getMessages() throws ExecutionException, InterruptedException {
        //Get right document from firebase
        DocumentReference docRef = db.collection(gameID).document(CHILDPATH);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        //Get the hashmap
        Map <String, Object> messagesInHashmap = document.getData();

        //Get the arraylist as object from the hastmap
        Object messagesAsObject = messagesInHashmap.get(ARRAYNAME);

        //Convert the object to een arraylist
        ArrayList <String> messagesInArraylist = ((ArrayList<String>) messagesAsObject);

        return messagesInArraylist;
    }


    /**
     * Add a string to the array of messages in firebase.
     * @param newMessage Message + time stamp as String.
     * @author Thomas Zijl
     */
    public void addMessage(String newMessage){
        try{
            DocumentReference chatbox = db.collection(gameID).document(CHILDPATH);
            ApiFuture<WriteResult> writeResult = chatbox.update(ARRAYNAME, FieldValue.arrayUnion(newMessage));
            System.out.println("Message send. - time : " + writeResult.get());
        }
        catch(ExecutionException EE){
            System.out.println("In de firebaseservice is een Excecution Exception opgetreden!");
            EE.printStackTrace();
        }
        catch(InterruptedException IE){
            System.out.println("In de firebaseservice is een Interrupted Exception opgetreden!");
            IE.printStackTrace();
        }
    }
}
