package com.example.shopmanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shopmanager.R;
import com.example.shopmanager.inventoryfragments.OnInventoryItemClicked;
import com.example.shopmanager.models.InventoryItem;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class StockItemAdapter extends RecyclerView.Adapter<StockItemAdapter.viewholder> {

    List<InventoryItem> inventoryItems;
    OnInventoryItemClicked onInventoryItemClicked;

    public StockItemAdapter(List<InventoryItem> inventoryItems,OnInventoryItemClicked onInventoryItemClicked) {
        this.inventoryItems = inventoryItems;
        this.onInventoryItemClicked = onInventoryItemClicked;
    }

    @NonNull
    @Override
    public StockItemAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewholder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.stock_item_layout2,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull StockItemAdapter.viewholder holder, int position) {
        InventoryItem item = inventoryItems.get(position);
        holder.itemName.setText(item.getName());
        holder.itemSellingPrice.setText("₹ "+String.valueOf(item.getSellingPrice()));
        holder.itemPurchasePrice.setText("₹ "+String.valueOf(item.getPurchasePrice()));
        holder.itemCategory.setText(item.getCategory());
        holder.itemAvailableStock.setText(String.valueOf(item.getAvailableStock())+item.getUnit());
        if(item.getImage()!=null) {
            Glide.with(holder.itemView.getContext()).load(item.getImage()).into(holder.itemImage);
        }
        holder.itemView.setOnClickListener(view -> {
            onInventoryItemClicked.onInventoryItemClicked(item);
        });
    }

    @Override
    public int getItemCount() {
        return inventoryItems.size();
    }

    public void update(List<InventoryItem> inventoryItems){
        this.inventoryItems = inventoryItems;
        notifyDataSetChanged();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        RoundedImageView itemImage;
        TextView itemName;
        TextView itemSellingPrice;
        TextView itemPurchasePrice;
        TextView itemAvailableStock;
        TextView itemCategory;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_Image);
            itemName = itemView.findViewById(R.id.item_name);
            itemCategory = itemView.findViewById(R.id.item_category);
            itemSellingPrice = itemView.findViewById(R.id.item_selling_price);
            itemPurchasePrice = itemView.findViewById(R.id.item_purchase_price);
            itemAvailableStock = itemView.findViewById(R.id.item_available_stock);
        }
    }
}
