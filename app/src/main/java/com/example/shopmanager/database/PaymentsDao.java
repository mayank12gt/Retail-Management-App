package com.example.shopmanager.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.shopmanager.models.Customer;
import com.example.shopmanager.models.Payment;

import java.util.List;

@Dao
public interface PaymentsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long addPayment(Payment payment);

    @Delete
    void removePayment(Payment payment);

    @Update
    public void updatePayment(Payment payment);

    @Query("select * from payments order by timestamp DESC ")
    LiveData<List<Payment>> getAllPayments();
}
