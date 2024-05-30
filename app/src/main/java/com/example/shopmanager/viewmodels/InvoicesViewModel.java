package com.example.shopmanager.viewmodels;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.shopmanager.models.Customer;
import com.example.shopmanager.models.InventoryItem;
import com.example.shopmanager.models.PaidStatus;
import com.example.shopmanager.models.Payment;
import com.example.shopmanager.models.invoice.Invoice;
import com.example.shopmanager.models.invoice.InvoiceItem;
import com.example.shopmanager.models.invoice.InvoiceItemBarcode;
import com.example.shopmanager.repo.Repo;
import com.example.shopmanager.utils.GetStoreData;
import com.example.shopmanager.utils.Utils;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.colors.ColorConstants;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;

import net.glxn.qrgen.android.QRCode;
import net.glxn.qrgen.core.image.ImageType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class InvoicesViewModel extends AndroidViewModel {
    Repo repo;
    Application application;
    public InvoicesViewModel(@NonNull Application application) {
        super(application);
        this.application = application;

        repo = new Repo(getApplication());
    }

    public boolean checkIfBarcodeAlreadyExists(String code){
        return repo.checkIfBarcodeAlreadyExists(code);
    }

    public LiveData<List<Invoice>> getAllInvoices(){
        return repo.getAllInvoices();
    }

    public int generateInvoiceData(String customerName, String customerContactNum, String customerEmail, List<String> invoiceItemCodes){
        ExecutorService service = Executors.newSingleThreadExecutor();
        Float grand_total=0f;

        Future<Integer> getInvoice = service.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Customer customer = getCustomerfromContactNum(customerContactNum);
                int customerId;
                if(customer==null){
                    customer = addCustomer(customerName,customerContactNum,customerEmail);
                    customerId = (int) repo.addCustomer(customer);
                }
                else{
                    customerId = customer.getId();
                }

                Log.d("createdCustomerId",String.valueOf(customerId));

                Invoice invoice = new Invoice();
                invoice.setCustomer_id(customerId);
                invoice.setTotal(0.00);
                invoice.setStatus(PaidStatus.UNPAID);
                invoice.setTimeStamp(System.currentTimeMillis());
                int invoiceId= (int) repo.generateInvoice(invoice);

                Invoice invoiceGenerated = repo.getInvoicefromId(invoiceId);


                HashMap<Integer,List<String>> uniqueItems = new HashMap<>();

                for (String code:invoiceItemCodes) {

                    int item_id = repo.getItemIdforBarcode(code);



                    Log.d("invoiceproductItemId",String.valueOf(item_id));
                    if (uniqueItems.containsKey(item_id)) {
                        uniqueItems.get(item_id).add(code);
                    }
                    else {
                        List<String> codeList = new ArrayList<>();
                        codeList.add(code);
                        uniqueItems.put(item_id, codeList);
                    }
                }
                uniqueItems.forEach((key, value) -> {
                    Log.d("key", String.valueOf(key));

                    for (String v : value) {
                        Log.d("values", v);
                    }
                });

                uniqueItems.forEach((key, value) -> {
                    Log.d("key",String.valueOf(key));

                    for (String v: value) {
                        Log.d("values",v);
                    }
                    if(repo.getItemfromId(key).getIsBarcode()==false){
                        InvoiceItem invoiceItem = new InvoiceItem();
                        invoiceItem.setInvoice_id(invoiceId);
                        invoiceItem.setInvoice_item_id(key);
                        invoiceItem.setItemQty(value.size());

                        int invoiceItemId = (int) repo.addItemtoInvoice(invoiceItem);
                        List<String> codes = uniqueItems.get(key);
                        Log.d("availablestock",String.valueOf(repo.getAvailableStockforItem(key)));
                        Log.d("updatedstock",String.valueOf(repo.getAvailableStockforItem(key)- value.size()));

                        repo.setAvailableStockforItem(repo.getAvailableStockforItem(key)- value.size(),key);
                    }
                    else{
                    InvoiceItem invoiceItem = new InvoiceItem();
                    invoiceItem.setInvoice_id(invoiceId);
                    invoiceItem.setInvoice_item_id(key);
                    invoiceItem.setItemQty(value.size());
                    invoiceItem.setItemTotal(value.size()*repo.getItemfromId(key).getSellingPrice());

                    int invoiceItemId = (int) repo.addItemtoInvoice(invoiceItem);



                    Log.d("grand_total",String.valueOf(grand_total));


                    Log.d("invoiceItemId",String.valueOf(invoiceItemId));


                    List<String> codes = uniqueItems.get(key);
                    for(String code:codes){
                        InvoiceItemBarcode invoiceItemBarcode = new InvoiceItemBarcode();
                        invoiceItemBarcode.setInvoice_item_id(key);
                        invoiceItemBarcode.setBarcode(code);
                        invoiceItemBarcode.setInvoice_id(invoiceId);

                        repo.addBarcodetoInvoiceItem(invoiceItemBarcode);

                        repo.removeBarcodefromItem(code,key);
                    }

                }
            });





                setInvoiceTotal(invoiceId);



                String upiUri = createUPIQR(invoiceId);
                Log.d("upiUri",upiUri.toString());
                repo.setUpiUri(upiUri,invoiceId);


                String path= createPdf(invoiceId);
                Log.d("path",path);


                repo.setPdfPath(path, invoiceId);





                return invoiceId;
            }
        }) ;

        try {
            return getInvoice.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            service.shutdown();
        }


    }

    private void setInvoiceTotal(int invoiceId) {
        ExecutorService service = Executors.newSingleThreadExecutor();

        Future<Double> getTotal = service.submit(new Callable<Double>() {
            @Override
            public Double call() throws Exception {
                List<InvoiceItem> invoiceItems = repo.getInvoiceItemsfromId(invoiceId);
                double grand_total=0.00;
                for (InvoiceItem it:invoiceItems) {
                    grand_total += it.getItemQty()* repo.getItemfromId(it.getInvoice_item_id()).getSellingPrice();
                }
                return grand_total;
            }
        });


        try {
            repo.setInvoiceTotal( getTotal.get().floatValue(), invoiceId);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            service.shutdown();
        }

    }

    public String createPdf(int invoiceId) {
        File outputDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "ShopManagerInvoices");
        if (!outputDirectory.exists()) {
            Log.d("dir","doesn't exist");
            outputDirectory.mkdirs();
        }
        Log.d("dir"," exist");
        String fileName = System.currentTimeMillis() + ".pdf";

        File outputFile = new File(outputDirectory, fileName);
        Log.d("outputfile",outputFile.getAbsolutePath());

        GetStoreData storeData = new GetStoreData(application);
        try {
            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(outputFile));
            Document document = new Document(pdfDocument);
            document.setFontSize(20f);
            Invoice invoice = repo.getInvoicefromId(invoiceId);
            Paragraph heading = new Paragraph();
            heading.add(storeData.getStoreName()+"\n");
            heading.add(storeData.getStoreAddr()+"\n");
            heading.add("Contact num: "+storeData.getStoreContact()+"\n");
            heading.add("Email: "+storeData.getStoreEmail()+"\n");
            heading.add("Date: "+ Utils.formatDate(invoice.getTimeStamp())+"\n");
            heading.add("Customer: "+repo.getCustomerfromId(invoice.getCustomer_id()).getName());

            heading.setBackgroundColor(ColorConstants.CYAN);
            heading.setTextAlignment(TextAlignment.CENTER);

            document.add(heading);
            Table table = new Table(4); // Three columns
            table.setHorizontalAlignment(HorizontalAlignment.CENTER);

            table.addCell("S no.");
            table.addCell("Item Name");
            table.addCell("Quantity");
            table.addCell("Total");

            List<InvoiceItem> invoiceItems = repo.getInvoiceItemsfromId(invoiceId);

            for (int i=0;i<invoiceItems.size();i++) {
                table.addCell(String.valueOf(i+1));
                table.addCell(repo.getItemfromId(invoiceItems.get(i).getInvoice_item_id()).getName());
                table.addCell("x"+invoiceItems.get(i).getItemQty());
                table.addCell(String.valueOf(invoiceItems.get(i).getItemQty()*repo.getItemfromId(invoiceItems.get(i).getInvoice_item_id()).getSellingPrice()));
            }
                    document.add(table);

            Paragraph grandTotal = new Paragraph("GrandTotal: "+invoice.getTotal());
            grandTotal.setTextAlignment(TextAlignment.CENTER);

            Paragraph footer = new Paragraph("Thank You for visiting!!");
            footer.setBackgroundColor(ColorConstants.CYAN);
            footer.setTextAlignment(TextAlignment.CENTER);
            document.add(grandTotal);
            document.add(footer);

            Bitmap qrBitmap = QRCode.from(repo.getUpiUrifromInvoiceId(invoiceId)).to(ImageType.PNG).withSize(250, 250).bitmap();


            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            qrBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            Image image = new Image(ImageDataFactory.create(byteArray));
            image.setHorizontalAlignment(HorizontalAlignment.CENTER);
            image.scaleToFit(200, 200);
            document.add(image);


            document.close();
        } catch (FileNotFoundException e) {
            Log.d("pdf error",e.getLocalizedMessage());

        } catch (IOException e) {

            Log.d("pdf error",e.getLocalizedMessage());
        }
        Log.d("path",outputFile.getAbsolutePath());
        return outputFile.getAbsolutePath();
    }

    public String createUPIQR(int invoiceId){
        Invoice invoice = repo.getInvoicefromId(invoiceId);
        GetStoreData storeData = new GetStoreData(application);
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa",storeData.getStoreUPIId() )
                .appendQueryParameter("pn", storeData.getStoreName())
                .appendQueryParameter("tn", "Shopping at "+storeData.getStoreName())
                .appendQueryParameter("am", String.valueOf(invoice.getTotal()))
                .appendQueryParameter("cu", "INR")
                .build();
        return uri.toString();
    }

    private Customer addCustomer(String customerName, String customerContactNum, String customerEmail) {
        Customer customer = new Customer();


            customer.setName(customerName);
            customer.setContactNum(customerContactNum);
            customer.setEmail(customerEmail);


        return customer;
    }
    public Customer getCustomerfromId(int id){
        ExecutorService  service = Executors.newSingleThreadExecutor();
        Future<Customer> getCustomer = service.submit(new Callable<Customer>() {
            @Override
            public Customer call() throws Exception {
                return repo.getCustomerfromId(id);
            }
        });

        try {
            return  getCustomer.get();
        } catch (ExecutionException|InterruptedException e) {
            Log.d("Error getting customer from Id",e.getLocalizedMessage().toString());
            throw new RuntimeException(e);
        }
        finally {
            service.shutdown();
        }
    }


    public List<InvoiceItem> getInvoiceItemsfromId(int id) {
        ExecutorService  service = Executors.newSingleThreadExecutor();
        Future<List<InvoiceItem>> getItems = service.submit(new Callable<List<InvoiceItem>>() {
            @Override
            public List<InvoiceItem> call() throws Exception {
                return repo.getInvoiceItemsfromId(id);
            }
        });
        try {
            return getItems.get();
        } catch (ExecutionException|InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            service.shutdown();
        }
    }


    public int getTotalNumofItemsforInvoice(int id, List<InvoiceItem> itemList) {
        AtomicInteger num = new AtomicInteger();
        itemList.forEach(item -> {
            num.addAndGet(item.getItemQty());
        });
        return num.get();
    }

    public InventoryItem getItemfromId(int invoiceItemId) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<InventoryItem> getItem = service.submit(new Callable<InventoryItem>() {
            @Override
            public InventoryItem call() throws Exception {
                return repo.getItemfromId(invoiceItemId);
            }
        });
        try {
            return getItem.get();
        } catch (ExecutionException|InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            service.shutdown();
        }
    }

    public Customer getCustomerfromContactNum(String contactNum) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<Customer> getCustomer  = service.submit(new Callable<Customer>() {
            @Override
            public Customer call() throws Exception {
                return repo.getCustomerfromContactNum(contactNum);
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

    public String getPdfPathfromId(int id) {
        ExecutorService service = Executors.newSingleThreadExecutor();

        Future<String> getPath = service.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {

                return repo.getPdfPathfromId(id);
            }
        });

        try {
            return getPath.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            service.shutdown();
        }

    }

    public LiveData<List<Invoice>> getAllInvoicesforCustomer(int id) {
        return repo.getAllInvoicesforCustomer( id);
    }

    public Invoice getInvoicefromId(int id) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<Invoice> getInvoice = service.submit(new Callable<Invoice>() {
            @Override
            public Invoice call() throws Exception {
                return repo.getInvoicefromId(id);
            }
        });
        try {
            return getInvoice.get();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            service.shutdown();
        }
    }
    public boolean checkifItemisinStock(String qrSKU) {
        return repo.checkIfItemisinStock(qrSKU);
    }
    public void updateInvoice(Invoice invoice) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
                repo.updateInvoice(invoice);
            }
        });
    }

    public String getDueAmountforCustomer(int id) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<Double> getDues = service.submit(new Callable<Double>() {
            @Override
            public Double call() throws Exception {
                return repo.getDueAmountforCustomer(id);
            }
        });
        try {
            return String.valueOf(getDues.get());
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        finally {
            service.shutdown();
        }
    }

    public LiveData<List<Payment>> getPayments(){
        return repo.getallpayments();
    }
}
