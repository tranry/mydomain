package com.example.mydomain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    EditText edtEmail,edtPassword;
    ImageView logingif,imageViewBgLogin,imgSen,imgtree;
    TextView textViewBgLogin;
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
        logingif=findViewById(R.id.btnAccess);
        textViewBgLogin=findViewById(R.id.textView6);
        imgSen=findViewById(R.id.imgSen);
        imgtree=findViewById(R.id.imgtree);

        Glide.with(Login.this).load(R.drawable.bglogin24).into(imgtree);
        Glide.with(Login.this).load(R.drawable.loginlogou).into(imgSen);
        imageViewBgLogin=findViewById(R.id.imageViewBgLogin);
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

// Đặt thời gian và các thuộc tính khác cho animation
        rotateAnimation.setDuration(5000); // Thời gian thực hiện animation (1000 milliseconds = 1 giây)
        rotateAnimation.setRepeatCount(Animation.INFINITE); // Số lần lặp vô hạn (nếu muốn lặp lại)
        rotateAnimation.setFillAfter(false); // Giữ lại trạng thái sau khi hoàn thành animation

// Áp dụng animation cho ImageView
        imageViewBgLogin.startAnimation(rotateAnimation);
        // Tạo đối tượng TranslateAnimation với các tham số cần thiết
        TranslateAnimation translateAnimation = new TranslateAnimation(-textViewBgLogin.getWidth(), 0, 0, 0);

// Đặt thời gian và các thuộc tính khác cho animation
        translateAnimation.setDuration(60000); // Thời gian thực hiện animation (1000 milliseconds = 1 giây)
        translateAnimation.setFillAfter(true); // Giữ lại trạng thái sau khi hoàn thành animation

// Áp dụng animation cho TextView
        textViewBgLogin.startAnimation(translateAnimation);
        Glide.with(Login.this).load(R.drawable.buttonlogingif).into(logingif);
        logingif.setOnClickListener(new View.OnClickListener() {
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
                                  CheckInfoUser();
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

    private void CheckInfoUser() {
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference("info");
        databaseReference.child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                User us = task.getResult().getValue(User.class);

                try {
                    us.getUsername();
                } catch (Exception e) {
                    User User = new User(0L,0L,0L);
                    databaseReference.child(user.getUid()).setValue(User.toMap());
                }


            }
        });
    }


}