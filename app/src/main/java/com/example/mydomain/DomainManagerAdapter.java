package com.example.mydomain;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class DomainManagerAdapter extends RecyclerView.Adapter<DomanViewHolder> {
    private List<InfoDomain> listInfo;
    private IclickListener iclickListener;
    private InfoDomain info;
    View Mview;
    public DomainManagerAdapter(List<InfoDomain> listInfo,IclickListener iclickListener) {
        this.listInfo = listInfo;
        this.iclickListener=iclickListener;
    }

    @NonNull
    @Override
    public DomanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Mview= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_domain_sell,parent,false);
        return new DomanViewHolder(Mview);

    }



    @Override
    public void onBindViewHolder(@NonNull DomanViewHolder holder,int position) {
        info=listInfo.get(position);
        if(info==null)
            return;
        holder.name.setText(info.getNamedomain());
        holder.imgDomain.setImageResource(info.getImgdomain());
        holder.price.setText("Giá đã mua: "+info.getPricedomain()+"$");
        holder.uid.setText("Người bán : "+info.getUid());
        if(info.getHistory()==1)
        {
            holder.btnSellManage.setVisibility(View.GONE);
            holder.btnCancelSell.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.btnSellManage.setVisibility(View.VISIBLE);
            holder.btnCancelSell.setVisibility(View.GONE);
        }

        holder.btnSellManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iclickListener.onClickSellItem(info);
            }
        });
        holder.btnCancelSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iclickListener.onClickCancelIem(info);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listInfo!=null)
            return listInfo.size();
        return 0;
    }
    public interface IclickListener
    {
        void onClickSellItem(InfoDomain info);
        void onClickCancelIem(InfoDomain info);

    }


}
