package com.example.shopmanager.invoicefragments;

import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.shopmanager.models.invoice.Invoice;
import com.google.android.material.chip.Chip;

public interface SetupInvoiceItemLayout {
    void setupInvoiceItemLayout(Invoice invoice, TextView customerName, TextView grandTotal, TextView numItems, TextView invoiceDate, RecyclerView invoiceItemsRv, Chip paymentStatusChip);


}
