package com.example.mydomain;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CheckdomainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckdomainFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View mView;
    EditText edtDomain;
    Button btnCheck,btnBuyDomain;
    TextView textKQ;
    ImageView img;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CheckdomainFragment() {
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
    public static CheckdomainFragment newInstance(String param1, String param2) {
        CheckdomainFragment fragment = new CheckdomainFragment();
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
         mView=inflater.inflate(R.layout.fragment_checkdomain, container, false);
         btnCheck= mView.findViewById(R.id.btnCheckDomain);
         edtDomain=mView.findViewById(R.id.edtInputDomain);
         textKQ=mView.findViewById(R.id.textKQ);
        img=mView.findViewById(R.id.imgCheckDomain);
        btnBuyDomain=mView.findViewById(R.id.btnBuyDomain);

        Glide.with(this).load(R.drawable.identity).into(img);
         btnCheck.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     textKQ.setText("");
                     if(edtDomain.getText().toString().indexOf(".")>0) {
                     ApiService.api.call(edtDomain.getText().toString()).enqueue(new Callback<DataObject>() {
                         @SuppressLint("ResourceAsColor")
                         @Override
                         public void onResponse(Call<DataObject> call, Response<DataObject> response) {
                             if (response.isSuccessful()) {
                                 DataObject data = response.body();
                                 if (data != null) {
                                     String check = "Tên miền " + edtDomain.getText().toString();
                                     // Lấy ra dữ liệu từ data
                                     if (data.isAvailable()){
                                         // Sử dụng dữ liệu nhận được
                                         textKQ.setTextColor(Color.DKGRAY);
                                         textKQ.setText(check + " chưa được đăng ký");
                                     }
                                     else {
                                         textKQ.setTextColor(Color.RED);
                                         textKQ.setText(check + " đã được đăng ký\nCòn thời hạn " + data.getDays_to_expire() + " ngày");
                                     }
                                 }
                             } else {
                                 // Xử lý lỗi nếu yêu cầu không thành công
                                 Toast.makeText(view.getContext(), "Kiểm tra thất bại", Toast.LENGTH_SHORT).show();
                             }

                         }

                         @Override
                         public void onFailure(Call<DataObject> call, Throwable t) {
                         }
                     });
                     }else {
                         Toast.makeText(view.getContext(), "Vui lòng nhập đúng định dạng tên miền", Toast.LENGTH_SHORT).show();
                     }
                 }

             });


         return mView;
    }

}