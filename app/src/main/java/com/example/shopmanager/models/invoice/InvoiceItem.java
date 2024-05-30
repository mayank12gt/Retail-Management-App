package com.example.shopmanager.models.invoice;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.shopmanager.models.InventoryItem;


@Entity(tableName = "invoice_items",
        foreignKeys = {@ForeignKey(entity = Invoice.class,
                       parentColumns = "invoice_id",
                       childColumns = "invoice_id",
                       onDelete =ForeignKey.CASCADE),
                    @ForeignKey(entity = InventoryItem.class,
                    parentColumns = "item_id",
                    childColumns = "invoice_item_id",
                    onDelete = ForeignKey.NO_ACTION)},
        primaryKeys = {"invoice_item_id","invoice_id"})


public class InvoiceItem {
//    @ColumnInfo(name = "invoice_item_row_id")
//    private int id;

    @ColumnInfo(name = "invoice_id")
    @NonNull
    private int invoice_id;

    @ColumnInfo(name = "invoice_item_id")
    private int invoice_item_id;
    @ColumnInfo(name = "invoice_item_qty")
    private int itemQty;

    public double getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(double itemTotal) {
        this.itemTotal = itemTotal;
    }

    @ColumnInfo(name = "invoice_item_total")
    private double itemTotal;






    public int getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(int invoice_id) {
        this.invoice_id = invoice_id;
    }

    public int getInvoice_item_id() {
        return invoice_item_id;
    }

    public void setInvoice_item_id(int invoice_item_id) {
        this.invoice_item_id = invoice_item_id;
    }

    public int getItemQty() {
        return itemQty;
    }

    public void setItemQty(int itemQty) {
        this.itemQty = itemQty;
    }
}
