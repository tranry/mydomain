package com.example.mydomain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Manage extends AppCompatActivity {
    RecyclerView recyclerView;
    DomainManagerAdapter domainAdapter;
    ArrayList<InfoDomain> mapData;
    LinearLayout layoutManagerAdd,layoutManagerEdit,layoutManagerDelete;
    String checkSelected;
    StoreFragment.OnDataLoadedListener listener;
    List<String> dsDomain;
    SearchView searchManagerDomain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        recyclerView=findViewById(R.id.recyclerView);
        layoutManagerAdd=findViewById(R.id.layoutManagerAdd);
        TextView textView25=findViewById(R.id.textView25);
        TextView textView21=findViewById(R.id.textView21);
        searchManagerDomain=findViewById(R.id.searchViewManager);
        searchManagerDomain.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                filterList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });
        dsDomain=new ArrayList<>();
        KiemTraTonTaiTrongHeThong(new CheckdomainFragment.OnLoadData() {
            @Override
            public void onClickCheck(String domain) {
                dsDomain.add(domain);
            }
        });
        layoutManagerAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAddDomain();
            }
        });
        layoutManagerEdit=findViewById(R.id.layoutManagerEdit);
        List<Integer> kt = new ArrayList<>();
        List<String> groupkt=new ArrayList<>();
        layoutManagerEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupkt.add("Edit");
                 if(groupkt.size()>2&&groupkt.get(groupkt.size()-2).toString().equals("Delete"))
                {
                    kt.add(1);

                }
