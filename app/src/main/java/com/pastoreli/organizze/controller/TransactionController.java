package com.pastoreli.organizze.controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.pastoreli.organizze.config.FirebaseConfig;
import com.pastoreli.organizze.helper.CustomBase64;
import com.pastoreli.organizze.helper.DateUtil;
import com.pastoreli.organizze.model.Transaction;

public class TransactionController {

    private DatabaseReference firebaseRef = FirebaseConfig.getFirebaseDatabase();
    private FirebaseAuth authentication = FirebaseConfig.getFirebaseAuthentication();

    public void updateRevenue (Double revenue) {
        UserController userController = new UserController();
        DatabaseReference userRef = userController.getUser();
        userRef.child("totalRevenue").setValue(revenue);
    }

    public void updateExpenditure (Double expenditure) {
        UserController userController = new UserController();
        DatabaseReference userRef = userController.getUser();
        userRef.child("totalExpenditure").setValue(expenditure);
    }

    public void registerTransaction (Transaction transaction) {
        String userEmail = authentication.getCurrentUser().getEmail();
        String idUser = CustomBase64.Base64Encode(userEmail);

        DatabaseReference firebase = FirebaseConfig.getFirebaseDatabase();
        firebase.child("transactions")
                .child(idUser)
                .child(DateUtil.monthYearDateHash(transaction.getDate()))
                .push()
                .setValue(transaction);
    }

}
