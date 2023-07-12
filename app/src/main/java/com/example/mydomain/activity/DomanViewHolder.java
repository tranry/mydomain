package com.example.mydomain.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mydomain.R;

public class DomanViewHolder extends RecyclerView.ViewHolder {
    public TextView name,price;
    public ImageView imgDomain;
    public Button btnSellManage,btnCancelSell,btnBuyDashBoard,btnSelectedEditItem,btnSelectedDeleteItem;
    public DomanViewHolder(@NonNull View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.nameDomain);
        price=itemView.findViewById(R.id.priceDomain);
        imgDomain=itemView.findViewById(R.id.imgDomain);
        btnSellManage=itemView.findViewById(R.id.btnSellManage);
        btnCancelSell=itemView.findViewById(R.id.btnCancelSell);
        btnBuyDashBoard=itemView.findViewById(R.id.btnBuyDashBoard);
        btnSelectedEditItem=itemView.findViewById(R.id.btnSelectedEditItem);
        btnSelectedDeleteItem=itemView.findViewById(R.id.btnSelectedDeleteItem);
    }
}
