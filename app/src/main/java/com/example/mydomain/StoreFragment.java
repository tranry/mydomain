package com.example.mydomain;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                domainAdapter=new DomainDashboardAdapter(mapData);
                recyclerView.setAdapter(domainAdapter);
                listDomain=new ListDomain(mapData);
            }
        });
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         mView= inflater.inflate(R.layout.fragment_store, container, false);
        recyclerView=mView.findViewById(R.id.recyclerViewDashboard);
        sliderView=mView.findViewById(R.id.sliderView);
        searchView=mView.findViewById(R.id.searchViewStore);
        searchView.clearFocus();

//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                filterList(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                filterList(newText);
//                return false;
//            }
//        });
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
//
//    private void filterList(String query) {
//        List<InfoDomain> listOld=new ArrayList<>();
//        for(InfoDomain data:mapData)
//        {
//            if(data.getNamedomain().toString().toLowerCase().contains(query.toLowerCase()))
//            listOld.add(data);
//        }
//        if(!query.isEmpty())
//        {
//           domainAdapter.setFilterList(listOld);
//        }
//        else domainAdapter.setFilterList(mapData);
//    }



    public interface OnDataLoadedListener {
        void onDataLoaded(ArrayList<InfoDomain> mapData);
    }


    private void getListDomain(OnDataLoadedListener listener) {
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