package services;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.cloud.firestore.EventListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.annotations.Nullable;
import domains.GameJSON;

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
    private Firestore db;


    public FirebaseService() {
        makeFirebaseConnection();
    }


    /**
     * Creates instance of firebaseservice with Singleton Pattern.
     * We can call FirebaseService.getInstance() from everywhere
     * And there is only 1 instance.
     *
     * @param gameID Game ID as String.
     * @return A instance of the class FirebaseService.
     * @author Thomas Zijl
     */
    public static FirebaseService getInstance() {
        if (firebaseservice == null) {
            firebaseservice = new FirebaseService();
            return firebaseservice;
        }
        else{
            return firebaseservice;
        }
    }


    private void makeFirebaseConnection() {
        try {
            InputStream serviceAccount = new FileInputStream("src/resources/iipsen-database-firebase-adminsdk-rpnmc-2910ea15ab.json");
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(credentials).build();
            FirebaseApp.initializeApp(options);
            db = FirestoreClient.getFirestore();

        } catch (IOException IOE) {
            IOE.printStackTrace();
        }
    }


    /**
     * Makes a document is firebase for this chat/game session.
     * @author Thomas Zijl
     */
    public void makeChatInFirebase(String GameUID) {
        try {
            Map<String, Object> chatMap = new HashMap();

            ArrayList<Object> messageArray = new ArrayList();
            Collections.addAll(messageArray, FIRSTMESSAGE);
            chatMap.put(ARRAYNAME, messageArray);

            // Add a new document in collection gameID with id childpath
            ApiFuture<WriteResult> future = db.collection("Chats").document(GameUID).set(chatMap);

            //Console update
            System.out.println("Save location made. - time : " + future.get().getUpdateTime());
            db.collection("Chats").document("11111111");

        } catch (ExecutionException EE) {
            System.out.println("In de firebaseservice is een Excecution Exception opgetreden!");
            EE.printStackTrace();
        } catch (InterruptedException IE) {
            System.out.println("In de firebaseservice is een Interrupted Exception opgetreden!");
            IE.printStackTrace();
        }
    }


    /**
     * Get all messages as an arraylist from the firebase document
     *
     * @author Thomas Zijl
     */
    public ArrayList<String> getMessages(String GameUID) throws ExecutionException, InterruptedException {
        //Get right document from firebase
        DocumentReference docRef = db.collection("Chats").document(GameUID);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        //Get the hashmap
        Map<String, Object> messagesInHashmap = document.getData();

        //Get the arraylist as object from the hastmap
        Object messagesAsObject = messagesInHashmap.get(ARRAYNAME);

        //Convert the object to een arraylist
        ArrayList<String> messagesInArraylist = ((ArrayList<String>) messagesAsObject);

        return messagesInArraylist;
    }


    /**
     * Add a string to the array of messages in firebase.
     *
     * @param newMessage Message + time stamp as String.
     * @author Thomas Zijl
     */
    public void addMessage(String newMessage, String GameUID) {
        try {
            DocumentReference chatbox = db.collection("Chats").document(GameUID);
            ApiFuture<WriteResult> writeResult = chatbox.update(ARRAYNAME, FieldValue.arrayUnion(newMessage));
            System.out.println("Message send. - time : " + writeResult.get());
        } catch (ExecutionException EE) {
            System.out.println("In de firebaseservice is een Excecution Exception opgetreden!");
            EE.printStackTrace();
        } catch (InterruptedException IE) {
            System.out.println("In de firebaseservice is een Interrupted Exception opgetreden!");
            IE.printStackTrace();
        }
    }

    public void saveGame(GameJSON gameJSON) {
        try {
            ApiFuture<WriteResult> future = db.collection("Games").document(gameJSON.gameUID).set(gameJSON);
        } catch (Exception e) {
            System.out.println("In de firebaseservice is een Excecution Exception opgetreden!");
            e.printStackTrace();
        }
    }


    /**
     * Geeft een update naar de meegeleverde controller
     * op het moment dat er een wijziging in het firebase document plaatsvindt.
     */
    private void listen(String GameUID) {

        DocumentReference chatbox = db.collection("Chats").document(GameUID);
        chatbox.addSnapshotListener(new EventListener<DocumentSnapshot>() {

            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirestoreException e) {
                System.out.println("Update chat");
                //Notify all observers in chat?
            }
        });
    }


    public ArrayList<String> getGameIDs() {
        ArrayList<String> gameNames = new ArrayList<>();

        try{
            CollectionReference ref = db.collection("Games");
            ApiFuture<QuerySnapshot> futures = ref.get();

            int documentCount = futures.get().getDocuments().size();

            for(int i = 0; i < documentCount; i++){
                gameNames.add(futures.get().getDocuments().get(i).getId().toString());
                System.out.println(gameNames.get(i));
            }
        }
        catch (InterruptedException IE){
            IE.printStackTrace();
            System.out.println("Iets fout in firebase service...");
        }
        catch (ExecutionException EE){
            EE.printStackTrace();
            System.out.println("Iets fout in firebase service...");
        }

        return gameNames;
    }
}
