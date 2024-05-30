package com.example.shopmanager.models.invoice;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "invoice_item_barcodes",
        foreignKeys = @ForeignKey(entity = InvoiceItem.class,
                parentColumns = {"invoice_id","invoice_item_id"},
                childColumns = {"invoice_id","invoice_item_id"},
                onDelete =ForeignKey.CASCADE),
        primaryKeys = {"code","invoice_id"})
public class InvoiceItemBarcode {
    @ColumnInfo(name = "invoice_item_id")
    private int invoice_item_id;

    @ColumnInfo(name = "invoice_id")
    private int invoice_id;


    @ColumnInfo(name = "code")
    @NonNull
    private String barcode;


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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
