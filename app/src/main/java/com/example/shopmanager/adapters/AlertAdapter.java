package com.example.shopmanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopmanager.R;
import com.example.shopmanager.inventoryfragments.OnInventoryItemClicked;
import com.example.shopmanager.models.Alert;

import org.bouncycastle.asn1.cmp.POPODecKeyChallContent;

import java.util.List;

public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.viewholder> {
    List<Alert> alerts;
    OnInventoryItemClicked onInventoryItemClicked;

    public AlertAdapter(List<Alert> alerts,OnInventoryItemClicked clicked) {
        this.alerts = alerts;
        this.onInventoryItemClicked = clicked;
    }

    @NonNull
    @Override
    public AlertAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.alert_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AlertAdapter.viewholder holder, int position) {
        holder.alertText.setText(alerts.get(position).getAlert());
        holder.itemView.setOnClickListener(view -> {
            onInventoryItemClicked.onInventoryItemClicked(alerts.get(position).getItem());
        });
    }

    @Override
    public int getItemCount() {
        return alerts.size();
    }

    public void update(List<Alert> alerts) {
        this.alerts =alerts;
        notifyDataSetChanged();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView alertText;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            alertText = itemView.findViewById(R.id.alert_name);
        }
    }
}
