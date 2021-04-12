package com.pastoreli.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pastoreli.organizze.R;
import com.pastoreli.organizze.controller.TransactionController;
import com.pastoreli.organizze.controller.UserController;
import com.pastoreli.organizze.helper.DateUtil;
import com.pastoreli.organizze.model.Transaction;
import com.pastoreli.organizze.model.User;

public class RevenueActivity extends AppCompatActivity {

    private EditText editAmount;
    private TextInputEditText editDate, editCategory, editDescription;
    private FloatingActionButton fabSave;

    private Transaction transaction;

    private UserController userController = new UserController();
    private TransactionController transactionController = new TransactionController();

    private double totalRevenue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue);

        editAmount = findViewById(R.id.editAmount);
        editDate = findViewById(R.id.editDate);
        editCategory = findViewById(R.id.editCategory);
        editDescription = findViewById(R.id.editDescription);
        fabSave = findViewById(R.id.fabSave);

        editDate.setText(DateUtil.currentDate());

        recoverTotalRevenue();

    }

    public void recoverTotalRevenue () {
        DatabaseReference userRef = userController.getUser();

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                totalRevenue = user.getTotalRevenue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void saveRevenue (View view) {

        if( formValidation() ) {

            double amount = Double.parseDouble(editAmount.getText().toString());

            transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setCategory(editCategory.getText().toString());
            transaction.setDescription(editDescription.getText().toString());
            transaction.setDate(editDate.getText().toString());
            transaction.setType("r");

            transactionController.updateRevenue(totalRevenue + amount);

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
            showToast("Valor não foi preenchido!");
            return false;
        }
        if(date.isEmpty()) {
            showToast("Data não foi preenchida!");
            return false;
        }
        if(category.isEmpty()) {
            showToast("Categaoria não foi preenchida!");
            return false;
        }
        if(description.isEmpty()) {
            showToast("Descrição não foi preenchida!");
            return false;
        }

        return true;

    }

    public void showToast (String text) {
        Toast.makeText(
                RevenueActivity.this,
                text,
                Toast.LENGTH_SHORT
        ).show();
    }

}