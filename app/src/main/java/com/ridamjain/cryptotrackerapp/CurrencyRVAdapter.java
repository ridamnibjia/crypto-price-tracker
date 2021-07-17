package com.ridamjain.cryptotrackerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class  CurrencyRVAdapter extends RecyclerView.Adapter<CurrencyRVAdapter.ViewHolder> {
    private ArrayList<CurrencyRVModel> currencyRVModels;
    private Context context;
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    public CurrencyRVAdapter(ArrayList<CurrencyRVModel> currencyRVModels, Context context) {
        this.currencyRVModels = currencyRVModels;
        this.context = context;
    }
    public void filteredList(ArrayList<CurrencyRVModel> filteredList)
    {
        currencyRVModels = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CurrencyRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_list,parent,false);
        return new CurrencyRVAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CurrencyRVModel currencyRVModel = currencyRVModels.get(position);
        holder.currencyName.setText(currencyRVModel.getName());
        holder.rate.setText("$"+df2.format(currencyRVModel.getPrice()));
        holder.symbol.setText(currencyRVModel.getSymbol());
    }

    @Override
    public int getItemCount() {
        return currencyRVModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView currencyName,symbol,rate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            currencyName = itemView.findViewById(R.id.name);
            symbol = itemView.findViewById(R.id.symbol);
            rate = itemView.findViewById(R.id.price);
        }
    }
}
