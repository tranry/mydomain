package com.example.mydomain;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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

public class DomainManagerAdapter extends RecyclerView.Adapter<DomanViewHolder>{
    private List<InfoDomain> listInfo;
    private IclickListener iclickListener;
    private InfoDomain info;
    String check;
    View Mview;
    public DomainManagerAdapter(List<InfoDomain> listInfo,IclickListener iclickListener) {
        this.listInfo = listInfo;
        this.iclickListener=iclickListener;
        notifyDataSetChanged();

    }
    public void setCheck(String check) {
        this.check = check;
        notifyDataSetChanged();
    }
    public void setFilterList(List<InfoDomain> listInfo)
    {
        this.listInfo=listInfo;
    }
    @NonNull
    @Override
    public DomanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Mview= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_domain_sell,parent,false);
        return new DomanViewHolder(Mview);

    }



    @Override
    public void onBindViewHolder(@NonNull DomanViewHolder holder, int position) {

        info = listInfo.get(position);
        int i=position;
        if (info == null) {
            return;
        }
        if(info.getNamedomain().indexOf(".")>0)
        {
            info.setNamedomain(info.getNamedomain().substring(0,info.getNamedomain().indexOf(".")));
        }
        holder.name.setText(info.getNamedomain());
        holder.imgDomain.setImageResource(info.getImgdomain());
        holder.price.setText("Giá đã mua: " + info.getPricedomain() + "$");

        if (info.getHistory() == 1) {
            holder.btnSellManage.setVisibility(View.GONE);
            holder.btnCancelSell.setVisibility(View.VISIBLE);
        } else {
            holder.btnSellManage.setVisibility(View.VISIBLE);
            holder.btnCancelSell.setVisibility(View.GONE);
        }
        if (check == null || check.isEmpty()) {
            // Không có giá trị "check" hoặc trống
            holder.btnSelectedDeleteItem.setVisibility(View.GONE);
            holder.btnSelectedEditItem.setVisibility(View.GONE);
        } else if (check.equals("Edit")) {
            // Giá trị "check" là "Edit"

            holder.btnSellManage.setVisibility(View.GONE);
            holder.btnCancelSell.setVisibility(View.GONE);
            holder.btnSelectedDeleteItem.setVisibility(View.GONE);
            holder.btnSelectedEditItem.setVisibility(View.VISIBLE);
        } else if (check.equals("Delete")) {
            // Giá trị "check" là "Delete"
            holder.btnSellManage.setVisibility(View.GONE);
            holder.btnCancelSell.setVisibility(View.GONE);
            holder.btnSelectedDeleteItem.setVisibility(View.VISIBLE);
            holder.btnSelectedEditItem.setVisibility(View.GONE);
        }
//        else if(check.equals("Edit"))
//        {
//            holder.btnSelectedDeleteItem.setVisibility(View.GONE);
//            holder.btnSelectedEditItem.setVisibility(View.VISIBLE);
//        }
//        else  if(check.equals("Delete"))
//        {
//            holder.btnSelectedDeleteItem.setVisibility(View.VISIBLE);
//            holder.btnSelectedEditItem.setVisibility(View.GONE);
//        }

        holder.btnSellManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info = listInfo.get(i);
                iclickListener.onClickSellItem(info);

            }
        });

        holder.btnCancelSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info = listInfo.get(i);
                iclickListener.onClickCancelIem(info);
            }
        });
        holder.btnSelectedEditItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info = listInfo.get(i);
                iclickListener.onClickSelectedEditItem(info);
            }
        });
        holder.btnSelectedDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info = listInfo.get(i);
                iclickListener.onClickSelectedDeleteItem(info);
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
        void onClickSelectedEditItem(InfoDomain info);
        void onClickSelectedDeleteItem(InfoDomain info);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
