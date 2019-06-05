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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FirebaseService {

    public Firestore testFB(){

        try{
            InputStream serviceAccount = new FileInputStream("src/resources/iipsen-database-firebase-adminsdk-rpnmc-2910ea15ab.json");
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(credentials)
                    .build();
            FirebaseApp.initializeApp(options);
            Firestore db = FirestoreClient.getFirestore();

            return db;
        }
        catch (IOException IOE){
            IOE.printStackTrace();
            Firestore db = null;
            System.out.print("NIET GOED! NIET GOED! NIET GOED! NIET GOED! NIET GOED! NIET GOED!");
            System.exit(0);
            return db;
        }
    }

    public void schrijfTestData(Firestore db){

        try{
            DocumentReference docRef = db.collection("Producten").document("Lijsten");
            // Add document data  with id "alovelace" using a hashmap
            Map<String, Object> data = new HashMap<>();
            data.put("12233", "14,95");
            data.put("23321", "9,95");
            data.put("23123", "19,95");
            //asynchronously write data
            ApiFuture<WriteResult> result = docRef.set(data);
            // ...
            // result.get() blocks on response
            System.out.println("Update time : " + result.get().getUpdateTime());
            System.out.println("Schrijftest gelukt!");
        }

        catch(ExecutionException EE){
            EE.printStackTrace();

        }
        catch(InterruptedException IE){
            IE.printStackTrace();

        }
    }

    public void leesEnPrintTestData(Firestore db){
        try{
            // asynchronously retrieve all users
            ApiFuture<QuerySnapshot> query = db.collection("Producten").get();

            // query.get() blocks on response
            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                System.out.println("Producten: " + document.getId());
                System.out.println("Lijst: " + document.getString("12233"));
                System.out.println("Lijst: " + document.getString("23321"));
                System.out.println("Lijst: " + document.getString("23123"));
                System.out.println("Leestest gelukt!");
            }

        }
        catch(ExecutionException EE){
            EE.printStackTrace();
        }
        catch(InterruptedException IE){
            IE.printStackTrace();
        }


    }
}
