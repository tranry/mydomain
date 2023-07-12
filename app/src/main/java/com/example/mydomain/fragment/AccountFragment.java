package com.example.mydomain.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.mydomain.activity.MainActivity;
import com.example.mydomain.activity.Manage;
import com.example.mydomain.R;
import com.example.mydomain.object.User;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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

public class AccountFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TextView edtEmail, edtNames, textSurplus;
    View mView;
    LinearLayout constraintShare, constraintChangeInfo;
    Button btnChangeInfo;
    EditText edtChangeName, editChangeEmail, edtChangeNumber, edtChangePassword;
    LinearLayout constraintExit, constraintNotify, constraintManageDomain;
    ConstraintLayout layoutbackToAccount;
    String verificationId;
    boolean KiemTraGuiSms = false;
    ImageView imgChangeAvata, profile_image, imgEditSurplus;


    public AccountFragment() {
        // Required empty public constructor
    }

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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_account, container, false);
        edtNames = mView.findViewById(R.id.textName);
        edtEmail = mView.findViewById(R.id.textEmail);
        constraintShare = mView.findViewById(R.id.constraintShare);
        imgChangeAvata = mView.findViewById(R.id.imgChangeAvata);
        constraintChangeInfo = mView.findViewById(R.id.constraintChangeInfo);
        profile_image = mView.findViewById(R.id.profile_image);
        constraintNotify = mView.findViewById(R.id.constraintNotify);
        constraintExit = mView.findViewById(R.id.constraintExit);
        constraintManageDomain = mView.findViewById(R.id.constraintManagerDomain);
        textSurplus = mView.findViewById(R.id.textSurplus);
        imgEditSurplus = mView.findViewById(R.id.imgEditSurplus);
        getInfoUser();
        getSurplus();
        imgEditSurplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSurplus();
            }


        });
        constraintNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogNotify(mView.getContext());
            }
        });
        constraintExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alBuilder = new AlertDialog.Builder(mView.getContext());
                alBuilder.setTitle("Thông báo");
                alBuilder.setMessage("Nhấn OK để tiếp tục đăng xuất");
                alBuilder.setIcon(R.drawable.ic_baseline_warning_24);
                alBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(mView.getContext(), MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(v.getContext(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                    }
                });
                alBuilder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alBuilder.create().show();


            }
        });
        imgChangeAvata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(AccountFragment.this).crop(96, 96).maxResultSize(96, 96).start();
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

        constraintManageDomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mView.getContext(), Manage.class);
                startActivity(intent);

            }
        });
        return mView;
    }

    private void dialogSurplus() {
        final Dialog dialog = new Dialog(mView.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit_surplus);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, 700);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
        View view = getLayoutInflater().inflate(R.layout.dialog_edit_surplus, null, false);
        EditText edtSurPlus = view.findViewById(R.id.edtSurPlus);

        Button btnEditSurPlus = view.findViewById(R.id.btnEditSurPlus);
        btnEditSurPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Long.parseLong(edtSurPlus.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(view.getContext(), "Nhập lại đúng định dạng số tiền cần sửa", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (edtSurPlus.getText().toString().isEmpty() || Long.parseLong(edtSurPlus.getText().toString()) < 0) {
                    Toast.makeText(view.getContext(), "Nhập lại số tiền cần sửa", Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = firebaseDatabase.getReference("info");
                databaseReference.child(user.getUid()).child("money").setValue(Long.parseLong(edtSurPlus.getText().toString())).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(view.getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            getSurplus();
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
        dialog.setContentView(view);
    }

    private void getSurplus() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("info");
        databaseReference.child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                User us = new User();
                us = task.getResult().getValue(User.class);

                try {
                    us.getMoney();
                } catch (Exception e) {
                    return;
                }
                Long surPlus = us.getMoney();
                textSurplus.setText("Số dư : " + surPlus + "$");
            }
        });

    }

    private void dialogNotify(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_notify);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, 500);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
        View view = getLayoutInflater().inflate(R.layout.dialog_notify, null, false);
        TextView textNotify = view.findViewById(R.id.textNotify);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("notify");
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
            Uri uri = data.getData();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setPhotoUri(uri).build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
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
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_dialog_change_info);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, 1800);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        View view = getLayoutInflater().inflate(R.layout.activity_dialog_change_info, null, false);
        btnChangeInfo = view.findViewById(R.id.btnChangeInfo);
        editChangeEmail = view.findViewById(R.id.edtChangeEmail);
        edtChangeName = view.findViewById(R.id.edtChangeName);
        edtChangePassword = view.findViewById(R.id.edtChangePasswords);
        edtChangeNumber = view.findViewById(R.id.edtChangeNumber);
        layoutbackToAccount = view.findViewById(R.id.layoutbackToAccount);
        Button btnSendVery = view.findViewById(R.id.btnSendVery);
        btnSendVery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtChangeNumber.getText().toString().trim().equals("")) {

                    int i = 60;
                    edtChangeNumber.setText("");
                    edtChangeNumber.setHint("Nhập mã OTP");
                    verifyNumber(edtChangeNumber.getText().toString());
                    delaySend(btnSendVery, i);


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
        FirebaseAuth auth = FirebaseAuth.getInstance();
        PhoneAuthOptions phoneAuthOptions = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber("+84" + phone).setActivity(AccountFragment.this.getActivity()).setTimeout(60L, TimeUnit.SECONDS)
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
                        verificationId = s;

                    }
                }).build();
        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions);
    }


    private void delaySend(Button btnSendVery, int i) {

        if (i > 0) {

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnSendVery.setText(i + "");
                    delaySend(btnSendVery, i - 1);
                }
            }, 1000);
        } else {
            edtChangeNumber.setText("");
            btnSendVery.setText("OTP");
        }
    }


    private void shareApp(Context context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "Tải ứng dụng ngay tại https://github.com/tranry/mydomain ");
        intent.setType("text/plain");
        context.startActivity(intent);

    }


    private void getInfoUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        edtNames.setText("Chào " + (user.getDisplayName() == "" ? "Member" : user.getDisplayName()));
        edtEmail.setText(user.getEmail());
        profile_image.setImageURI(user.getPhotoUrl());
    }

    private void changeInfo(Dialog dialog) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder().setDisplayName(edtChangeName.getText().toString()).build();
        try {
            String email = editChangeEmail.getText().toString().trim();
            String password = edtChangePassword.getText().toString().trim();
            user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                        user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    if (!edtChangePassword.getText().toString().isEmpty()) {
                                        FirebaseAuth auth = FirebaseAuth.getInstance();
                                        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    getInfoUser();
                                                    Toast.makeText(mView.getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                                    dialog.cancel();
                                                } else
                                                    Toast.makeText(mView.getContext(), "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    } else
                                        Toast.makeText(mView.getContext(), "Vui lòng nhập mật khẩu để xác thực", Toast.LENGTH_SHORT).show();


                                }

                            }
                        });
                    else
                        Toast.makeText(mView.getContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void getInfoChange() {
        try {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            edtChangeName.setText(user.getDisplayName().equals("") ? "" : user.getDisplayName());
            editChangeEmail.setText(user.getEmail());
        } catch (Exception e) {

        }


    }
}