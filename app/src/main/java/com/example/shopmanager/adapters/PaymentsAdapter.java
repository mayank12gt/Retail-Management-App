package com.example.shopmanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopmanager.R;
import com.example.shopmanager.customersfragments.OnCustomerItemClicked;
import com.example.shopmanager.models.Customer;
import com.example.shopmanager.models.Payment;
import com.example.shopmanager.utils.Utils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class PaymentsAdapter extends RecyclerView.Adapter<PaymentsAdapter.viewholder> {
    List<Payment> ps;


    public PaymentsAdapter(List<Payment> ps) {
        this.ps = ps;
    }

    @NonNull
    @Override
    public PaymentsAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentsAdapter.viewholder holder, int position) {
        Payment p = ps.get(position);
        holder.time.setText(Utils.formatDate(p.getTimestamp()));
        holder.amt.setText(String.valueOf(p.getAmount()));

    }

    @Override
    public int getItemCount() {
        return ps.size();
    }

    public  void update(List<Payment> ps){
        this.ps = ps;
        notifyDataSetChanged();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView time,amt;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.payment_time);
            amt = itemView.findViewById(R.id.amount);
        }
    }
}
