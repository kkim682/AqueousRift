package model;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DataSnapshot;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class DataManager {

    private static final String DATABASE_URL = "https://aqueous-rift"
        + ".firebaseio.com/";
    private static DatabaseReference db;

    /**
     * Static method that should be called ONCE to connect with our database
     */
    public static void initializeFireBase() {
        // Initialize the app with a service account, granting admin privileges
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                .setServiceAccount(new FileInputStream("config.json"))
                .setDatabaseUrl(DATABASE_URL)
                .build();
            FirebaseApp.initializeApp(options);
        } catch (FileNotFoundException error) {
            System.out.println(error);
        }

        // The app only has access as defined in the Security Rules
        db = FirebaseDatabase
            .getInstance()
            .getReference();
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError e) {
                System.out.println(e);
            }
        });
    }

    /**
    * Sets the path to database
    * @param key The key to search in DB
    */
    public void setReference(String key) {
        db = db.child(key);
    }

    /**
    * Gets a databaseReference in our object tree
    * @param key Child key to obtain a reference to
    * @return databaseReference DB object to execute on
    */
    public static DatabaseReference getReference(String key) {
        return db.child(key);
    }
}
