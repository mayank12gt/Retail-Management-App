package com.example.shopmanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopmanager.R;
import com.example.shopmanager.invoicefragments.OnInvoiceItemClicked;
import com.example.shopmanager.invoicefragments.SetupInvoiceItemLayout;
import com.example.shopmanager.models.invoice.Invoice;
import com.example.shopmanager.models.invoice.InvoiceItem;
import com.google.android.material.chip.Chip;

import java.util.List;

public class InvoicesAdapter extends RecyclerView.Adapter<InvoicesAdapter.viewholder> {

    List<Invoice> invoiceList;
SetupInvoiceItemLayout setupInvoiceItemLayout;
OnInvoiceItemClicked onInvoiceItemClicked;

    public InvoicesAdapter(List<Invoice> invoiceList, SetupInvoiceItemLayout setupInvoiceItemLayout,OnInvoiceItemClicked onInvoiceItemClicked) {
        this.invoiceList = invoiceList;
        this.setupInvoiceItemLayout = setupInvoiceItemLayout;
        this.onInvoiceItemClicked = onInvoiceItemClicked;
    }



    @NonNull
    @Override
    public InvoicesAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new viewholder(LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.invoice_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InvoicesAdapter.viewholder holder, int position) {
        Invoice invoice = invoiceList.get(position);
        holder.itemView.setOnClickListener(view -> {
            onInvoiceItemClicked.OnInvoiceItemClicked(invoice);
        });

        setupInvoiceItemLayout
                .setupInvoiceItemLayout(invoice,
                        holder.customerName,
                        holder.grandTotal,
                        holder.numItems,
                        holder.date,
                        holder.invoiceItemsRv,
                        holder.paymentStatusChip);


    }

    @Override
    public int getItemCount() {
        return invoiceList.size();
    }
    public void update(List<Invoice> invoiceList){
        this.invoiceList = invoiceList;
        notifyDataSetChanged();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        TextView customerName;
        TextView grandTotal;
        TextView numItems;
        TextView date;
        RecyclerView invoiceItemsRv;
        Chip paymentStatusChip;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.customer_name);
            grandTotal = itemView.findViewById(R.id.invoice_grand_total);
            numItems = itemView.findViewById(R.id.invoice_num_items);
            date = itemView.findViewById(R.id.invoice_date);
            invoiceItemsRv = itemView.findViewById(R.id.invoice_items_rv);
            paymentStatusChip = itemView.findViewById(R.id.paid_status_chip);
        }

    }
}
