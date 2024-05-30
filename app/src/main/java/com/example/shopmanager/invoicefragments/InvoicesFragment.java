package com.example.shopmanager.invoicefragments;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shopmanager.R;
import com.example.shopmanager.adapters.InvoiceItemsAdapter;
import com.example.shopmanager.adapters.InvoicesAdapter;
import com.example.shopmanager.models.Customer;
import com.example.shopmanager.models.InventoryItem;
import com.example.shopmanager.models.PaidStatus;
import com.example.shopmanager.models.invoice.Invoice;
import com.example.shopmanager.models.invoice.InvoiceItem;
import com.example.shopmanager.utils.Utils;
import com.example.shopmanager.viewmodels.InvoicesViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;
import java.util.List;


public class InvoicesFragment extends Fragment implements SetupInvoiceItemLayout,SetupInvoiceItemListItemLayout, OnInvoiceItemClicked{


   static final String ARG_INVOICES_FRAGMENT = "invoices_fragment";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    RecyclerView invoiceItemsRv;
    CircularProgressIndicator loadingInvoices;
    MaterialButton addInvoicesBtn;
    FloatingActionButton filterInvoicesBtn;
    SearchView searchInvoicesView;

    InvoicesViewModel viewModel;

    List<Invoice> invoiceList;
    InvoicesAdapter adapter ;





    public InvoicesFragment() {
        // Required empty public constructor
    }


    public static InvoicesFragment newInstance() {
        InvoicesFragment fragment = new InvoicesFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        viewModel = new ViewModelProvider(this).get(InvoicesViewModel.class);
        invoiceList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      View v= inflater.inflate(R.layout.fragment_invoices, container, false);
        addInvoicesBtn = v.findViewById(R.id.add_invoice_btn);
        searchInvoicesView = v.findViewById(R.id.invoices_search_view);
        filterInvoicesBtn = v.findViewById(R.id.filter_invoices_btn);
        invoiceItemsRv = v.findViewById(R.id.invoices_rv);


        setupRecyclerView();

        addInvoicesBtn.setOnClickListener(view -> {
        getParentFragmentManager()
                .beginTransaction()
                .addToBackStack(ARG_INVOICES_FRAGMENT)
                .replace(R.id.frame, AddItemstoInvoiceScannerFragment.newInstance())
                .commit();

        });


      return v;
    }

    private void setupRecyclerView() {
        adapter = new InvoicesAdapter(invoiceList,this::setupInvoiceItemLayout, this::OnInvoiceItemClicked);
        invoiceItemsRv.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        invoiceItemsRv.setAdapter(adapter);
        getInvoices();
    }

    private void getInvoices() {
        viewModel.getAllInvoices().observe(getViewLifecycleOwner(), new Observer<List<Invoice>>() {
            @Override
            public void onChanged(List<Invoice> invoiceList) {
                adapter.update(invoiceList);
            }
        });
    }


    @Override
    public void setupInvoiceItemLayout(Invoice invoice, TextView customerName, TextView grandTotal, TextView numItems, TextView invoiceDate, RecyclerView invoiceItemsRv, Chip paymentStatusChip) {
        Customer customer = viewModel.getCustomerfromId(invoice.getCustomer_id());
        List<InvoiceItem> items = viewModel.getInvoiceItemsfromId(invoice.getId());
        int num = viewModel.getTotalNumofItemsforInvoice(invoice.getId(),items);
        if(invoice.getStatus()== PaidStatus.PAID){
            paymentStatusChip.setText("Paid");

            paymentStatusChip.setBackgroundColor(getResources().getColor(R.color.green_1,getActivity().getTheme()));

        } else if (invoice.getStatus()== PaidStatus.UNPAID) {
            paymentStatusChip.setText("Unpaid");

            paymentStatusChip.setBackgroundColor(getResources().getColor(R.color.red_1,getActivity().getTheme()));
        }

        customerName.setText(customer.getName());
        grandTotal.setText("₹ "+String.valueOf(invoice.getTotal()));
        invoiceDate.setText(Utils.formatDate(invoice.getTimeStamp()));
        numItems.setText(String.valueOf(num)+" items");
        items.forEach(item -> {
            Log.d("invoice items",String.valueOf(item.getInvoice_id()));
        });

        InvoiceItemsAdapter itemsAdapter = new InvoiceItemsAdapter(items,this,ARG_INVOICES_FRAGMENT);
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
        itemTotal.setText(String.valueOf(inventoryItem.getSellingPrice()*item.getItemQty()));


    }

    @Override
    public void OnInvoiceItemClicked(Invoice invoice) {
        getParentFragmentManager().beginTransaction().addToBackStack(ARG_INVOICES_FRAGMENT).replace(R.id.frame, InvoiceDetailsFragment.newInstance(invoice)).commit();
    }
}