package com.pastoreli.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.pastoreli.organizze.R;
import com.pastoreli.organizze.config.FirebaseConfig;
import com.pastoreli.organizze.model.User;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmail, editPassword;
    private Button btnEnter;
    private User user;
    private FirebaseAuth authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnEnter = findViewById(R.id.btnEnter);

        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textEmail = editEmail.getText().toString();
                String textPassword = editPassword.getText().toString();

                if( !textEmail.isEmpty() ) {
                    if( !textPassword.isEmpty() ) {

                        user = new User();
                        user.setEmail(textEmail);
                        user.setPassword(textPassword);

                        handeLogin();

                    } else
                        showToast("Preencha a senha!");
                } else
                    showToast("Preencha o email!");

            }
        });

    }

    public void handeLogin () {
        authentication = FirebaseConfig.getFirebaseAuthentication();
        authentication.signInWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                    goToHome();
                else {
                    try {
                        throw task.getException();
                    } catch ( FirebaseAuthInvalidUserException e ) {
                        showToast("Usuário não está cadastado.");
                    } catch ( FirebaseAuthInvalidCredentialsException e ) {
                        showToast("E-mail e senha não correspondem a um usuário cadastrado.");
                    } catch ( Exception e ) {
                        showToast("Erro ao fazer login.");
                    }
                }
            }
        });

    }

    public void goToHome () {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    public void showToast (String text) {
        Toast.makeText(
                LoginActivity.this,
                text,
                Toast.LENGTH_SHORT
        ).show();
    }

}