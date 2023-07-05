package com.example.mydomain;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DomanViewHolder extends RecyclerView.ViewHolder {
    public TextView name,price,uid;
    public ImageView imgDomain;
    public DomanViewHolder(@NonNull View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.nameDomain);
        price=itemView.findViewById(R.id.priceDomain);
        uid=itemView.findViewById(R.id.uidName);
        imgDomain=itemView.findViewById(R.id.imgDomain);
    }
}
