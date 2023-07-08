package com.example.mydomain;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class cauhoi extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioButton rdBtnA,rdBtnS,rdBtnSA,rdBtnSO;
    Button btnCheck,btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cauhoi);
        rdBtnA=findViewById(R.id.rdBtnA);
        rdBtnS=findViewById(R.id.rdBtnS);
        rdBtnSA=findViewById(R.id.rdBtnSA);
        rdBtnSO=findViewById(R.id.rdBtnSO);
        btnCheck=findViewById(R.id.btnCheck);
        btnBack=findViewById(R.id.btnBack);
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rdBtnS.isChecked())
                {
                    Toast.makeText(cauhoi.this,"Bạn đã trả lời đúng",Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(cauhoi.this,"Bạn đã trả lời sai",Toast.LENGTH_SHORT).show();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}