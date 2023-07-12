package com.example.mydomain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.mydomain.activity.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        this.getSupportActionBar().hide();

        ImageView welcome = findViewById(R.id.gif);
        ImageView hello = findViewById(R.id.gifhello);
        Glide.with(this).load(R.drawable.load).into(welcome);
        Glide.with(this).load(R.drawable.hello).into(hello);
        Handler delay = new Handler();
        delay.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextActivity();
            }
        }, 2000);
    }

    private void nextActivity() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent myIt = new Intent(welcome.this, MainActivity.class);
            startActivity(myIt);
        } else {
            Intent myIt = new Intent(welcome.this, Home.class);
            startActivity(myIt);
        }
        finishAffinity();
    }
}