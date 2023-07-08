package com.example.mydomain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetAllDomain extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<InfoDomain> mapData;
    DomainDashboardAdapter domainAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_domain_all);
        this.getSupportActionBar().hide();
        mapData=new ArrayList<>();
        recyclerView=findViewById(R.id.recyclerViewDomainAll);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(GetAllDomain.this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        getListDomain(new StoreFragment.OnDataLoadedListener() {
            @Override
            public void onDataLoaded(ArrayList<InfoDomain> mapData) {
                domainAdapter=new DomainDashboardAdapter(mapData);
                recyclerView.setAdapter(domainAdapter);
            }
        });


    }
    private void getListDomain(StoreFragment.OnDataLoadedListener listener) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("sellermanager");


        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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
                        String domain=s.split("namedomain=")[1].split(",")[0].trim();

                        int history= Integer.parseInt(his.substring(0,his.length()));
                        InfoDomain infoDomain = new InfoDomain(uid, domain, imgdomain, price,history);
                        mapData.add(infoDomain);

                    }
                    listener.onDataLoaded(mapData); // Gọi callback khi dữ liệu đã được lấy thành công
                }
            }
        });
    }
}