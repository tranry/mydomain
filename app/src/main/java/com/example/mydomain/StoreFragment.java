package com.example.mydomain;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
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
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class StoreFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    RecyclerView recyclerView;
    ImageSlider sliderView;
    TextView viewall;
    SearchView searchView;
    DomainDashboardAdapter domainAdapter;
    View mView;
    ArrayList<InfoDomain> mapData;
    ListDomain listDomain;
    LinearLayout layoutGiaoDich,layoutDauGia;

    public StoreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrangChuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StoreFragment newInstance(String param1, String param2) {
        StoreFragment fragment = new StoreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapData=new ArrayList<>();
        getListDomain(new OnDataLoadedListener() {
            @Override
            public void onDataLoaded(ArrayList<InfoDomain> mapData) {
                domainAdapter=new DomainDashboardAdapter(mapData, new DomainDashboardAdapter.IclickListener() {
                    @Override
                    public void onClickBuyItem(InfoDomain info) {
                        buyDomain(info);
                    }
                });
                recyclerView.setAdapter(domainAdapter);
                listDomain=new ListDomain(mapData);
            }
        });
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void buyDomain(InfoDomain info) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AlertDialog.Builder alBuilder=new AlertDialog.Builder(mView.getContext());
        alBuilder.setTitle("Thông báo");
        alBuilder.setMessage("Nhấn OK để tiếp tục mua");
        alBuilder.setIcon(R.drawable.store);
        alBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              if(info.getUid().equals(user.getUid()))
              {
                  Toast.makeText(mView.getContext(),"Bạn đang sở hữu tên miền này",Toast.LENGTH_SHORT).show();
              }
              else
              {

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
        FirebaseDatabase firebaseDataInfo=FirebaseDatabase.getInstance();
        DatabaseReference databaseInfo=firebaseDataInfo.getReference("info");
        databaseInfo.child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                User us = new User();
                us = task.getResult().getValue(User.class);
                try {
                    us.getMoney();

                } catch (Exception e) {
                    Toast.makeText(mView.getContext(), "Bạn không đủ tiền để thanh toán", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (us.getMoney() >= info.getPricedomain()) {

                    Long sum = us.getMoney() - info.getPricedomain();
                    databaseInfo.child(user.getUid()).child("money").setValue(sum);

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
                                int img=info.getImgdomain();
                                if(img==2131230906)
                                {
                                    info.setNamedomain(info.getNamedomain()+".com");
                                }
                                else  if(img==2131231357)
                                {
                                    info.setNamedomain(info.getNamedomain()+".net");
                                }
                                else info.setNamedomain(info.getNamedomain()+".co.uk");

                                databaseChange.child(user.getUid()).child(generatedId).setValue(info.toMap());
                                Toast.makeText(mView.getContext(),"Mua thành công",Toast.LENGTH_SHORT).show();

                                Intent intent=new Intent(mView.getContext(),Manage.class);
                                startActivity(intent);

                            }


                        }
                    });
                }
                else
                {
                    Toast.makeText(mView.getContext(), "Bạn không đủ tiền để thanh toán", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
            });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         mView= inflater.inflate(R.layout.fragment_store, container, false);
        recyclerView=mView.findViewById(R.id.recyclerViewDashboard);
        sliderView=mView.findViewById(R.id.sliderView);
        layoutGiaoDich=mView.findViewById(R.id.layoutGiaoDich);
        layoutDauGia=mView.findViewById(R.id.layoutDauGia);
        layoutGiaoDich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mView.getContext(),"Sắp ra mắt",Toast.LENGTH_SHORT).show();

            }
        });
        layoutDauGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mView.getContext(),"Sắp ra mắt",Toast.LENGTH_SHORT).show();

            }
        });
        searchView=mView.findViewById(R.id.searchViewStore);
        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

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
        ArrayList<SlideModel> imageList = new ArrayList<SlideModel>(); // Create image list

// imageList.add(SlideModel("String Url" or R.drawable)
// imageList.add(SlideModel("String Url" or R.drawable, "title") You can add title

        imageList.add(new SlideModel(R.drawable.img6, ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.img1, ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.img3, ScaleTypes.CENTER_CROP));
        sliderView.setImageList(imageList);
        sliderView.startSliding(1500); // with new period
        GridLayoutManager layoutManager=new GridLayoutManager(mView.getContext(),1,GridLayoutManager.HORIZONTAL,false);

        recyclerView.setLayoutManager(layoutManager);


        viewall=mView.findViewById(R.id.viewall);
        viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(mView.getContext(),GetAllDomain.class);
                intent.putParcelableArrayListExtra("seller",listDomain);
                startActivity(intent);
            }
        });
        return mView;
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



    public interface OnDataLoadedListener {
        void onDataLoaded(ArrayList<InfoDomain> mapData);
    }


    private void getListDomain(OnDataLoadedListener listener) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("sellermanager");

        databaseReference.addChildEventListener(new ChildEventListener() {
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
                listener.onDataLoaded(mapData); // Gọi callback khi dữ liệu đã được lấy thành công
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

    private  InfoDomain getDataSnapshot(DataSnapshot snapshot)
    {
        String s = snapshot.getValue().toString();
        String uid = s.split("uid=")[1].split(",")[0].trim();
        int imgdomain = Integer.parseInt(s.split("imgdomain=")[1].split(",")[0].trim());
        String pr = s.split("pricedomain=")[1].split(",")[0].trim();
        int price = Integer.parseInt(pr);
        String his = s.split("history=")[1].split(",")[0].trim();
        String dom=s.split("namedomain=")[1].split(",")[0].trim();
        String domain=dom.substring(0,dom.indexOf("."));
        String key=s.split("key=")[1].split(",")[0].trim();
        int history= Integer.parseInt(his.substring(0,his.length()));
        InfoDomain infoDomain = new InfoDomain(uid, domain, imgdomain, price,history);
        infoDomain.setKey(key.substring(0,key.length()-1));
        return  infoDomain;
    }
}