//                Uri path=Uri.parse("android.resource://"+BuildConfig.APPLICATION_ID+"/"+R.drawable.custom_selected);
               kt.add(1);
               if(kt.size() %2!=0)
               {

                   Drawable drawable= ContextCompat.getDrawable(Manage.this,R.drawable.custom_selected);
                   layoutManagerEdit.setBackground(drawable);
                   textView25.setTextColor(Color.WHITE);
                   Drawable drawables = ContextCompat.getDrawable(Manage.this, R.drawable.custom_login);
                   layoutManagerDelete.setBackground(drawables);
                   textView21.setTextColor(Color.BLACK);
                   checkSelected="Edit";
                   listener.onDataLoaded(mapData);
               }
               else
               {
                   Drawable drawable= ContextCompat.getDrawable(Manage.this,R.drawable.custom_login);
                   layoutManagerEdit.setBackground(drawable);
                   textView25.setTextColor(Color.BLACK);
                   checkSelected="";
                   listener.onDataLoaded(mapData);
               }


            }
        });
        layoutManagerDelete=findViewById(R.id.layoutManagerDelete);
        List<Integer> ktd = new ArrayList<>();
        layoutManagerDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                groupkt.add("Delete");
                if(groupkt.size()>2&&groupkt.get(groupkt.size()-2).toString().equals("Edit"))
                {
                    ktd.add(1);

                }
                ktd.add(1);
                if(ktd.size() %2!=0) {
                    Drawable drawable = ContextCompat.getDrawable(Manage.this, R.drawable.custom_selected);
                    layoutManagerDelete.setBackground(drawable);
                    textView21.setTextColor(Color.WHITE);
                    checkSelected = "Delete";
                    Drawable drawables= ContextCompat.getDrawable(Manage.this,R.drawable.custom_login);
                    layoutManagerEdit.setBackground(drawables);
                    textView25.setTextColor(Color.BLACK);

                    listener.onDataLoaded(mapData);
                }
                else {
                    Drawable drawable = ContextCompat.getDrawable(Manage.this, R.drawable.custom_login);
                    layoutManagerDelete.setBackground(drawable);
                    textView21.setTextColor(Color.BLACK);
                    checkSelected = "";
                    listener.onDataLoaded(mapData);
                }

            }
        });

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

                    }

                    @Override
                    public void onClickCancelIem(InfoDomain info) {
                        CancelSell(info);
                    }

                    @Override
                    public void onClickSelectedEditItem(InfoDomain info) {
                        EditItem(info);
                    }

                    @Override
                    public void onClickSelectedDeleteItem(InfoDomain info) {
                        DeleteItem(info);
                    }


                });
                domainAdapter.setCheck(checkSelected);
                recyclerView.setAdapter(domainAdapter);
            }
        });

    }

    private void DeleteItem(InfoDomain info) {
        AlertDialog.Builder alBuilder=new AlertDialog.Builder(Manage.this);
        alBuilder.setTitle("Thông báo");
        alBuilder.setMessage("Nhấn OK để tiếp tục xóa");
        alBuilder.setIcon(R.drawable.ic_baseline_production_quantity_limits_24);

        alBuilder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("sellermanager");
                if(info.getNamedomain().indexOf(".")>0)
                {
                    info.setNamedomain(info.getNamedomain().substring(0,info.getNamedomain().indexOf(".")));
                }
                databaseReference.child(info.getNamedomain()).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                    }
                });
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase firebase = FirebaseDatabase.getInstance();
                DatabaseReference database = firebase.getReference("manager");
                database.child(user.getUid()).child(info.getKey()).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(Manage.this, "Xóa thành công", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        alBuilder.show();
    }

    private void EditItem(InfoDomain info) {

        final Dialog dialog=new Dialog(Manage.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit_domain);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,1200);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
        View view=getLayoutInflater().inflate(R.layout.dialog_edit_domain,null,false);
        EditText edtName=view.findViewById(R.id.edtNameEdit);
        String named="";
        if(info.getImgdomain()==R.drawable.com)
        {
            named+=".com";
        }
        else if(info.getImgdomain()==R.drawable.net)
        {
            named+=".net";
        }
        else named+=".co.uk";

        edtName.setText(info.getNamedomain()+named);

        EditText edtPrice=view.findViewById(R.id.edtPriceEdit);
        edtPrice.setText(info.getPricedomain()+"");
        Button btnEdit=view.findViewById(R.id.btnAdminEditDomain);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtName.getText().toString().isEmpty()) {
                    Toast.makeText(view.getContext(), "Nhập lại đúng định dạng tên miền", Toast.LENGTH_SHORT).show();
                    return;

                }

                String domain = edtName.getText().toString();
                if (domain.indexOf(".") < 0) {
                    Toast.makeText(view.getContext(), "Nhập lại đúng định dạng tên miền", Toast.LENGTH_SHORT).show();
                    return;
                }
                int index = domain.indexOf(".");
                System.out.print(index);
                String mien = domain.substring(index + 1, domain.length());
                int drawable;
                if (mien.equals("com")) {
                    drawable = R.drawable.com;
                } else if (mien.equals("net")) {
                    drawable = R.drawable.net;
                } else drawable = R.drawable.site;
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("manager");
                int price = 0;
                try {
                    price = Integer.parseInt(edtPrice.getText().toString());
                } catch (Exception ex) {
                    Toast.makeText(view.getContext(), "Nhập lại đúng định dạng số tiền", Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseDatabase firebase = FirebaseDatabase.getInstance();
                DatabaseReference databaseRef = firebase.getReference("sellermanager");
                if(info.getNamedomain().indexOf(".")>0)
                {
                    info.setNamedomain(info.getNamedomain().substring(0,info.getNamedomain().indexOf(".")));
                }
                databaseRef.child(info.getNamedomain()).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                    }
                });
                InfoDomain infoDomain = new InfoDomain(info.getUid(), domain, drawable, price, info.getHistory());
                infoDomain.setKey(info.getKey());
                if(infoDomain.getHistory()==1)
                {
                    databaseRef.child(infoDomain.getNamedomain().substring(0,infoDomain.getNamedomain().indexOf("."))).updateChildren(infoDomain.toMapKey(), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        }
                    });
                }

                databaseReference.child(info.getUid()).child(info.getKey()).updateChildren(infoDomain.toMap(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(view.getContext(), "Sửa Thành Công", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        loadListDomain();
                    }
                });

            }});
                dialog.setContentView(view);
    }
    private void KiemTraTonTaiTrongHeThong(CheckdomainFragment.OnLoadData on) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("manager");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String s = snapshot.getValue().toString();
                String [] dataMulti=s.split("\\}");
                for(String i:dataMulti)
                {
                    String domain=i.split("namedomain=")[1].split(",")[0].trim();
                    on.onClickCheck(domain);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void dialogAddDomain() {
        final Dialog dialog=new Dialog(Manage.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_domain);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,1200);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
        View view=getLayoutInflater().inflate(R.layout.dialog_add_domain,null,false);
        EditText edtName=view.findViewById(R.id.edtName);
        EditText edtPrice=view.findViewById(R.id.edtPrice);
        Button btnAdd=view.findViewById(R.id.btnAdminAddDomain);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtName.getText().toString().isEmpty())
                {
                    Toast.makeText(view.getContext(), "Nhập lại đúng định dạng tên miền", Toast.LENGTH_SHORT).show();
                    return;

                }

                String domain = edtName.getText().toString();
                if (domain.indexOf(".") < 0) {
                    Toast.makeText(view.getContext(), "Nhập lại đúng định dạng tên miền", Toast.LENGTH_SHORT).show();
                    return;
                }
                int index = domain.indexOf(".");
                System.out.print(index);
                String mien = domain.substring(index + 1, domain.length());
                int drawable;
                if (mien.equals("com")) {
                    drawable = R.drawable.com;
                } else if (mien.equals("net")) {
                    drawable = R.drawable.net;
                } else drawable = R.drawable.site;
                for(String s:dsDomain)
                {
                    if(s.equals(domain)) {
                        Toast.makeText(Manage.this,"Tên miền đã tồn tại trong hệ thống",Toast.LENGTH_SHORT).show();
                        return;

                    }
                }
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("manager");
                DatabaseReference newChildRef = databaseReference.push();

                // Lấy ID ngẫu nhiên
                String generatedId = newChildRef.getKey();
                databaseReference.child(user.getUid()).child(generatedId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        InfoDomain us = task.getResult().getValue(InfoDomain.class);

                        try {
                            us.getNamedomain();
                        } catch (Exception e) {
                            int price = 0;
                            try {
                                price = Integer.parseInt(edtPrice.getText().toString());
                            } catch (Exception ex) {
                                Toast.makeText(view.getContext(), "Nhập lại đúng định dạng số tiền", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            InfoDomain infoDomain = new InfoDomain(user.getUid(), domain, drawable, price, 0);

                            databaseReference.child(user.getUid()).child(generatedId).updateChildren(infoDomain.toMap(), new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    Toast.makeText(view.getContext(), "Thêm Thành Công", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });
                        }
                    }
                });
            }});
        dialog.setContentView(view);

    }

    private void filterList(String query) {
        List<InfoDomain> listOld=new ArrayList<>();
        for(InfoDomain data:mapData)
        {
            if(data.getNamedomain().toString().toLowerCase().contains(query.toLowerCase()))
            {
                listOld.add(data);

            }
        }
        if(!query.isEmpty())
        {
            domainAdapter.setFilterList(listOld);
        }
        if(query.isEmpty())
        {
            domainAdapter.setFilterList(mapData);
        }




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
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("sellermanager");
        String d=info.getNamedomain();
        if(d.indexOf(".")>0)
        {
            d=d.substring(0,d.indexOf("."));
        }
        String domain=d;
        databaseReference.child(domain).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

            }
        });

                info.setHistory(0);
                int img=info.getImgdomain();
                if(img==2131230906)
                {
                    info.setNamedomain(domain+".com");
                }
                else  if(img==2131231357)
                {
                    info.setNamedomain(domain+".net");
                }
                else info.setNamedomain(domain+".co.uk");
                FirebaseUser userUp = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase firebaseDatabaseUp = FirebaseDatabase.getInstance();

                DatabaseReference databaseReferenceUp = firebaseDatabaseUp.getReference("manager");

                databaseReferenceUp.child(userUp.getUid()).child(info.getKey()).updateChildren(info.toMap(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                       Toast.makeText(Manage.this, "Hủy bán tên miền thành công", Toast.LENGTH_SHORT).show();


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

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("sellermanager");
                String d=info.getNamedomain();
                if(d.indexOf(".")>0)
                {
                    d=d.substring(0,d.indexOf("."));
                }
                String domain=d;
                databaseReference.child(domain).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        InfoDomain us = new InfoDomain();
                                us=task.getResult().getValue(InfoDomain.class);

                        try {
                            us.getNamedomain();
                            return;

                        } catch (Exception e) {

                            info.setHistory(1);
                            int img=info.getImgdomain();
                            if(img==2131230906)
                            {
                                info.setNamedomain(domain+".com");
                            }
                            else  if(img==2131231357)
                            {
                                info.setNamedomain(domain+".net");
                            }
                            else info.setNamedomain(domain+".co.uk");

                            databaseReference.child(domain).setValue(info.toMapKey());
                            FirebaseUser userUp = FirebaseAuth.getInstance().getCurrentUser();
                            FirebaseDatabase firebaseDatabaseUp = FirebaseDatabase.getInstance();

                            DatabaseReference databaseReferenceUp = firebaseDatabaseUp.getReference("manager");

                            databaseReferenceUp.child(userUp.getUid()).child(info.getKey()).updateChildren(info.toMap(), new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    Toast.makeText(Manage.this, "Đăng bán tên miền thành công", Toast.LENGTH_SHORT).show();

                                }
                            });

                        }


                    }
                });

            }});
        alBuilder.show();
    }
    private void loadListDomain() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("manager");
        mapData = new ArrayList<>();

        databaseReference.child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for(DataSnapshot d:task.getResult().getChildren())
                {
                    InfoDomain info=getDataSnapshot(d);
                    if(info==null)
                    {
                        return;
                    }
                    if(info.getNamedomain().indexOf(".")>0)
                    {
                        info.setNamedomain(info.getNamedomain().substring(0,info.getNamedomain().indexOf(".")));
                    }
                    mapData.add(info);

            }
                listener.onDataLoaded(mapData);
                domainAdapter.notifyDataSetChanged();
        }});


    }
    private void getListDomain(StoreFragment.OnDataLoadedListener listener) {
        this.listener=listener;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("manager");
        mapData=new ArrayList<>();

        databaseReference.child(user.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                InfoDomain info=getDataSnapshot(snapshot);
                if(info==null)
                {
                    return;
                }
                if(info.getNamedomain().indexOf(".")>0)
                {
                    info.setNamedomain(info.getNamedomain().substring(0,info.getNamedomain().indexOf(".")));
                }
                mapData.add(info);
                listener.onDataLoaded(mapData);
                domainAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                InfoDomain info=snapshot.getValue(InfoDomain.class);

                if(info==null)
                {
                    return;
                }
                if(info.getNamedomain().indexOf(".")>0)
                {
                    info.setNamedomain(info.getNamedomain().substring(0,info.getNamedomain().indexOf(".")));
                }
                for(int i=0;i<mapData.size();i++)
                {

                    if(info.getNamedomain().equals(mapData.get(i).getNamedomain()))
                    {  mapData.set(i,info);
                        break;
                    }

                }
                listener.onDataLoaded(mapData);

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                InfoDomain info=snapshot.getValue(InfoDomain.class);
                if(info==null)
                {
                    return;
                }
                if(info.getNamedomain().indexOf(".")>0)
                {
                    info.setNamedomain(info.getNamedomain().substring(0,info.getNamedomain().indexOf(".")));
                }
                for(int i=0;i<mapData.size();i++) {
                    if (info.getNamedomain().equals(mapData.get(i).getNamedomain()))
                    {
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
    private  InfoDomain getDataSnapshot(DataSnapshot data)
    {
        String s = data.getValue().toString();
        String uid = s.split("uid=")[1].split(",")[0].trim();
        int imgdomain = Integer.parseInt(s.split("imgdomain=")[1].split(",")[0].trim());
        String pr = s.split("pricedomain=")[1].split(",")[0].trim();
        pr=pr.substring(0,pr.length()-1);
        int price = Integer.parseInt(pr);
        String his = s.split("history=")[1].split(",")[0].trim();
        String domain=s.split("namedomain=")[1].split(",")[0].trim();
        if(domain.indexOf(".")>0)
        {
            domain=domain.substring(0,domain.indexOf("."));
        }
        int history= Integer.parseInt(his.substring(0,his.length()));
        InfoDomain infoDomain = new InfoDomain(uid, domain, imgdomain, price,history);
        infoDomain.setKey(data.getKey().toString());
        return  infoDomain;
    }

}