package com.pastoreli.organizze.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pastoreli.organizze.R;
import com.pastoreli.organizze.model.Transaction;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {

    private List<Transaction> transactionList;
    private Context context;

    public TransactionAdapter(List<Transaction> list, Context context) {
        this.transactionList = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemList = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_transaction, parent, false);

        return new MyViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Transaction transaction = transactionList.get(position);
        Log.i("LISTA", transaction.getCategory());

        holder.textCategory.setText(transaction.getCategory());
        holder.textDescription.setText(transaction.getDescription());

        if(transaction.getType().equals("e")) {
            holder.textAmount.setTextColor(context.getResources().getColor(R.color.colorPrimaryExpenditure));
            holder.textAmount.setText("-"+transaction.getAmount());
        } else {
            holder.textAmount.setTextColor(context.getResources().getColor(R.color.colorPrimaryRevenue));
            holder.textAmount.setText(String.valueOf(transaction.getAmount()));
        }

    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public class  MyViewHolder extends RecyclerView.ViewHolder {

        TextView textAmount, textCategory, textDescription;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textAmount = itemView.findViewById(R.id.textAmount);
            textCategory = itemView.findViewById(R.id.textCategory);
            textDescription = itemView.findViewById(R.id.textDescription);

        }
    }

}
