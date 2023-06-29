package com.example.mydomain;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
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
//    TextView edtName;
    TextView edtEmail,edtSDT,edtNames;
    Button btnEditInfo;View mView;
    ConstraintLayout constraintShare,constraintChangeInfo;
    Button btnChangeInfo;
    EditText edtChangeName,editChangeEmail,edtChangeNumber;
    ConstraintLayout layoutbackToAccount;
    Handler myHandler;

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
//        Button btnThoat= mView.findViewById(R.id.btnThoat);
        edtNames=mView.findViewById(R.id.textName);
        edtEmail=mView.findViewById(R.id.textEmail);
        constraintShare=mView.findViewById(R.id.constraintShare);
        constraintChangeInfo=mView.findViewById(R.id.constraintChangeInfo);

        constraintShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareApp(v.getContext());
            }
        });
        constraintChangeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogChangeInfo(v.getContext());
            }
        });

//        edtSDT=mView.findViewById(R.id.edtSDT);
//         edtName=mView.findViewById(R.id.edtName);
//         btnEditInfo=mView.findViewById(R.id.btnSuaThongTin);
        getInfoUser();
//        btnEditInfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                suaThongTin();
//            }
//        });
//        btnThoat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
//                Toast.makeText(view.getContext(), "Thoát thành công", Toast.LENGTH_SHORT).show();
//                Intent it = new Intent(view.getContext(), MainActivity.class);
//                startActivity(it);
//            }
//        });
        return mView;
    }

    private void showDialogChangeInfo(Context context) {
        final Dialog dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_dialog_change_info);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,1800);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        View view=getLayoutInflater().inflate(R.layout.activity_dialog_change_info,null,false);
        btnChangeInfo=view.findViewById(R.id.btnChangeInfo);
        editChangeEmail=view.findViewById(R.id.edtChangeEmail);
        edtChangeName=view.findViewById(R.id.edtChangeName);
        edtChangeNumber=view.findViewById(R.id.edtChangeNumber);
        layoutbackToAccount=view.findViewById(R.id.layoutbackToAccount);
        Button btnSendVery=view.findViewById(R.id.btnSendVery);
        btnSendVery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtChangeNumber.getText().toString().trim().equals(""))
                {
                         int i=30;
                        delaySend(btnSendVery,i);

                }

            }
        });
        layoutbackToAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
            }
        });
        getInfoChange();
        btnChangeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               changeInfo(dialog);
            }
        });

        dialog.setContentView(view);
    }



    private void delaySend(Button btnSendVery,int i) {
        if(i>0)
        {
            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnSendVery.setText(i+"");
                    delaySend(btnSendVery,i-1);
                }
            },1000);
        }
        else btnSendVery.setText("Send");
    }


    private void shareApp(Context context) {
        Intent intent= new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,"Tải ứng dụng ngay tại https://github.com/tranry/mydomain ");
        intent.setType("text/plain");
        context.startActivity(intent);

    }



    private void getInfoUser() {
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        edtNames.setText("Chào "+(user.getDisplayName()==""?"Member":user.getDisplayName()));
//        edtSDT.setText(user.getPhoneNumber());
        edtEmail.setText(user.getEmail());
//        edtNames.setText(user.getDisplayName());
    }
    private void changeInfo(Dialog dialog) {
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profile=new UserProfileChangeRequest.Builder().setDisplayName(edtChangeName.getText().toString()).build();
        user.updateEmail(editChangeEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        getInfoUser();
                                        Toast.makeText(mView.getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                        dialog.hide();
                                    }

                                }
                });

            }
        });

    }

    private void getInfoChange() {
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        edtChangeName.setText(user.getDisplayName().equals("")?"":user.getDisplayName());
        editChangeEmail.setText(user.getEmail());
    }
}