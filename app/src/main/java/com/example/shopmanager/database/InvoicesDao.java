package com.example.shopmanager.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.shopmanager.models.PaidStatus;
import com.example.shopmanager.models.invoice.Invoice;
import com.example.shopmanager.models.invoice.InvoiceItem;
import com.example.shopmanager.models.invoice.InvoiceItemBarcode;

import java.util.List;

@Dao
public interface InvoicesDao {

    @Insert
    long createInvoice(Invoice invoice);
    @Update
    void updateInvoice(Invoice invoice);


    @Insert
    long addItemtoInvoice(InvoiceItem item);

    @Insert
    void addBarcodetoInvoiceItem(InvoiceItemBarcode barcode);

    @Query("select * from invoice_items where invoice_id = :id")
    public List<InvoiceItem> getInvoiceItemsfromId(int id);

    @Query("select * from invoices order by invoice_id desc ")
    public LiveData<List<Invoice>> getAllInvoices();

    @Query("select * from invoices where invoice_id = :invoiceId")
    public Invoice getInvoicefromId(int invoiceId);


    @Query("update invoices set total = :grandTotal where invoice_id =:invoiceId ")
    void setGrandTotal(Float grandTotal, int invoiceId);

    @Query("update invoices set pdf_path = :pdfPath where invoice_id =:invoiceId ")
    void setPdfPath(String pdfPath, int invoiceId);

    @Query("select pdf_path from invoices where invoice_id =:id ")
    String getPdfPathfromId(int id);

    @Query("update invoices set upi_uri = :upiUri where invoice_id =:invoiceId ")
    void setUpiUri(String upiUri, int invoiceId);

    @Query("select upi_uri from invoices where invoice_id =:invoiceId ")
    String getUpiUrifromInvoiceId(int invoiceId);
    @Query("select * from invoices where customer_id=:id and paid_status=  :status")
    List<Invoice> getDueInvoicesforCustomer(int id, PaidStatus status);
}
