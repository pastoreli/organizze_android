package com.pastoreli.organizze.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.pastoreli.organizze.R;
import com.pastoreli.organizze.config.FirebaseConfig;

public class MainActivity extends IntroActivity {

    private FirebaseAuth authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        setButtonNextVisible(false);
        setButtonBackVisible(false);

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_1)
                .build()
        );
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_2)
                .build()
        );
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_3)
                .build()
        );
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_4)
                .build()
        );
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_cadastro)
                .canGoForward(false)
                .canGoBackward(false)
                .build()
        );

    }

    @Override
    protected void onStart() {
        super.onStart();

        checkAuthUser();
    }

    public void btEnter(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void btRegister (View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void checkAuthUser () {

        authentication = FirebaseConfig.getFirebaseAuthentication();

//        authentication.signOut();

        if ( authentication.getCurrentUser() != null )
            goToHome();
    }

    public void goToHome () {
        startActivity(new Intent(this, HomeActivity.class));
    }

}