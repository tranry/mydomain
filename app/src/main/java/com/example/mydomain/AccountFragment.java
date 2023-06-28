package com.example.mydomain;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView edtName;
    EditText edtEmail,edtSDT,edtNames;
    Button btnEditInfo;View mView;

    public AccountFragment() {
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
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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
         mView=inflater.inflate(R.layout.fragment_account, container, false);
        Button btnThoat= mView.findViewById(R.id.btnThoat);
        edtNames=mView.findViewById(R.id.edtTen);
        edtEmail=mView.findViewById(R.id.edtEmailAccount);
        edtSDT=mView.findViewById(R.id.edtSDT);
         edtName=mView.findViewById(R.id.edtName);
         btnEditInfo=mView.findViewById(R.id.btnSuaThongTin);
        getInfoUser();
        btnEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suaThongTin();
            }
        });
        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(view.getContext(), "Thoát thành công", Toast.LENGTH_SHORT).show();
                Intent it = new Intent(view.getContext(), MainActivity.class);
                startActivity(it);
            }
        });
        return mView;
    }

    private void suaThongTin() {
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profile=new UserProfileChangeRequest.Builder().setDisplayName(edtNames.getText().toString()).build();

        user.updateEmail(edtEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                getInfoUser();
                                Toast.makeText(mView.getContext(), "Update thành công", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
            }
        });
//        PhoneAuthCredential phone=new PhoneAuthCredential();
//        user.updatePhoneNumber(edtSDT.getText().toString())
    }

    private void getInfoUser() {
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        edtName.setText("Chào "+(user.getDisplayName()==""?"Member":user.getDisplayName()));
        edtSDT.setText(user.getPhoneNumber());
        edtEmail.setText(user.getEmail());
        edtNames.setText(user.getDisplayName());
    }
}