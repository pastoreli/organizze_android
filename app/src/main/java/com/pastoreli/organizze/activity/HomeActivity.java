package com.pastoreli.organizze.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pastoreli.organizze.R;
import com.pastoreli.organizze.adapter.TransactionAdapter;
import com.pastoreli.organizze.config.FirebaseConfig;
import com.pastoreli.organizze.controller.UserController;
import com.pastoreli.organizze.helper.CustomBase64;
import com.pastoreli.organizze.model.Transaction;
import com.pastoreli.organizze.model.User;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;
    private TextView textWellcome, textBalance, textGeneralBalance;
    private RecyclerView recyclerTransactions;

    private TransactionAdapter adapterTransaction;

    private FirebaseAuth authentication;
    private DatabaseReference userRef;
    private ValueEventListener valueEventListenerUser;
    private ValueEventListener valueEventListenerTransactions;
    private DatabaseReference fireBaseRaf = FirebaseConfig.getFirebaseDatabase();
    private DatabaseReference transactionRef;

    private UserController userController = new UserController();

    private Double totalExpenditure = 0.0;
    private Double totalRevenue = 0.0;
    private Double userSummary = 0.0;

    private List<Transaction> transactionList = new ArrayList<>();
    private Transaction transationToDelete;
    private String selectedMonthYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        calendarView = findViewById(R.id.calendarView);
        textWellcome = findViewById(R.id.textWellcome);
        textBalance = findViewById(R.id.textBalane);
        textGeneralBalance = findViewById(R.id.textGeneralBalance);
        recyclerTransactions = findViewById(R.id.recyclerTransactions);

        calendarConfig();
        swipe();
        recyclerConfig();

    }

    @Override
    protected void onStart() {
        super.onStart();
        recoverSummary();
        recoverTransactions();
    }

    public void swipe () {

        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
               deleteTansaction(viewHolder);
            }
        };

        new ItemTouchHelper( itemTouch ).attachToRecyclerView( recyclerTransactions );

    }

    public void deleteTansaction (RecyclerView.ViewHolder viewHolder) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Excluir Movimentação da Conta");
        alertDialog.setMessage("Você tem certeza que deseja realmente excluir essa movimentação de sua conta?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder.getAdapterPosition();
                transationToDelete = transactionList.get(position);

                authentication = FirebaseConfig.getFirebaseAuthentication();
                String userEmail = authentication.getCurrentUser().getEmail();
                String idUser = CustomBase64.Base64Encode(userEmail);

                transactionRef = fireBaseRaf.child("transactions")
                        .child(idUser)
                        .child(selectedMonthYear);

                transactionRef.child( transationToDelete.getId() ).removeValue();
                adapterTransaction.notifyItemRemoved(position);

                updateBalance();

            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapterTransaction.notifyDataSetChanged();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();


    }

    public void updateBalance () {

        userRef = userController.getUser();

        if ( transationToDelete.getType().equals("r") ) {
            totalRevenue -= transationToDelete.getAmount();
            userRef.child("totalRevenue").setValue(totalRevenue);
        } else {
            totalExpenditure -= transationToDelete.getAmount();
            userRef.child("totalExpenditure").setValue(totalExpenditure);
        }
    }

    public void recoverTransactions () {
        authentication = FirebaseConfig.getFirebaseAuthentication();
        String userEmail = authentication.getCurrentUser().getEmail();
        String idUser = CustomBase64.Base64Encode(userEmail);

        transactionRef = fireBaseRaf.child("transactions")
                .child(idUser)
                .child(selectedMonthYear);

        Log.i("LISTAAA: ", selectedMonthYear);

        valueEventListenerTransactions = transactionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                transactionList.clear();
                for( DataSnapshot data : snapshot.getChildren() ) {
                    Transaction transaction = data.getValue(Transaction.class);
                    transaction.setId(data.getKey());
                    transactionList.add(transaction);
                }

                adapterTransaction.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Log.i("MES", "mes: " + selectedMonthYear);
    }

    public void recyclerConfig() {

        adapterTransaction =  new TransactionAdapter(transactionList, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerTransactions.setLayoutManager(layoutManager);
        recyclerTransactions.setHasFixedSize(true);
        recyclerTransactions.setAdapter(adapterTransaction);
    }

    public void calendarConfig () {
        CharSequence months[] = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
        calendarView.setTitleMonths(months);

        CalendarDay currentDate = calendarView.getCurrentDate();
        String selectedMonth = String.format("%02d", currentDate.getMonth());
        selectedMonthYear = selectedMonth + "" + currentDate.getYear();

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                String selectedMonth = String.format("%02d", date.getMonth());
                selectedMonthYear = selectedMonth + "" + date.getYear();

                transactionRef.removeEventListener( valueEventListenerTransactions );
                recoverTransactions();
            }
        });

    }

    public void recoverSummary () {
        userRef = userController.getUser();

        valueEventListenerUser = userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                totalRevenue = user.getTotalRevenue();
                totalExpenditure = user.getTotalExpenditure();
                userSummary = totalRevenue - totalExpenditure;

                DecimalFormat decimalFormat = new DecimalFormat("0.##");

                textWellcome.setText(user.getName());
                textBalance.setText("R$ " + decimalFormat.format(userSummary));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void addRevenue (View view) {
        startActivity(new Intent(this, RevenueActivity.class));
    }

    public void addExpenditure (View view) {
        startActivity(new Intent(this, ExpenditureActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuLogout:
                authentication = FirebaseConfig.getFirebaseAuthentication();
                authentication.signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        userRef.removeEventListener(valueEventListenerUser);
        transactionRef.removeEventListener(valueEventListenerTransactions);
    }
}