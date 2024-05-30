package com.example.shopmanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopmanager.R;
import com.example.shopmanager.invoicefragments.AddCustomertoInvoiceFragment;
import com.example.shopmanager.invoicefragments.SetupInvoiceItemListItemLayout;
import com.example.shopmanager.models.invoice.InvoiceItem;

import java.util.List;

public class InvoiceItemsAdapter extends RecyclerView.Adapter<InvoiceItemsAdapter.viewholder> {

    List<InvoiceItem> invoiceItems;
    SetupInvoiceItemListItemLayout setupInvoiceItemListItemLayout;
    String requestTag;

    public InvoiceItemsAdapter(List<InvoiceItem> invoiceItems,SetupInvoiceItemListItemLayout setupInvoiceItemListItemLayout,String requestTag) {
        this.invoiceItems = invoiceItems;
        this.setupInvoiceItemListItemLayout = setupInvoiceItemListItemLayout;
        this.requestTag = requestTag;
    }

    @NonNull
    @Override
    public InvoiceItemsAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewholder(LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.invoice_itemslist_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceItemsAdapter.viewholder holder, int position) {
//        InvoiceItem item = invoiceItems.get(position);
//        int num= invoiceItems.size();
        if(requestTag == AddCustomertoInvoiceFragment.ARG_ADD_CUSTOMER_TO_INVOICE_FRAG){
            holder.removeItem.setVisibility(View.VISIBLE);
            holder.removeItem.setOnClickListener(view -> {

            });
        }
        setupInvoiceItemListItemLayout.SetupInvoiceItemListItemLayout(invoiceItems,position, holder.itemNo,holder.itemName,holder.itemQty,holder.itemTotal);


    }

    @Override
    public int getItemCount() {
        return invoiceItems.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{

        TextView itemNo;
        TextView itemName;
        TextView itemQty;
        TextView itemTotal;
        ImageView removeItem;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            itemNo = itemView.findViewById(R.id.item_no);
            itemName = itemView.findViewById(R.id.item_name);
            itemQty = itemView.findViewById(R.id.item_qty);
            itemTotal = itemView.findViewById(R.id.item_total);
            //removeItem = itemView.findViewById(R.id.remove_item);
        }
    }
}
