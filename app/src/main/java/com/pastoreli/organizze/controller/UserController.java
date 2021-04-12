package com.pastoreli.organizze.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.pastoreli.organizze.config.FirebaseConfig;
import com.pastoreli.organizze.helper.CustomBase64;

public class UserController {

    private DatabaseReference firebaseRef = FirebaseConfig.getFirebaseDatabase();
    private FirebaseAuth authentication = FirebaseConfig.getFirebaseAuthentication();

    public DatabaseReference getUser () {
        String userEmail = authentication.getCurrentUser().getEmail();
        String idUser = CustomBase64.Base64Encode(userEmail);
        return firebaseRef.child("users").child(idUser);
    }

}
