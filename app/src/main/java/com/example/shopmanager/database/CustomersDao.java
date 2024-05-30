package com.example.shopmanager.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.shopmanager.models.Customer;
import com.example.shopmanager.models.InventoryItem;
import com.example.shopmanager.models.invoice.Invoice;

import java.util.List;

@Dao
public interface CustomersDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long addCustomer(Customer customer);

    @Delete
    void removeCustomer(Customer customer);

    @Update
    public void updateCustomer(Customer customer);

    @Query("select * from customers order by customer_id DESC ")
    LiveData<List<Customer>> getAllCustomers();
    @Query("select * from customers where customer_id = :id ")
    Customer getCustomerfromId(int id);

    @Query("select * from customers where customer_contact_num = :contactNum ")
    Customer getCustomerfromContactNum(String contactNum);

    @Query("select * from invoices where customer_id = :id ")
    LiveData<List<Invoice>> getAllInvoicesforCustomer(int id);
}
