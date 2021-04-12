package com.pastoreli.organizze.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseConfig {

    private static FirebaseAuth authentication;
    private static DatabaseReference firebase;

    public static DatabaseReference getFirebaseDatabase () {
        if(firebase == null)
            firebase = FirebaseDatabase.getInstance().getReference();

        return firebase;
    }

    public static FirebaseAuth getFirebaseAuthentication () {
        if(authentication == null)
            authentication = FirebaseAuth.getInstance();

        return authentication;
    }

}
