package com.example.shopmanager.invoicefragments;

import android.widget.TextView;

import com.example.shopmanager.models.InventoryItem;
import com.example.shopmanager.models.invoice.InvoiceItem;

import java.util.List;

public interface SetupInvoiceItemListItemLayout {
    void SetupInvoiceItemListItemLayout(List<InvoiceItem> items,int position, TextView itemNo, TextView itemName, TextView itemQty, TextView itemTotal);
}
