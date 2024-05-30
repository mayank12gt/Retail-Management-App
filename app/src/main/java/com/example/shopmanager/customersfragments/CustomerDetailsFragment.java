package com.example.shopmanager.customersfragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shopmanager.R;
import com.example.shopmanager.adapters.InvoiceItemsAdapter;
import com.example.shopmanager.adapters.InvoicesAdapter;
import com.example.shopmanager.invoicefragments.InvoiceDetailsFragment;
import com.example.shopmanager.invoicefragments.OnInvoiceItemClicked;
import com.example.shopmanager.invoicefragments.SetupInvoiceItemLayout;
import com.example.shopmanager.invoicefragments.SetupInvoiceItemListItemLayout;
import com.example.shopmanager.models.Customer;
import com.example.shopmanager.models.InventoryItem;
import com.example.shopmanager.models.PaidStatus;
import com.example.shopmanager.models.invoice.Invoice;
import com.example.shopmanager.models.invoice.InvoiceItem;
import com.example.shopmanager.utils.Utils;
import com.example.shopmanager.viewmodels.InvoicesViewModel;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;


public class CustomerDetailsFragment extends Fragment implements SetupInvoiceItemLayout, SetupInvoiceItemListItemLayout, OnInvoiceItemClicked {

//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
     static final String ARG_CUSTOMER_FRAG = "customer_frag";
    private static final String ARG_CUSTOMER = "customer";
//
//    // TODO: Rename and change types of parameters
    private Customer customer;
//    private String mParam2;

    InvoicesViewModel viewModel;

    List<Invoice> invoiceList;
    InvoicesAdapter adapter ;
    RecyclerView invoiceItemsRv;
    TextView customerName;
    TextView customerContactNum;
    TextView customerEmail;
    TextView customerNumInvoices;
    TextView customerDueAmount;

    public CustomerDetailsFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static CustomerDetailsFragment newInstance(Customer customer) {
        CustomerDetailsFragment fragment = new CustomerDetailsFragment();
        Bundle args = new Bundle();
       args.putSerializable(ARG_CUSTOMER, customer);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           customer = (Customer) getArguments().getSerializable(ARG_CUSTOMER);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        viewModel = new ViewModelProvider(this).get(InvoicesViewModel.class);
        invoiceList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View v= inflater.inflate(R.layout.fragment_customer_details, container, false);
        invoiceItemsRv = v.findViewById(R.id.invoices_rv);
        customerName = v.findViewById(R.id.customer_name);
        customerContactNum = v.findViewById(R.id.customer_contact_num);
        customerEmail = v.findViewById(R.id.customer_email);
        customerNumInvoices = v.findViewById(R.id.customer_num_invoices);
        customerDueAmount = v.findViewById(R.id.customer_due_amount);

        customerName.setText(customer.getName());
        customerContactNum.setText(customer.getContactNum());
        customerEmail.setText(customer.getEmail());

        customerDueAmount.setText("Due amount: ₹ "+viewModel.getDueAmountforCustomer(customer.getId()));


        setupRecyclerView();
    return v;
    }
    private void setupRecyclerView() {
        adapter = new InvoicesAdapter(invoiceList, this::setupInvoiceItemLayout,this::OnInvoiceItemClicked);
        invoiceItemsRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        invoiceItemsRv.setAdapter(adapter);
        getInvoices();
    }
    private void getInvoices() {
        viewModel.getAllInvoicesforCustomer(customer.getId()).observe(getViewLifecycleOwner(), new Observer<List<Invoice>>() {
            @Override
            public void onChanged(List<Invoice> invoiceList) {
                customerNumInvoices.setText(String.valueOf(invoiceList.size())+" invoices");
                adapter.update(invoiceList);
            }
        });
    }
    @Override
    public void setupInvoiceItemLayout(Invoice invoice, TextView customerName, TextView grandTotal, TextView numItems, TextView invoiceDate, RecyclerView invoiceItemsRv, Chip paymentStatusChip) {
        Customer customer = viewModel.getCustomerfromId(invoice.getCustomer_id());
        List<InvoiceItem> items = viewModel.getInvoiceItemsfromId(invoice.getId());
        int num = viewModel.getTotalNumofItemsforInvoice(invoice.getId(),items);



        customerName.setText(customer.getName());
        grandTotal.setText("₹ "+String.valueOf(invoice.getTotal()));
        invoiceDate.setText(Utils.formatDate(invoice.getTimeStamp()));
        numItems.setText(String.valueOf(num)+" items");
        items.forEach(item -> {
            Log.d("invoice items",String.valueOf(item.getInvoice_id()));
        });

        if(invoice.getStatus()== PaidStatus.PAID){
            paymentStatusChip.setText("Paid");

            paymentStatusChip.setBackgroundColor(getResources().getColor(R.color.green_1,getActivity().getTheme()));

        } else if (invoice.getStatus()== PaidStatus.UNPAID) {
            paymentStatusChip.setText("Unpaid");

            paymentStatusChip.setBackgroundColor(getResources().getColor(R.color.red_1,getActivity().getTheme()));
        }

        InvoiceItemsAdapter itemsAdapter = new InvoiceItemsAdapter(items,this,ARG_CUSTOMER_FRAG);
        invoiceItemsRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        invoiceItemsRv.setAdapter(itemsAdapter);

    }


    @Override
    public void SetupInvoiceItemListItemLayout(List<InvoiceItem> items,int position, TextView itemNo, TextView itemName, TextView itemQty, TextView itemTotal) {
        InvoiceItem item = items.get(position);

        InventoryItem inventoryItem = viewModel.getItemfromId(item.getInvoice_item_id());
        itemNo.setText(String.valueOf(position+1));
        itemName.setText(inventoryItem.getName());
        itemQty.setText(String.valueOf("x"+item.getItemQty()));
        itemTotal.setText(String.valueOf(inventoryItem.getPurchasePrice()*item.getItemQty()));


    }

    @Override
    public void OnInvoiceItemClicked(Invoice invoice) {
        getParentFragmentManager().beginTransaction().addToBackStack(ARG_CUSTOMER_FRAG).replace(R.id.frame, InvoiceDetailsFragment.newInstance(invoice)).commit();
    }


}