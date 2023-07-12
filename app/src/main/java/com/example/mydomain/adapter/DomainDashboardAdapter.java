package com.example.mydomain.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydomain.activity.DomanViewHolder;
import com.example.mydomain.object.InfoDomain;
import com.example.mydomain.R;

import java.util.List;

public class DomainDashboardAdapter extends RecyclerView.Adapter<DomanViewHolder> {
    private List<InfoDomain> listInfo;
    private IclickListener iclickListener;
    private InfoDomain info;

    public DomainDashboardAdapter(List<InfoDomain> listInfo, IclickListener iclickListener) {
        this.listInfo = listInfo;
        this.iclickListener = iclickListener;
        notifyDataSetChanged();

    }

    public void setFilterList(List<InfoDomain> listInfo) {
        this.listInfo = listInfo;
        notifyDataSetChanged();

    }


    @NonNull
    @Override
    public DomanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_domain_dashboard, parent, false);
        return new DomanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DomanViewHolder holder, int position) {
        info = listInfo.get(position);
        int i = position;
        if (info == null)
            return;
        holder.name.setText(info.getNamedomain());
        holder.imgDomain.setImageResource(info.getImgdomain());
        holder.price.setText("Giá : " + info.getPricedomain() + "$");
        holder.btnBuyDashBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info = listInfo.get(i);
                iclickListener.onClickBuyItem(info);
            }
        });
    }

    public interface IclickListener {
        void onClickBuyItem(InfoDomain info);

    }

    @Override
    public int getItemCount() {
        if (listInfo != null)
            return listInfo.size();
        return 0;
    }
}
