package com.example.shopmanager.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.shopmanager.models.Customer;
import com.example.shopmanager.models.invoice.Invoice;
import com.example.shopmanager.repo.Repo;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CustomersViewModel extends AndroidViewModel {
   Repo repo;
    public CustomersViewModel(@NonNull Application application) {
        super(application);
        repo = new Repo(application);
    }

    public LiveData<List<Customer>> getAllCustomers(){
        return repo.getAllCustomers();
    }

    public void addCustomer(Customer customer){
        ExecutorService service= Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                repo.addCustomer(customer);
            }
        });

    }


    public Customer getCustomerfromContactNum(String phoneNum) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<Customer> getCustomer= service.submit(new Callable<Customer>() {
            @Override
            public Customer call() throws Exception {
                return repo.getCustomerfromContactNum(phoneNum);
            }
        });
        try {
            return getCustomer.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            service.shutdown();
        }
    }
}
