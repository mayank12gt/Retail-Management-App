package com.example.shopmanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shopmanager.R;
import com.example.shopmanager.inventoryfragments.OnInventoryItemClicked;
import com.example.shopmanager.invoicefragments.OnAddBtnClicked;
import com.example.shopmanager.invoicefragments.OnRemoveBtnClicked;
import com.example.shopmanager.models.InventoryItem;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class AddItemsManuallyAdapter extends RecyclerView.Adapter<AddItemsManuallyAdapter.viewholder> {

    List<InventoryItem> inventoryItems;
    OnAddBtnClicked onAddBtnClicked;
    OnRemoveBtnClicked onRemoveBtnClicked;

    public AddItemsManuallyAdapter(List<InventoryItem> inventoryItems, OnAddBtnClicked onAddBtnClicked, OnRemoveBtnClicked onRemoveBtnClicked) {
        this.inventoryItems = inventoryItems;
        this.onRemoveBtnClicked = onRemoveBtnClicked;
        this.onAddBtnClicked = onAddBtnClicked;
    }

    @NonNull
    @Override
    public AddItemsManuallyAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewholder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.stock_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddItemsManuallyAdapter.viewholder holder, int position) {
        InventoryItem item = inventoryItems.get(position);
        holder.itemName.setText(item.getName());
        holder.itemSellingPrice.setText("₹ "+String.valueOf(item.getSellingPrice()));
        holder.itemPurchasePrice.setText("₹ "+String.valueOf(item.getPurchasePrice()));
        holder.itemCategory.setText(item.getCategory());
        holder.qty.setText(String.valueOf(0));
        holder.itemAvailableStock.setText(String.valueOf(item.getAvailableStock())+item.getUnit());
        if(item.getImage()!=null) {
            Glide.with(holder.itemView.getContext()).load(item.getImage()).into(holder.itemImage);
        }
       holder.addItemtoInvoice.setOnClickListener(view -> {
           if(Integer.parseInt(holder.qty.getText().toString())<item.getAvailableStock()) {
               onAddBtnClicked.addBtnClicked(item);
               holder.qty.setText(String.valueOf(Integer.parseInt(holder.qty.getText().toString()) + 1));

           }});

        holder.removeItemfromInvoice.setOnClickListener(view -> {
            onRemoveBtnClicked.removeBtnClicked(item);
            if(Integer.parseInt(holder.qty.getText().toString())>0) {
                holder.qty.setText(String.valueOf(Integer.parseInt(holder.qty.getText().toString()) - 1));
            }
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
        ImageView addItemtoInvoice;
        ImageView removeItemfromInvoice;
        TextView qty;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_Image);
            itemName = itemView.findViewById(R.id.item_name);
            itemCategory = itemView.findViewById(R.id.item_category);
            itemSellingPrice = itemView.findViewById(R.id.item_selling_price);
            itemPurchasePrice = itemView.findViewById(R.id.item_purchase_price);
            itemAvailableStock = itemView.findViewById(R.id.item_available_stock);
            addItemtoInvoice = itemView.findViewById(R.id.add_item);
            removeItemfromInvoice = itemView.findViewById(R.id.remove_item);
            qty = itemView.findViewById(R.id.item_qty);
        }
    }
}
