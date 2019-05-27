package app.service;



        import java.io.FileInputStream;
        import java.io.FileNotFoundException;
        import java.io.IOException;
        import java.io.InputStream;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;
        import java.util.concurrent.ExecutionException;


public class FireBaseLel {

    public static void startFirebase() throws IOException, InterruptedException, ExecutionException {

        System.out.println("Start firebase");

        /*

        import com.google.api.core.ApiFuture;
        import com.google.auth.oauth2.GoogleCredentials;
        import com.google.cloud.firestore.*;
        import com.google.firebase.FirebaseApp;
        import com.google.firebase.FirebaseOptions;
        import com.google.firebase.cloud.FirestoreClient;

        // Use a service account
        InputStream serviceAccount = new FileInputStream("source/iipsen-database-firebase-adminsdk-rpnmc-85360e6053.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(credentials)
                .build();
        FirebaseApp.initializeApp(options);

        Firestore db = FirestoreClient.getFirestore();

        //Add data
        DocumentReference docRef = db.collection("users").document("alovelace");
        // Add document data  with id "alovelace" using a hashmap
        Map<String, Object> data = new HashMap<>();
        data.put("first", "Ada");
        data.put("last", "Lovelace");
        data.put("born", 1815);
        //asynchronously write data
        ApiFuture<WriteResult> result = docRef.set(data);
        // ...
        // result.get() blocks on response
        System.out.println("Update time : " + result.get().getUpdateTime());

        //add More data
        docRef = db.collection("users").document("aturing");
        // Add document data with an additional field ("middle")
        data = new HashMap<>();
        data.put("first", "Alan");
        data.put("middle", "Mathison");
        data.put("last", "Turing");
        data.put("born", 1912);

        result = docRef.set(data);
        System.out.println("Update time : " + result.get().getUpdateTime());

        //read data
        // asynchronously retrieve all users
        ApiFuture<QuerySnapshot> query = db.collection("users").get();
        // ...

        // query.get() blocks on response
        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            System.out.println("User: " + document.getId());
            System.out.println("First: " + document.getString("first"));
            if (document.contains("middle")) {
                System.out.println("Middle: " + document.getString("middle"));
            }
            System.out.println("Last: " + document.getString("last"));
            System.out.println("Born: " + document.getLong("born"));
        }

         */

        System.out.println("Niet Gelukt, facking cool");

    }
}
