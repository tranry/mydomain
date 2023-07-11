package com.example.mydomain;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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
    TextView textDomainLive;
    ImageView img;
    LinearLayout layoutDie,layoutLive;
    String domain;
    List<String> dsDomain;

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
        dsDomain=new ArrayList<>();
        KiemTraTonTaiTrongHeThong(new OnLoadData() {
            @Override
            public void onClickCheck(String domain) {
                dsDomain.add(domain);
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
         mView=inflater.inflate(R.layout.fragment_checkdomain, container, false);
         btnCheck= mView.findViewById(R.id.btnCheckDomain);
         edtDomain=mView.findViewById(R.id.edtInputDomain);
        textDomainLive=mView.findViewById(R.id.textDomainLive);
        img=mView.findViewById(R.id.imgCheckDm);
        layoutDie=mView.findViewById(R.id.layoutDie);
        layoutLive=mView.findViewById(R.id.layoutLive);
        btnBuyDomain=mView.findViewById(R.id.btnCheckBuy);
        TextView textNameDomain,textPrice;
        textPrice=mView.findViewById(R.id.textPrice);
        textNameDomain=mView.findViewById(R.id.textNameDomain);

        Glide.with(this).load(R.drawable.sweeshswoosk).into(img);

        btnBuyDomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                DatabaseReference databaseReference=firebaseDatabase.getReference("info");
                AlertDialog.Builder alBuilder=new AlertDialog.Builder(mView.getContext());
                alBuilder.setTitle("Thông báo");
                alBuilder.setMessage("Nhấn OK để tiếp tục mua");
                alBuilder.setIcon(R.drawable.ic_baseline_production_quantity_limits_24);
                alBuilder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseReference.child(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                User us = new User();
                                us =task.getResult().getValue(User.class);
                                StringBuilder builder = new StringBuilder();
                                for (char i : textPrice.getText().toString().toCharArray()) {
                                    if (Character.isDigit(i))
                                        builder.append(i);
                                }
                                try {
                                   us.getMoney();
                                }catch (Exception e)
                                {

                                    Toast.makeText(mView.getContext(), "Bạn không đủ tiền để thanh toán", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (us.getMoney() >= Long.parseLong(builder.toString())) {
                                    Long sum = us.getMoney() - Long.parseLong(builder.toString());
                                    databaseReference.child(user.getUid()).child("money").setValue(sum).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                            {
                                                layoutDie.setVisibility(View.GONE);
                                                edtDomain.setText("");

                                                int index=domain.indexOf(".");
                                                System.out.print(index);
                                                String mien= domain.substring(index+1, domain.length());
                                                int drawable;
                                                if(mien.equals("com"))
                                                {
                                                    drawable=R.drawable.com;
                                                }
                                                else if(mien.equals("net"))
                                                {
                                                    drawable=R.drawable.net;
                                                }
                                                else drawable=R.drawable.site;
                                                setDataDomain(domain, drawable, Integer.parseInt(builder.toString()), 0, new OnLoadData() {
                                                    @Override
                                                    public void onClickCheck(String domain) {
                                                        dsDomain.add(domain);

                                                    }
                                                });

                                            }
                                        }
                                    });
                                } else {edtDomain.setText("");
                                    Toast.makeText(mView.getContext(), "Bạn không đủ tiền trong tài khoản", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                    }
                });
                 alBuilder.show();
            }
        });
         btnCheck.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     domain=edtDomain.getText().toString();
                   for(String s:dsDomain)
                   {
                       if(s.equals(domain)) {
                           Toast.makeText(mView.getContext(),"Tên miền đã tồn tại trong hệ thống",Toast.LENGTH_SHORT).show();
                           return;

                       }
                   }

                     if(domain.indexOf(".")>0) {
                     ApiService.api.call(domain).enqueue(new Callback<DataObject>() {
                         @SuppressLint("ResourceAsColor")
                         @Override
                         public void onResponse(Call<DataObject> call, Response<DataObject> response) {
                             if (response.isSuccessful()) {
                                 layoutLive.setVisibility(View.GONE);

                                 layoutDie.setVisibility(View.GONE);
                                 textNameDomain.setText(domain);
                                 int index=domain.indexOf(".");
                                 System.out.print(index);
                                 String mien= domain.substring(index+1, domain.length());
                                 checkPrice(mien);
                                 Handler handler=new Handler();
                                 handler.postDelayed(new Runnable() {
                                     @Override
                                     public void run() {
                                         DataObject data = response.body();
                                         if (data != null) {
                                             // Lấy ra dữ liệu từ data
                                             if (data.isAvailable()){
                                                 // Sử dụng dữ liệu nhận được
                                                 layoutLive.setVisibility(View.GONE);

                                                 layoutDie.setVisibility(View.VISIBLE);
                                             }
                                             else {
                                                 edtDomain.setText("");
                                                 textDomainLive.setText("Tên miền "+domain
                                                         + " đã đăng ký"+"\nCòn hạn "+data.getDays_to_expire() +" ngày");
                                                 layoutDie.setVisibility(View.GONE);
                                                 layoutLive.setVisibility(View.VISIBLE);
                                             }
                                         }

                                     }
                                 },1500);

                             } else {
                                 // Xử lý lỗi nếu yêu cầu không thành công
                                 Toast.makeText(view.getContext(), "Kiểm tra thất bại", Toast.LENGTH_SHORT).show();
                             }

                         }

                         private void checkPrice(String mien) {
                             FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                             DatabaseReference databaseReference=firebaseDatabase.getReference("PriceDomain");

                             try {

                                 databaseReference.child(mien).addValueEventListener(new ValueEventListener() {
                                     @Override
                                     public void onDataChange(@NonNull DataSnapshot snapshot) {
                                         String id=snapshot.getValue(Long.class)+"";
                                         System.out.print(id);
                                         if(!id.isEmpty())
                                         {
                                             textPrice.setText("Giá : " + id+ "$");

                                         }
                                         else textPrice.setText("Giá : 20$");


                                     }

                                     @Override
                                     public void onCancelled(@NonNull DatabaseError error) {

                                     }
                                 });
                             }catch (DatabaseException ex)
                             {
                                 ex.printStackTrace();
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

    private void KiemTraTonTaiTrongHeThong(OnLoadData on) {
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
    public interface OnLoadData
    {
        void onClickCheck(String domain);

    }
    private void setDataDomain(String domain,int i,int price,int history,OnLoadData on) {
            FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
            FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
            DatabaseReference databaseReference=firebaseDatabase.getReference("manager");
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

                        InfoDomain infoDomain=new InfoDomain(user.getUid(),domain,i,price,history);

                        databaseReference.child(user.getUid()).child(generatedId).updateChildren(infoDomain.toMap(), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(mView.getContext(), "Mua Thành Công", Toast.LENGTH_SHORT).show();
                                on.onClickCheck(infoDomain.getNamedomain());
                            }
                        });
                        Intent intent=new Intent(mView.getContext(),Manage.class);
                        startActivity(intent);
                    }


                }
            });


    }

}