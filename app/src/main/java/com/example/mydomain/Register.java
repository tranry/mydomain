package com.example.mydomain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    ImageView imgBack;
    ImageView btnSignUp;
    EditText edtEmail,edtPassword,edtPasswords;
    ProgressDialog progress;
    private boolean KiemTraKytu(String key,String text)
    {
        if(TextUtils.isEmpty(key))
        {
            Toast.makeText(Register.this, text, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();
        progress=new ProgressDialog(Register.this);
        edtEmail=findViewById(R.id.edtEmailSignUp);
        edtPassword=findViewById(R.id.edtPasswordSignUp);
        edtPasswords=findViewById(R.id.edtPasswordsSignUp);
        btnSignUp=findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=edtEmail.getText().toString();
                String password=edtPassword.getText().toString();
                String passwords=edtPasswords.getText().toString();
                if(KiemTraKytu(email,"Vui lòng nhập lại email"))
                {
                    if(KiemTraKytu(password,"Vui lòng nhập lại mật khẩu"))
                    {
                        if(KiemTraKytu(passwords,"Vui lòng nhập lại mật khẩu"))
                        {
                            if(password.equals(passwords))
                            {
                                if(password.length()>6) {
                                    progress.show();
                                    FirebaseAuth auth = FirebaseAuth.getInstance();
                                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            progress.dismiss();
                                            if (task.isSuccessful()) {
                                                Intent it = new Intent(view.getContext(), Login.class);
                                                startActivity(it);
                                                finishAffinity();
                                                Toast.makeText(Register.this, "Đăng Ký Thành Công", Toast.LENGTH_SHORT).show();

                                            } else {
                                                AlertDialog.Builder alert = new AlertDialog.Builder(Register.this);
                                                alert.setTitle("Thất bại");
                                                alert.setMessage("Đăng ký tài khoản không thành công");
                                                alert.setIcon(R.drawable.ic_baseline_sms_failed_24);
                                                alert.create().show();
                                            }
                                        }
                                    });
                                }
                                else Toast.makeText(Register.this, "Mật khẩu phải trên 6 ký tự", Toast.LENGTH_SHORT).show();

                            }
                            else Toast.makeText(Register.this, "Mật khẩu của bạn không khớp", Toast.LENGTH_SHORT).show();
                        }

                    }

                }


            }
        });
    }
}