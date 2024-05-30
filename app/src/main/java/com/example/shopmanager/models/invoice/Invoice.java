package com.example.shopmanager.models.invoice;

import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.shopmanager.models.Customer;
import com.example.shopmanager.models.PaidStatus;

import java.io.Serializable;

@Entity(tableName = "invoices",
        foreignKeys = @ForeignKey(entity = Customer.class,
                parentColumns = "customer_id",
                childColumns = "customer_id",
                onDelete = ForeignKey.CASCADE))
public class Invoice implements Serializable {
    @ColumnInfo(name = "invoice_id")
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "customer_id")
    private int customer_id;

    @ColumnInfo(name = "total")
    private double total;

    @ColumnInfo(name = "pdf_path")
    private String pdfPath;

    @ColumnInfo(name="paid_status")
    private PaidStatus status;

    public PaidStatus getStatus() {
        return status;
    }

    public void setStatus(PaidStatus status) {
        this.status = status;
    }

    public String getUpiUri() {
        return upiUri;
    }

    public void setUpiUri(String upiUri) {
        this.upiUri = upiUri;
    }

    @ColumnInfo(name = "upi_uri")
    private String upiUri;

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @ColumnInfo(name = "invoice_timestamp")
    long timeStamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
