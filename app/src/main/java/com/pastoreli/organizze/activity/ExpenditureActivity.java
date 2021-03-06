package com.pastoreli.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pastoreli.organizze.R;
import com.pastoreli.organizze.config.FirebaseConfig;
import com.pastoreli.organizze.controller.TransactionController;
import com.pastoreli.organizze.controller.UserController;
import com.pastoreli.organizze.helper.CustomBase64;
import com.pastoreli.organizze.helper.DateUtil;
import com.pastoreli.organizze.model.Transaction;
import com.pastoreli.organizze.model.User;

public class ExpenditureActivity extends AppCompatActivity {

    private EditText editAmount;
    private TextInputEditText editDate, editCategory, editDescription;
    private FloatingActionButton fabSave;

    private Transaction transaction;

    private UserController userController = new UserController();
    private TransactionController transactionController = new TransactionController();

    private double totalExpenditure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenditure);

        editAmount = findViewById(R.id.editAmount);
        editDate = findViewById(R.id.editDate);
        editCategory = findViewById(R.id.editCategory);
        editDescription = findViewById(R.id.editDescription);
        fabSave = findViewById(R.id.fabSave);

        editDate.setText(DateUtil.currentDate());

        recoverTotalExpenditure();

    }

    public void recoverTotalExpenditure () {
        DatabaseReference userRef = userController.getUser();

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                totalExpenditure = user.getTotalExpenditure();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void saveExpenditure (View view) {

        if( formValidation() ) {

            double amount = Double.parseDouble(editAmount.getText().toString());

            transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setCategory(editCategory.getText().toString());
            transaction.setDescription(editDescription.getText().toString());
            transaction.setDate(editDate.getText().toString());
            transaction.setType("e");

            transactionController.updateExpenditure(totalExpenditure + amount);

            transactionController.registerTransaction(transaction);

            finish();

        }

    }

    public boolean formValidation () {
        String amount = editAmount.getText().toString();
        String date = editDate.getText().toString();
        String category = editCategory.getText().toString();
        String description = editDescription.getText().toString();

        if(amount.isEmpty()) {
            showToast("Valor n??o foi preenchido!");
            return false;
        }
        if(date.isEmpty()) {
            showToast("Data n??o foi preenchida!");
            return false;
        }
        if(category.isEmpty()) {
            showToast("Categaoria n??o foi preenchida!");
            return false;
        }
        if(description.isEmpty()) {
            showToast("Descri????o n??o foi preenchida!");
            return false;
        }

        return true;

    }

    public void showToast (String text) {
        Toast.makeText(
                ExpenditureActivity.this,
                text,
                Toast.LENGTH_SHORT
        ).show();
    }

}