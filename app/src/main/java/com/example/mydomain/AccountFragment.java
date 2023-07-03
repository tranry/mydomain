package com.example.mydomain;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

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
    EditText edtChangeName,editChangeEmail,edtChangeNumber,edtChangePassword;
    ConstraintLayout layoutbackToAccount,constraintExit,constraintNotify,constraintManageDomain;
    Handler myHandler;
    String verificationId;
    boolean KiemTraGuiSms=false;
    ImageView imgChangeAvata,profile_image;


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
        imgChangeAvata=mView.findViewById(R.id.imgChangeAvata);
        constraintChangeInfo=mView.findViewById(R.id.constraintChangeInfo);
        profile_image=mView.findViewById(R.id.profile_image);
        constraintNotify=mView.findViewById(R.id.constraintNotify);
        constraintExit=mView.findViewById(R.id.constraintExit);
        constraintManageDomain=mView.findViewById(R.id.constraintManageDomain);
        getInfoUser();
        constraintNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogNotify(mView.getContext());
            }
        });
        constraintExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               AccountFragment.this.getActivity().finishAndRemoveTask();
//               System.exit(0);
                AlertDialog.Builder alBuilder=new AlertDialog.Builder(mView.getContext());
                alBuilder.setTitle("Thông báo");
                alBuilder.setMessage("Nhấn OK để tiếp tục đăng xuất");
                alBuilder.setIcon(R.drawable.ic_baseline_warning_24);
                alBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent=new Intent(mView.getContext(),MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(v.getContext(),"Đăng xuất thành công",Toast.LENGTH_SHORT).show();
                    }
                });
                alBuilder.create().show();


            }
        });
        imgChangeAvata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(AccountFragment.this).crop(96,96).maxResultSize(96,96).start();
            }
        });
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
        constraintManageDomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
//                Toast.makeText(mView.getContext(), user.getUid()+"", Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(mView.getContext(),Manage.class);
                startActivity(intent);

            }
        });
        return mView;
    }

    private void dialogNotify(Context context) {
        final Dialog dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_notify);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,500);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations=R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
        View view=getLayoutInflater().inflate(R.layout.dialog_notify,null,false);
        TextView textNotify=view.findViewById(R.id.textNotify);
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference("notify");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                textNotify.setText(snapshot.getValue(String.class));
                databaseReference.setValue("Chào mừng bạn");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            Uri uri=data.getData();

            FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
            UserProfileChangeRequest profile=new  UserProfileChangeRequest.Builder().setPhotoUri(uri).build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        profile_image.setImageURI(uri);
                    }
                }
            });
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(mView.getContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mView.getContext(), "Đã hủy", Toast.LENGTH_SHORT).show();
        }

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
        edtChangePassword=view.findViewById(R.id.edtChangePasswords);
        edtChangeNumber=view.findViewById(R.id.edtChangeNumber);
        layoutbackToAccount=view.findViewById(R.id.layoutbackToAccount);
        Button btnSendVery=view.findViewById(R.id.btnSendVery);
        btnSendVery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtChangeNumber.getText().toString().trim().equals(""))
                {

                         int i=60;
                    edtChangeNumber.setText("");
                    edtChangeNumber.setHint("Nhập mã OTP");
                    verifyNumber(edtChangeNumber.getText().toString());
                    delaySend(btnSendVery,i);


                }

            }
        });
        layoutbackToAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
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

    private void verifyNumber(String phone) {
        FirebaseAuth auth= FirebaseAuth.getInstance();
        PhoneAuthOptions phoneAuthOptions=PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber("+84"+phone).setActivity(AccountFragment.this.getActivity()).setTimeout(60L, TimeUnit.SECONDS)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationId=s;

                    }
                }).build();
        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions);
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
        else {
            edtChangeNumber.setText("");
            btnSendVery.setText("OTP");
        }
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
        profile_image.setImageURI(user.getPhotoUrl());
//        edtNames.setText(user.getDisplayName());
    }
    private void changeInfo(Dialog dialog) {
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profile=new UserProfileChangeRequest.Builder().setDisplayName(edtChangeName.getText().toString()).build();
        try {
            String email=editChangeEmail.getText().toString().trim();
            String password=edtChangePassword.getText().toString().trim();
            user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                           user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        if(!edtChangePassword.getText().toString().isEmpty())
                                        {
//                                            sendCodeVerify(edtChangeNumber.getText().toString());
//                                            user.updatePassword(edtChangePassword.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//                                                    if(task.isSuccessful())
//                                                    {
//                                                        getInfoUser();
//                                                        Toast.makeText(mView.getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
//                                                        dialog.hide();
//                                                    }
//                                                    else Toast.makeText(mView.getContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
//
//
//                                                }
//                                            });
                                            FirebaseAuth auth= FirebaseAuth.getInstance();
                                            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if(task.isSuccessful())
                                                    {
                                                        getInfoUser();
                                                        Toast.makeText(mView.getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                                        dialog.cancel();
                                                    }
                                                    else Toast.makeText(mView.getContext(), "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }
                                       else  Toast.makeText(mView.getContext(), "Vui lòng nhập mật khẩu để xác thực", Toast.LENGTH_SHORT).show();


                                    }

                                }
                });
                    else Toast.makeText(mView.getContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                 }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    private void sendCodeVerify(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        KiemTraGuiSms=true;
    }

    private void getInfoChange() {
        try {
            FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
            edtChangeName.setText(user.getDisplayName().equals("")?"":user.getDisplayName());
            editChangeEmail.setText(user.getEmail());
        }
       catch (Exception e)
       {

       }


    }
}