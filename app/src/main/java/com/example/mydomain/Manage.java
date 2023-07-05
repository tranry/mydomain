package com.example.mydomain;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Manage extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageView imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        recyclerView=findViewById(R.id.recyclerView);
        imgBack=findViewById(R.id.imgBackAccountFragment);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        GridLayoutManager gridLayoutManager=new GridLayoutManager(Manage.this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        DomainAdapter domainAdapter=new DomainAdapter(getListDomaain());
        recyclerView.setAdapter(domainAdapter);
    }

    private List<InfoDomain> getListDomaain() {
        List<InfoDomain> ds= new ArrayList<InfoDomain>(Arrays.asList(new InfoDomain[]{
                new InfoDomain("1", "timkiemmaail.com", R.drawable.com, 10),
                new InfoDomain("1", "timkiemmaail.com", R.drawable.com, 10),
                new InfoDomain("1", "timkiemmaail.com", R.drawable.com, 10),
                new InfoDomain("1", "timkiemmaail.com", R.drawable.com, 10),
                new InfoDomain("1", "timkiemmaail.com", R.drawable.com, 10),
                new InfoDomain("1", "timkiemmaail.com", R.drawable.com, 10),
                new InfoDomain("1", "timkiemmaail.com", R.drawable.com, 10),
                new InfoDomain("1", "timkiemmaail.com", R.drawable.com, 10),
                new InfoDomain("1", "timkiemmaail.com", R.drawable.com, 10),
                new InfoDomain("1", "timkiemmaail.com", R.drawable.com, 10),
                new InfoDomain("1", "timkiemmaail.com", R.drawable.com, 10),
                new InfoDomain("1", "timkiemmaail.com", R.drawable.com, 10),
                new InfoDomain("1", "timkiemmaail.com", R.drawable.com, 10)
        }));
        return ds;
    }
}