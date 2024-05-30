package com.example.shopmanager.repo;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.shopmanager.database.InventoryDB;
import com.example.shopmanager.models.Customer;
import com.example.shopmanager.models.InventoryItem;
import com.example.shopmanager.models.ItemBarcode;
import com.example.shopmanager.models.PaidStatus;
import com.example.shopmanager.models.Payment;
import com.example.shopmanager.models.invoice.Invoice;
import com.example.shopmanager.models.invoice.InvoiceItem;
import com.example.shopmanager.models.invoice.InvoiceItemBarcode;
import com.example.shopmanager.webfragment.SyncData;
import com.example.shopmanager.webfragment.WebPageItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Repo {
    Context context;
    InventoryDB db;
    SyncData syncData;

    public Repo(Context context) {
        syncData = new SyncData(this);
        this.context = context;
        db = InventoryDB.getDb(context);
    }

    public LiveData<List<Customer>> getAllCustomers() {
        return db.getCustomersDao().getAllCustomers();
    }

    public long addCustomer(Customer customer) {
        long id = db.getCustomersDao().addCustomer(customer);
        return id;
    }

    public void removeCustomer(Customer customer) {
        db.getCustomersDao().removeCustomer(customer);
    }

    public void updateCustomer(Customer customer) {
        db.getCustomersDao().updateCustomer(customer);
    }


    public LiveData<List<InventoryItem>> getAllItemsinInventory() {
        return db.getDao().getAllItemsinInventory();
    }

    public  void setWebIdforItem(int id,String webId){
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                db.getDao().setWebIdforItem( id,webId);
            }
        });
    }

    //todo sync with webpage
    public void addItemtoInventory(InventoryItem item, List<ItemBarcode> barcodes) {
        if (item.getIsBarcode() == true) {
            long id = db.getDao().addItemtoInventory(item);

            for (ItemBarcode barcode : barcodes) {
                barcode.setId((int) id);
                db.getBarcodesDao().addBarcodetoItem(barcode);
            }


            int availableStock = db.getBarcodesDao().getAvailableStockforItem((int) id);

            db.getDao().setAvailableStock(availableStock, (int) id);

                        List<InventoryItem> items = new ArrayList<>();
                        items.add(getItemfromId((int) id));
                            syncData.addItemstoWebpage(items);

        }
    }

     //todo sync with webpage
    public void addItemtoInventory(InventoryItem item) {
        if (item.getIsBarcode() == false) {
            long id = db.getDao().addItemtoInventory(item);
             List<InventoryItem> items = new ArrayList<>();
             items.add(getItemfromId((int) id));
                 //SyncData.addItemstoWebpage(items);
                 syncData.addItemstoWebpage(items);

        }
    }
       //todo sync with webpage
    public void removeItemfromInventory(InventoryItem item) {
        db.getDao().removeItemfromInventory(item);
    }

    public void removeBarcodefromItem(String code, Integer itemId) {
        db.getBarcodesDao().removeBarcodefromItem(code);
        int availableStock = db.getBarcodesDao().getAvailableStockforItem((int) itemId);

        db.getDao().setAvailableStock(availableStock, (int) itemId);

    }
    //todo sync with webpage
    public void updateIteminInventory(InventoryItem item) {
        db.getDao().updateIteminInventory(item);
        syncData.updateItemonWebPage(item);
    }

    public void getAllItems() {
        db.getDao().getAllItemsinInventory();
    }

    public int getAvailableStockforItem(int itemId) {
        if(db.getDao().getItemfromId(itemId).getIsBarcode()==false){
            Log.d("isBarcode","false");
            return db.getDao().getAvailableStockforItem(itemId);
        }
        else {
            Log.d("isBarcode","true");
            return db.getBarcodesDao().getAvailableStockforItem(itemId);
        }
    }

    //todo sync with webpage
    public void setAvailableStockforItem(int availableStock, int id) {
         db.getDao().setAvailableStock(availableStock,id);
          syncData.updateItemonWebPage(getItemfromId(id));
    }

    public boolean checkIfBarcodeAlreadyExists(String code) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<Boolean> check = service.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return db.getBarcodesDao().checkIfBarcodeAlreadyExists(code) == 1;
            }
        });
        try {
            boolean alreadyExists = check.get();
            return alreadyExists;
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            service.shutdown();
        }


    }


    public long generateInvoice(Invoice invoice) {
        return db.getInvoicesDao().createInvoice(invoice);
    }
    public LiveData<List<Invoice>> getAllInvoices(){
        return db.getInvoicesDao().getAllInvoices();
    }

    public long addItemtoInvoice(InvoiceItem item) {
        return db.getInvoicesDao().addItemtoInvoice(item);
    }

    public void addBarcodetoInvoiceItem(InvoiceItemBarcode barcode) {
        db.getInvoicesDao().addBarcodetoInvoiceItem(barcode);
    }



    public int getItemIdforBarcode(String code) {
        if(!(db.getDao().getItemIdforBarcode(code) > 0)){
            return db.getDao().getItemIdforQRCode(code);
        }
        return db.getDao().getItemIdforBarcode(code);
    }

    public Customer getCustomerfromId(int id) {
        return db.getCustomersDao().getCustomerfromId(id);
    }

    public List<InvoiceItem> getInvoiceItemsfromId(int id) {
        return db.getInvoicesDao().getInvoiceItemsfromId(id);
    }
    public Invoice getInvoicefromId(int id) {
        return db.getInvoicesDao().getInvoicefromId(id);
    }
    public void setPdfPath(String pdfPath, int invoiceId){
        db.getInvoicesDao().setPdfPath(pdfPath,invoiceId);
    }

    public InventoryItem getItemfromId(int invoiceItemId) {
        return db.getDao().getItemfromId(invoiceItemId);
    }



    public void setInvoiceTotal(Float grandTotal, int invoiceId) {
        db.getInvoicesDao().setGrandTotal(grandTotal,invoiceId);
    }

    public Customer getCustomerfromContactNum(String contactNum) {
        return db.getCustomersDao().getCustomerfromContactNum(contactNum);
    }

    public String getPdfPathfromId(int id) {
        return db.getInvoicesDao().getPdfPathfromId(id);
    }

    public void addBarcodetoInventoryItem(ItemBarcode barcode) {
        db.getDao().addBarcodetoInventoryItem(barcode);

    }


    public LiveData<List<Invoice>> getAllInvoicesforCustomer(int id) {
        return db.getCustomersDao().getAllInvoicesforCustomer(id);
    }

    public void setUpiUri(String upiUri, int invoiceId) {
        db.getInvoicesDao().setUpiUri(upiUri,invoiceId);
    }

    public String getUpiUrifromInvoiceId(int invoiceId) {
        return db.getInvoicesDao().getUpiUrifromInvoiceId( invoiceId);
    }



    public void updateInvoice(Invoice invoice) {
        db.getInvoicesDao().updateInvoice(invoice);
    }

    public boolean checkIfItemisinStock(String qrSKU) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<Boolean> check = service.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return db.getDao().checkIfItemisInStock(qrSKU) == 1;
            }
        });
        try {
            boolean inStock = check.get();
            return inStock;
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            service.shutdown();
        }
    }

    public boolean checkIfItemAlreadyExists(String qrSKU) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<Boolean> check = service.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return db.getDao().checkIfItemAlreadyExists(qrSKU) == 1;
            }
        });
        try {
            boolean inStock = check.get();
            return inStock;
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            service.shutdown();
        }
    }

    public InventoryItem getItemfromSKU(String qrSKU) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<InventoryItem> get = service.submit(new Callable<InventoryItem>() {
            @Override
            public InventoryItem call() throws Exception {
                return db.getDao().getItemfromId(db.getDao().getItemIdforQRCode(qrSKU));
            }
        });
        try {
            return get.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            service.shutdown();
        }
    }

    public Double getDueAmountforCustomer(int id) {
      List<Invoice> dueInvoices = db.getInvoicesDao().getDueInvoicesforCustomer(id, PaidStatus.UNPAID);
      Double dueAmt=0.00;
      for (Invoice in:
             dueInvoices) {
            dueAmt+=in.getTotal();
        }

      return dueAmt;

    }

    public LiveData<List<InventoryItem>> getOutofStockItems() {
        return db.getDao().getOutofStockItems();
    }

    public LiveData<List<InventoryItem>> getNonBarcodeandInStockItemsinInventory() {
        return db.getDao().getNonBarcodeandInStockItemsinInventory(false);
    }

    public void addPayment(Payment payment) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                db.getPaymentDao().addPayment(payment);
            }
        });

    }
    public LiveData<List<Payment>> getallpayments(){
        return db.getPaymentDao().getAllPayments();
    }
}
