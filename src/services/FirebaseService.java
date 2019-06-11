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

    // Singleton Pattern.
    // now we can call: SpelbordController.getInstance()  from everywhere
    // AND it guarantees there is only 1 instance.
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


    public void makeChatInFirebase(){

        try{
            //Make root hashmap
            Map<String, Object> chatMap = new HashMap<>();

            ArrayList<Object> messageArray = new ArrayList<>();
            Collections.addAll(messageArray, FIRSTMESSAGE);
            chatMap.put(ARRAYNAME, messageArray);

            // Add a new document (asynchronously) in collection gameID with id childpath
            ApiFuture<WriteResult> future = db.collection(gameID).document(CHILDPATH).set(chatMap);

            //Console update
            System.out.println("Save location made. - time : " + future.get().getUpdateTime());

        }
        catch(ExecutionException EE){
            EE.printStackTrace();

        }
        catch(InterruptedException IE){
            IE.printStackTrace();

        }
    }


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


    public void addMessage(String message){
        try{
            DocumentReference chatbox = db.collection(gameID).document(CHILDPATH);

            // Atomically add a new region to the "regions" array field.
            ApiFuture<WriteResult> writeResult = chatbox.update(ARRAYNAME, FieldValue.arrayUnion(message));
            System.out.println("Message send. - time : " + writeResult.get());
        }
            catch(ExecutionException EE){
            EE.printStackTrace();
        }
            catch(InterruptedException IE){
            IE.printStackTrace();
        }
    }
}
