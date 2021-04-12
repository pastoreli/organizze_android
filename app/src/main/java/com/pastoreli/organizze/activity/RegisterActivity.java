package com.pastoreli.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pastoreli.organizze.R;
import com.pastoreli.organizze.config.FirebaseConfig;
import com.pastoreli.organizze.helper.CustomBase64;
import com.pastoreli.organizze.model.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText editName, editEmail, editPassword;
    private Button btnRegister;
    private FirebaseAuth authentication;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textName = editName.getText().toString();
                String textEmail = editEmail.getText().toString();
                String textPassword = editPassword.getText().toString();

                if( !textName.isEmpty() ) {
                    if( !textEmail.isEmpty() ) {
                        if( !textPassword.isEmpty() ) {

                            user = new User();
                            user.setName(textName);
                            user.setEmail(textEmail);
                            user.setPassword(textPassword);

                            registerUser();

                        } else
                            showToast("Preencha a senha!");
                    } else
                        showToast("Preencha o email!");
                } else
                    showToast("Preencha o nome!");

            }
        });

    }

    public void registerUser () {

        authentication = FirebaseConfig.getFirebaseAuthentication();
        authentication.createUserWithEmailAndPassword(
                user.getEmail(), user.getPassword()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    saveUserData();
                    showToast("Sucesso ao cadastrar usuário");
                    finish();
                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        showToast("Digite uma senha mais forte.");
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        showToast("Por favor, digite um e-mail válido.");
                    } catch (FirebaseAuthUserCollisionException e) {
                        showToast("Esta conta já foi cadastrada.");
                    } catch (Exception e) {
                        showToast("Erro ao cadastrar usuário: "+ e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    public void saveUserData () {
        String idUser = CustomBase64.Base64Encode(user.getEmail());
        user.setIdUser(idUser);

        DatabaseReference firebase = FirebaseConfig.getFirebaseDatabase();
        firebase.child("users")
                .child(user.getIdUser())
                .setValue(user);

    }

    public void showToast (String text) {
        Toast.makeText(
                RegisterActivity.this,
                text,
                Toast.LENGTH_SHORT
        ).show();
    }

}