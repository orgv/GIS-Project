package com.example.consolefindergis;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.SalesViewHolder> {

    private List<SalesInfo> mData;
    //private LayoutInflater mInflater;
    private Context context;

    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, List<SalesInfo> data) {
        //this.mInflater = LayoutInflater.from(context);
        this.context=context;
        this.mData = data;
    }



    // stores and recycles views as they are scrolled off screen
    public class SalesViewHolder extends RecyclerView.ViewHolder {
        TextView title,description,sellerName,sellerNumber,city,itemPrice;

        SalesViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_page);
            description = itemView.findViewById(R.id.description);
            sellerName = itemView.findViewById(R.id.sellerName);
            sellerNumber = itemView.findViewById(R.id.sellerNumber);
            city = itemView.findViewById(R.id.city);
            itemPrice = itemView.findViewById(R.id.itemPrice);
        }

    }

    // inflates the row layout from xml when needed
    @Override
    public SalesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_layout, parent, false);
        return new SalesViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(SalesViewHolder holder, int position) {

        SalesInfo salesInfo = mData.get(position);

        holder.description.setText(salesInfo.description);
        holder.title.setText(salesInfo.title);
        holder.sellerName.setText(salesInfo.sellerName);
        holder.sellerNumber.setText(salesInfo.sellerNumber);
        holder.itemPrice.setText(salesInfo.itemPrice);
        holder.city.setText(salesInfo.city);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("description", salesInfo.description);
                bundle.putString("title", salesInfo.title);
                bundle.putString("sellerName", salesInfo.sellerName);
                bundle.putString("sellerNumber", salesInfo.sellerNumber);
                bundle.putString("itemPrice", salesInfo.itemPrice);
                bundle.putString("city", salesInfo.city);

            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


}

