package services;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class FirebaseService {

    private final String ARRAYNAME = "messageArray";
    private final String CHILDPATH = "Chatbox";
    private final String FIRSTMESSAGE = "System: Welkom bij Diplomacy!";
    private String gameName;


    public Firestore makeFirebaseConnection(String gameName) throws IOException {
        //initialiseer playernaam en gamenaam
        this.gameName = gameName;

        //Inisaliseer en autoriseer cloud firestore connectie
        InputStream serviceAccount = new FileInputStream("src/resources/iipsen-database-firebase-adminsdk-rpnmc-2910ea15ab.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
        FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(credentials).build();
        FirebaseApp.initializeApp(options);
        Firestore db = FirestoreClient.getFirestore();

        return db;
    }

    public void makeSaveLocationChat(Firestore db){

        try{
            //Make root hashmap
            Map<String, Object> chatMap = new HashMap<>();

            ArrayList<Object> messageArray = new ArrayList<>();
            Collections.addAll(messageArray, FIRSTMESSAGE);
            chatMap.put(ARRAYNAME, messageArray);

            // Add a new document (asynchronously) in collection gameName with id childpath
            ApiFuture<WriteResult> future = db.collection(gameName).document(CHILDPATH).set(chatMap);

            //Console update
            System.out.println("Update time : " + future.get().getUpdateTime());

        }

        catch(ExecutionException EE){
            EE.printStackTrace();

        }
        catch(InterruptedException IE){
            IE.printStackTrace();

        }
    }


    public ArrayList<String> getData(Firestore db) throws ExecutionException, InterruptedException {
        //Get right document from firebase
        DocumentReference docRef = db.collection(gameName).document(CHILDPATH);
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


    public void addMessageToChat(Firestore db, String message){
        try{
            DocumentReference chatbox = db.collection(gameName).document(CHILDPATH);

            // Atomically add a new region to the "regions" array field.
            ApiFuture<WriteResult> writeResult = chatbox.update(ARRAYNAME, FieldValue.arrayUnion(message));
            System.out.println("Update time : " + writeResult.get());
        }
            catch(ExecutionException EE){
            EE.printStackTrace();
        }
            catch(InterruptedException IE){
            IE.printStackTrace();
        }
    }
}
