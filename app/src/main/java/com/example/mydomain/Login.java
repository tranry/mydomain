package com.example.mydomain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    Button btnAccess;
    EditText edtEmail,edtPassword;
    private boolean KiemTraKytu(String key,String text)
    {
        if(TextUtils.isEmpty(key))
        {
            Toast.makeText(Login.this, text, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();
        edtEmail=findViewById(R.id.edtEmail);
        edtPassword=findViewById(R.id.edtPassword);
        btnAccess=findViewById(R.id.btnAccess);
        btnAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=edtEmail.getText().toString();
                String password=edtPassword.getText().toString();
               if(KiemTraKytu(email,"Vui lòng nhập lại email"))
               {
                   if(KiemTraKytu(password,"Vui lòng nhập lại mật khẩu"))
                   {
                      FirebaseAuth auth= FirebaseAuth.getInstance();
                      auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                          @Override
                          public void onComplete(@NonNull Task<AuthResult> task) {
                              if(task.isSuccessful())
                              {
                                  Intent it=new Intent(view.getContext(), Home.class);
                                  startActivity(it);
                                  finishAffinity();
                              }
                              else Toast.makeText(Login.this, "Thông tin tài khoản hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT).show();

                          }
                      });

                   }

               }


            }
        });
    }
}