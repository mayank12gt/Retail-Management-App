package com.example.shopmanager.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.shopmanager.models.Customer;
import com.example.shopmanager.models.InventoryItem;
import com.example.shopmanager.models.ItemBarcode;
import com.example.shopmanager.models.Payment;
import com.example.shopmanager.models.invoice.Invoice;
import com.example.shopmanager.models.invoice.InvoiceItem;
import com.example.shopmanager.models.invoice.InvoiceItemBarcode;


@Database(entities = {InventoryItem.class, ItemBarcode.class, Customer.class, Invoice.class,
                        InvoiceItem.class, InvoiceItemBarcode.class, Payment.class},version = 1)
public abstract class InventoryDB extends RoomDatabase {

    private static InventoryDB db;

    public static synchronized InventoryDB getDb(Context context){
        if(db==null){
            db = Room.databaseBuilder(context.getApplicationContext(),
                    InventoryDB.class,"inventory_db").build();
        }
        return db;
    }

    public abstract InventoryDao getDao();
    public abstract ItemBarcodesDao getBarcodesDao();
    public abstract CustomersDao getCustomersDao();

    public abstract  InvoicesDao getInvoicesDao();
    public  abstract PaymentsDao getPaymentDao();



}
