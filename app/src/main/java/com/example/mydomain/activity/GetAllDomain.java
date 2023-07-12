package com.example.mydomain.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.mydomain.R;
import com.example.mydomain.adapter.DomainDashboardAdapter;
import com.example.mydomain.fragment.StoreFragment;
import com.example.mydomain.object.InfoDomain;
import com.example.mydomain.object.User;
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

public class GetAllDomain extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<InfoDomain> mapData;
    DomainDashboardAdapter domainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_domain_all);
        this.getSupportActionBar().hide();
        mapData = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerViewDomainAll);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(GetAllDomain.this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        getListDomain(new StoreFragment.OnDataLoadedListener() {
            @Override
            public void onDataLoaded(ArrayList<InfoDomain> mapData) {
                domainAdapter = new DomainDashboardAdapter(mapData, new DomainDashboardAdapter.IclickListener() {
                    @Override
                    public void onClickBuyItem(InfoDomain info) {
                        buyDomain(info);
                    }
                });
                recyclerView.setAdapter(domainAdapter);
            }
        });


    }

    private void buyDomain(InfoDomain info) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AlertDialog.Builder alBuilder = new AlertDialog.Builder(GetAllDomain.this);
        alBuilder.setTitle("Thông báo");
        alBuilder.setMessage("Nhấn OK để tiếp tục mua");
        alBuilder.setIcon(R.drawable.store);
        alBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (info.getUid().equals(user.getUid())) {
                    Toast.makeText(GetAllDomain.this, "Bạn đang sở hữu tên miền này", Toast.LENGTH_SHORT).show();
                } else {
                    changeDomain(info);

                }
            }
        });
        alBuilder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alBuilder.create().show();


    }

    private void changeDomain(InfoDomain info) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase firebaseDataInfo = FirebaseDatabase.getInstance();
        DatabaseReference databaseInfo = firebaseDataInfo.getReference("info");
        databaseInfo.child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                User us = new User();
                us = task.getResult().getValue(User.class);
                try {
                    us.getMoney();

                } catch (Exception e) {
                    Toast.makeText(GetAllDomain.this, "Bạn không đủ tiền để thanh toán", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (us.getMoney() >= info.getPricedomain()) {

                    Long sum = us.getMoney() - info.getPricedomain();
                    databaseInfo.child(user.getUid()).child("money").setValue(sum);
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseSm = firebaseDatabase.getReference("sellermanager");
                    databaseSm.child(info.getNamedomain()).removeValue();
                    DatabaseReference databaseMg = firebaseDatabase.getReference("manager");
                    databaseMg.child(info.getUid()).child(info.getKey()).removeValue();

                    DatabaseReference databaseChange = firebaseDatabase.getReference("manager");
                    DatabaseReference newChildRef = databaseChange.push();

                    // Lấy ID ngẫu nhiên
                    String generatedId = newChildRef.getKey();
                    databaseChange.child(user.getUid()).child(generatedId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            InfoDomain us = task.getResult().getValue(InfoDomain.class);

                            try {
                                us.getNamedomain();
                            } catch (Exception e) {
                                info.setUid(user.getUid());
                                info.setHistory(0);
                                int img = info.getImgdomain();
                                if (img == 2131230906) {
                                    info.setNamedomain(info.getNamedomain() + ".com");
                                } else if (img == 2131231357) {
                                    info.setNamedomain(info.getNamedomain() + ".net");
                                } else info.setNamedomain(info.getNamedomain() + ".co.uk");

                                databaseChange.child(user.getUid()).child(generatedId).setValue(info.toMap());
                                Toast.makeText(GetAllDomain.this, "Mua thành công", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(GetAllDomain.this, Manage.class);
                                startActivity(intent);

                            }


                        }
                    });
                } else {
                    Toast.makeText(GetAllDomain.this, "Bạn không đủ tiền để thanh toán", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private void getListDomain(StoreFragment.OnDataLoadedListener listener) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("sellermanager");

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                InfoDomain info = getDataSnapshot(snapshot);
                if (info == null) {
                    return;
                }
                mapData.add(info);
                listener.onDataLoaded(mapData); // Gọi callback khi dữ liệu đã được lấy thành công
                domainAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                InfoDomain info = getDataSnapshot(snapshot);
                if (info == null) {
                    return;
                }
                for (int i = 0; i < mapData.size(); i++) {
                    if (info.getUid() == mapData.get(i).getUid())
                        mapData.set(i, info);
                }
                listener.onDataLoaded(mapData);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                InfoDomain info = snapshot.getValue(InfoDomain.class);
                if (info == null) {
                    return;
                }
                for (int i = 0; i < mapData.size(); i++) {
                    if (info.getNamedomain().contains(mapData.get(i).getNamedomain())) {
                        mapData.remove(mapData.get(i));
                        break;
                    }
                }

                listener.onDataLoaded(mapData);
                // Gọi callback khi dữ liệu đã được lấy thành công
                domainAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private InfoDomain getDataSnapshot(DataSnapshot snapshot) {
        String s = snapshot.getValue().toString();
        String uid = s.split("uid=")[1].split(",")[0].trim();
        int imgdomain = Integer.parseInt(s.split("imgdomain=")[1].split(",")[0].trim());
        String pr = s.split("pricedomain=")[1].split(",")[0].trim();
        int price = Integer.parseInt(pr);
        String his = s.split("history=")[1].split(",")[0].trim();
        String dom = s.split("namedomain=")[1].split(",")[0].trim();
        String domain = dom.substring(0, dom.indexOf("."));
        String key = s.split("key=")[1].split(",")[0].trim();
        int history = Integer.parseInt(his.substring(0, his.length()));
        InfoDomain infoDomain = new InfoDomain(uid, domain, imgdomain, price, history);
        infoDomain.setKey(key.substring(0, key.length() - 1));
        return infoDomain;
    }
}