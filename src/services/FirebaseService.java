package services;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.cloud.firestore.EventListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.annotations.Nullable;
import controllers.GameController;
import domains.GameJSON;
import domains.Player;
import domains.Province;
import domains.Unit;
import javafx.application.Platform;
import models.ChatBox;
import models.GameModel;
import models.SuperModel;

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
    private ArrayList<Map> players;
    private String getGameName;
    private int getGameTurnTime;
    private int getGameTurn;
    private ArrayList<String> countrynames = new ArrayList<>();
    private final String PLAYERDOCUMENT = "allPlayers";
    private ListenerRegistration registration = null;
    private ListenerRegistration lobbyListener = null;
    private ListenerRegistration chatBoxListener;

    public FirebaseService() {
        makeFirebaseConnection();
    }


    /**
     * Creates instance of firebaseservice with Singleton Pattern.
     * We can call FirebaseService.getInstance() from everywhere
     * And there is only 1 instance.
     *
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
     *  from a DocumentSnapshot, get all messages as an arraylist
     *
     * @author Thomas Zijl
     *
     * @param document
     * @return ArrayList<String>
     */
    public ArrayList<String> getMessagesFromDocument(DocumentSnapshot document) {
        //Get the hashmap
        Map<String, Object> messagesInHashmap = document.getData();

        //Get the arraylist as object from the hastmap
        Object messagesAsObject = messagesInHashmap.get(ARRAYNAME);

        //Convert the object to een arraylist
        ArrayList<String> messagesInArraylist = ((ArrayList<String>) messagesAsObject);
        return messagesInArraylist;
    }


    /**
     * Get firebase messages document
     *
     * @author Thomas Zijl
     */
    public ArrayList<String> getMessages(String GameUID) throws ExecutionException, InterruptedException {
        //Get right document from firebase
        DocumentReference docRef = db.collection("Chats").document(GameUID);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        return getMessagesFromDocument(document);
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

    public GameJSON getGame(String gameUID){
        GameJSON gameJSON = null;
        try{
            DocumentReference docRef = db.collection("Games").document(gameUID);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();
            gameJSON = document.toObject(GameJSON.class);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return gameJSON;
    }

    public void sendOrders(String gameUID, List<Unit> units) {
        try {
            GameJSON gameJSON = getGame(gameUID);
            for (Unit unit : units){
                for (int i = 0; i < gameJSON.Provinces.size(); i++) {
                    if (gameJSON.Provinces.get(i).abbr.equals(unit.getProvince().getAbbreviation())) {
                        Province province = unit.getTargetProvince();
                        gameJSON.Provinces.get(i).stationed.orderTarget = province.getAbbreviation();
                        gameJSON.Provinces.get(i).stationed.orderType = unit.getCurrentOrder();
                    }
                }
            }
            saveGame(gameJSON);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addPlayerInFirebase(Player newPlayer, String gameUID) {
        try {
            GameJSON gameJSON = getGame(gameUID);
            gameJSON.Players.add(newPlayer);
            saveGame(gameJSON);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Geeft een update naar de meegeleverde controller
     * op het moment dat er een wijziging in het firebase document plaatsvindt.
     */
    public void listenToChat(String GameUID, ChatBox chatBox) {

        DocumentReference chatbox = db.collection("Chats").document(GameUID);
        if(chatBoxListener != null)
            chatBoxListener.remove();
        chatBoxListener = chatbox.addSnapshotListener(new EventListener<DocumentSnapshot>() {

            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirestoreException e) {
                Platform.runLater(() -> chatBox.updateChat(getMessagesFromDocument(snapshot)));
            }
        });
    }

    public void setGameListener(String gameUID, GameController gameController) {
        DocumentReference games = db.collection("Games").document(gameUID);
        if(registration != null)
            registration.remove();
        registration = games.addSnapshotListener((snapshot, e) -> {
            Platform.runLater(() -> {
                GameJSON gameJSON = snapshot.toObject(GameJSON.class);
                gameController.gameFirebaseUpdated(gameJSON, e);
            });

        });
    }

    public void startLobbyListener(String gameUID, SuperModel superModel) {
        DocumentReference games = db.collection("Games").document(gameUID);
        lobbyListener = games.addSnapshotListener((snapshot, e) -> {
            GameJSON gameJSON = snapshot.toObject(GameJSON.class);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    superModel.onLobbyEvent(gameJSON);
                }
            });
        });
    }

    public void stopLobbyListener() {
        lobbyListener = null;
    }


    public ArrayList<String> getGameName() {
        ArrayList<String> gameNames = new ArrayList<>();

        try{
            CollectionReference ref = db.collection("Games");
            ApiFuture<QuerySnapshot> futures = ref.get();

            int documentCount = futures.get().getDocuments().size();

            for(int i = 0; i < documentCount; i++){
                gameNames.add("Game " + (i + 1) +": " + futures.get().getDocuments().get(i).getData().get("name") + " - ID: " + futures.get().getDocuments().get(i).getId());
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

    public ArrayList<String> getActivePlayerUIDS(String gameUID) {
        GameJSON gameJSON = getGame(gameUID);
        ArrayList<String> activePlayerUIDs = new ArrayList<>();

        for(int index = 0; index < gameJSON.Players.size(); index++){
            String userUID = gameJSON.Players.get(index).getUID();
            activePlayerUIDs.add(userUID);
        }

        return activePlayerUIDs;
    }

    public ArrayList<String> getActivePlayerNames(String gameUID) {
        GameJSON gameJSON = getGame(gameUID);
        ArrayList<String> activePlayerNames = new ArrayList<>();

        for(int index = 0; index < gameJSON.Players.size(); index++){
            String userName = gameJSON.Players.get(index).getUID();
            activePlayerNames.add(userName);
        }

        return activePlayerNames;
    }

    public ArrayList<String> getGameIDs() {
        ArrayList<String> gameIDs = new ArrayList<>();

        try{
            CollectionReference ref = db.collection("Games");
            ApiFuture<QuerySnapshot> futures = ref.get();

            int documentCount = futures.get().getDocuments().size();

            for(int i = 0; i < documentCount; i++){
                gameIDs.add(futures.get().getDocuments().get(i).getId());
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

        return gameIDs;
    }

    public List<Player> getPlayersFromFB(String gameUID){
        List<Player> players;
        GameJSON gameJSON = getGame(gameUID);
        players = gameJSON.Players;
        System.out.println(players);
        return players;
    }

    public ArrayList<String> getChoosenCountriesNames(String gameID){
        try{
            //Get right document from firebase
            DocumentReference docRef = db.collection("Games").document(gameID);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();

            ArrayList<Map> playerinfo = (ArrayList<Map>) document.getData().get("Players");
            countrynames = new ArrayList<>();

            for(int x = 0; x < playerinfo.size(); x++){
                String country = (String) playerinfo.get(x).get("country");
                countrynames.add(country);
            }

            return countrynames;
        }
        catch (InterruptedException IE){
            IE.printStackTrace();
            System.out.println("Iets fout in firebase service...");
            return countrynames;
        }
        catch (ExecutionException EE){
            EE.printStackTrace();
            System.out.println("Iets fout in firebase service...");
            return countrynames;
        }
    }




    public String getGameName(String gameUID){
        try{
            //Get right document from firebase
            DocumentReference docRef = db.collection("Games").document(gameUID);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();

            getGameName = (String)document.getData().get("name");

            return getGameName;
        }
        catch (InterruptedException IE){
            IE.printStackTrace();
            System.out.println("Iets fout in firebase service...");
            return getGameName;
        }
        catch (ExecutionException EE){
            EE.printStackTrace();
            System.out.println("Iets fout in firebase service...");
            return getGameName;
        }
    }

    public int getGameTurnTime(String gameUID){
        try{
            //Get right document from firebase
            DocumentReference docRef = db.collection("Games").document(gameUID);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();

            long getGameTurnTimeAsLong = (long) document.getData().get("turnTime");
            getGameTurnTime = Math.toIntExact(getGameTurnTimeAsLong);

            return getGameTurnTime;
        }
        catch (InterruptedException IE){
            IE.printStackTrace();
            System.out.println("Iets fout in firebase service...");
            return getGameTurnTime;
        }
        catch (ExecutionException EE){
            EE.printStackTrace();
            System.out.println("Iets fout in firebase service...");
            return getGameTurnTime;
        }
    }

    public int getGameTurn(String gameUID){
        try{
            //Get right document from firebase
            DocumentReference docRef = db.collection("Games").document(gameUID);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();

            long getGameTurnAsLong = (long) document.getData().get("turn");
            getGameTurn = Math.toIntExact(getGameTurnAsLong);

            return getGameTurn;
        }
        catch (InterruptedException IE){
            IE.printStackTrace();
            System.out.println("Iets fout in firebase service...");
            return getGameTurn;
        }
        catch (ExecutionException EE){
            EE.printStackTrace();
            System.out.println("Iets fout in firebase service...");
            return getGameTurn;
        }
    }

    public void addNewPlayerInFirebase(String playerUID,String playerName) {
        try {
            DocumentReference players = db.collection("Players").document(PLAYERDOCUMENT);
            players.get().get().getData().get("Players");
            HashMap<String, String> playerInformation = (HashMap) players.get().get().getData().get("Players");
            playerInformation.put(playerUID,playerName);

            ApiFuture<WriteResult> writeResult = players.update("Players", playerInformation);
            System.out.println("Player added send. - time : " + writeResult.get());
        } catch (ExecutionException EE) {
            System.out.println("In de firebaseservice is een Excecution Exception opgetreden!");
            EE.printStackTrace();
        } catch (InterruptedException IE) {
            System.out.println("In de firebaseservice is een Interrupted Exception opgetreden!");
            IE.printStackTrace();
        }
    }

    public String playerUIDtoPlayername(String playerUID) {

        String playerName = "";

        try{
            //Get right document from firebase
            DocumentReference docRef = db.collection("Players").document(PLAYERDOCUMENT);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();

            //Get the hashmap
            Map<String, Object> messagesInHashmap = document.getData();
            Map<String,String> playerNamesMap = (HashMap)messagesInHashmap.get("Players");
            playerName = (String) playerNamesMap.get(playerUID);
            System.out.println("PLAYERUID: " +playerUID + " AND PLAYERNAME: " + playerName);

            return playerName;
        } catch (ExecutionException EE) {
            System.out.println("In de firebaseservice is een Excecution Exception opgetreden!");
            EE.printStackTrace();
            return playerName;
        } catch (InterruptedException IE) {
            System.out.println("In de firebaseservice is een Interrupted Exception opgetreden!");
            IE.printStackTrace();
            return playerName;
        }


    }

    public void addPlayer(String gameUID, String playerKey) {
        try {
            GameJSON gameJSON = getGame(gameUID);
            Player player = new Player();
            player.setUID(playerKey);
            player.setId(gameJSON.Players.size());
            player.setName("Player " + player.getId() + " - " + playerUIDtoPlayername(playerKey));
            player.setCountry(gameJSON.freeCountries.get(0));
            gameJSON.Players.add(player);
            gameJSON.freeCountries.remove(0);
            saveGame(gameJSON);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
