package com.example.mydomain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Manage extends AppCompatActivity {
    RecyclerView recyclerView;
    DomainManagerAdapter domainAdapter;
    ArrayList<InfoDomain> mapData;
    Button btnHuy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        recyclerView=findViewById(R.id.recyclerView);
        getListDomain(new StoreFragment.OnDataLoadedListener() {
            @Override
            public void onDataLoaded(ArrayList<InfoDomain> mapData) {

                GridLayoutManager gridLayoutManager=new GridLayoutManager(Manage.this,2);
                recyclerView.setLayoutManager(gridLayoutManager);
                FirebaseUser userUp = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase firebaseDatabaseUp = FirebaseDatabase.getInstance();
                DatabaseReference databaseReferenceUp = firebaseDatabaseUp.getReference("manager");
                domainAdapter=new DomainManagerAdapter(mapData, new DomainManagerAdapter.IclickListener() {
                    @Override
                    public void onClickSellItem(InfoDomain info) {

                        SellDomain(info);
                        databaseReferenceUp.child(userUp.getUid()).child(info.getKey()).child("history").setValue(1);
                        domainAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onClickCancelIem(InfoDomain info) {
                        CancelSell(info);
                        databaseReferenceUp.child(userUp.getUid()).child(info.getKey()).child("history").setValue(0);
                        domainAdapter.notifyDataSetChanged();

                    }


                });
                recyclerView.setAdapter(domainAdapter);
            }
        });

    }

    private void CancelSell(InfoDomain info) {
        AlertDialog.Builder alBuilder=new AlertDialog.Builder(Manage.this);
        alBuilder.setTitle("Thông báo");
        alBuilder.setMessage("Nhấn OK để tiếp tục hủy bán");
        alBuilder.setIcon(R.drawable.ic_baseline_production_quantity_limits_24);
        alBuilder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
        mapData.remove(info);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("sellermanager");

        databaseReference.child(info.getNamedomain()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(Manage.this, "Đăng hủy bán tên miền thành công", Toast.LENGTH_SHORT).show();

            }
        });
            }});
        alBuilder.show();
    }


    private void SellDomain(InfoDomain info) {
        AlertDialog.Builder alBuilder=new AlertDialog.Builder(Manage.this);
        alBuilder.setTitle("Thông báo");
        alBuilder.setMessage("Nhấn OK để tiếp tục bán");
        alBuilder.setIcon(R.drawable.ic_baseline_production_quantity_limits_24);
        alBuilder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("sellermanager");

                databaseReference.child(info.getNamedomain()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        InfoDomain us = task.getResult().getValue(InfoDomain.class);

                        try {
                            us.getNamedomain();

                        } catch (Exception e) {
                            info.setHistory(1);
                            databaseReference.child(info.getNamedomain()).setValue(info.toMap());
                            Toast.makeText(Manage.this, "Đăng bán tên miền thành công", Toast.LENGTH_SHORT).show();


                        }


                    }
                });

            }});
        alBuilder.show();
    }

    private void getListDomain(StoreFragment.OnDataLoadedListener listener) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("manager");
        mapData=new ArrayList<>();

        databaseReference.child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot data : task.getResult().getChildren()) {
                        String s = data.getValue().toString();
                        String uid = s.split("uid=")[1].split(",")[0].trim();
                        int imgdomain = Integer.parseInt(s.split("imgdomain=")[1].split(",")[0].trim());
                        String pr = s.split("pricedomain=")[1].trim();
                        StringBuilder builder = new StringBuilder();
                        for (char i : pr.toCharArray()) {
                            if (Character.isDigit(i))
                                builder.append(i);
                        }
                        int price = Integer.parseInt(builder.toString());
                        String his = s.split("history=")[1].split(",")[0].trim();
                        String named=s.split("namedomain=")[1].split(",")[0].trim();
                        int index=named.indexOf(".");
                        String domain=named.substring(0,index);
                        int history= Integer.parseInt(his.substring(0,his.length()));
                        InfoDomain infoDomain = new InfoDomain(uid, domain, imgdomain, price,history);
                        infoDomain.setKey(data.getKey().toString());
                        mapData.add(infoDomain);

//
                    }
                    listener.onDataLoaded(mapData); // Gọi callback khi dữ liệu đã được lấy thành công
                }
            }
        });
    }


}