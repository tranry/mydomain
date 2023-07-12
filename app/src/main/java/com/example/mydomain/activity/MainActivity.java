package com.example.mydomain.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.mydomain.Login;
import com.example.mydomain.R;
import com.example.mydomain.Register;

public class MainActivity extends AppCompatActivity {
    Button btnLogin, btnRegister;
    ImageView kitty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        kitty = findViewById(R.id.kitty);
        Glide.with(this).load(R.drawable.kitty).into(kitty);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, Login.class);
                startActivity(it);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, Register.class);
                startActivity(it);
            }
        });
    }


}