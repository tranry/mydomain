package com.example.mydomain;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DomainDashboardAdapter extends RecyclerView.Adapter<DomanViewHolder> {
    private List<InfoDomain> listInfo;

    public DomainDashboardAdapter(List<InfoDomain> listInfo) {
        this.listInfo = listInfo;
    }

    @NonNull
    @Override
    public DomanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_domain_dashboard,parent,false);
        return new DomanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DomanViewHolder holder, int position) {
        InfoDomain info=listInfo.get(position);
        if(info==null)
            return;
        holder.name.setText(info.getNameDomain());
        holder.imgDomain.setImageResource(info.getImgDomain());
        holder.price.setText("Giá : "+info.getPriceDomain()+"$");
        holder.uid.setText("Tên người bán"+info.getUid());
    }

    @Override
    public int getItemCount() {
        if(listInfo!=null)
            return listInfo.size();
        return 0;
    }
}
