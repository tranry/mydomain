package com.example.mydomain;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetAllDomain extends AppCompatActivity {
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_domain_all);
        this.getSupportActionBar().hide();
        recyclerView=findViewById(R.id.recyclerViewDomainAll);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(GetAllDomain.this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        DomainDashboardAdapter domainAdapter=new DomainDashboardAdapter(getListDomaain());
        recyclerView.setAdapter(domainAdapter);

    }
    private List<InfoDomain> getListDomaain() {
        List<InfoDomain> ds= new ArrayList<InfoDomain>(Arrays.asList(new InfoDomain[]{
                new InfoDomain("1", "superm", R.drawable.com, 10),
                new InfoDomain("1", "timkiemmail", R.drawable.com, 10),
                new InfoDomain("1", "google", R.drawable.com, 10),
                new InfoDomain("1", "superm", R.drawable.com, 10),
                new InfoDomain("1", "timkiemmail", R.drawable.com, 10),
                new InfoDomain("1", "google", R.drawable.com, 10),new InfoDomain("1", "superm", R.drawable.com, 10),
                new InfoDomain("1", "timkiemmail", R.drawable.com, 10),
                new InfoDomain("1", "google", R.drawable.com, 10),
                new InfoDomain("1", "google", R.drawable.com, 10)
        }));
        return ds;
    }
}