package com.example.mydomain;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreFragment extends Fragment {

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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView= inflater.inflate(R.layout.fragment_store, container, false);
        recyclerView=mView.findViewById(R.id.recyclerViewDashboard);
        sliderView=mView.findViewById(R.id.sliderView);
        ArrayList<SlideModel> imageList = new ArrayList<SlideModel>(); // Create image list

// imageList.add(SlideModel("String Url" or R.drawable)
// imageList.add(SlideModel("String Url" or R.drawable, "title") You can add title

        imageList.add(new SlideModel(R.drawable.googledomains, ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.img1, ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel(R.drawable.img3, ScaleTypes.CENTER_CROP));
        sliderView.setImageList(imageList);
        sliderView.startSliding(1500); // with new period
        GridLayoutManager layoutManager=new GridLayoutManager(mView.getContext(),1,GridLayoutManager.HORIZONTAL,false);

        recyclerView.setLayoutManager(layoutManager);
        DomainDashboardAdapter domainAdapter=new DomainDashboardAdapter(getListDomaain());
        recyclerView.setAdapter(domainAdapter);
        viewall=mView.findViewById(R.id.viewall);
        viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mView.getContext(),GetAllDomain.class);
                startActivity(intent);
            }
        });
        return mView;
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
                new InfoDomain("1", "google", R.drawable.com, 10)
        }));
        return ds;
    }
}