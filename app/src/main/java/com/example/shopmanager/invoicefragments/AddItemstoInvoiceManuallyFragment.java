package com.example.shopmanager.invoicefragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shopmanager.R;
import com.example.shopmanager.adapters.AddItemsManuallyAdapter;
import com.example.shopmanager.models.InventoryItem;
import com.example.shopmanager.viewmodels.InventoryViewModel;
import com.example.shopmanager.viewmodels.InvoicesViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class AddItemstoInvoiceManuallyFragment extends Fragment implements OnAddBtnClicked,OnRemoveBtnClicked {
    private static final String ARG_INVOICE_CODES_LIST = "invoice_bar_codes";
    public static final String ARG_ADD_ITEMS_MANUALLY_FRAG = "add_customer_to_invoice";
    private static final String CODE_INVOICE_MANUAL_FRAGMENT ="add_items_manually" ;

    List<String> codesList;
    List<InventoryItem> itemList;

    InvoicesViewModel invoicesViewModel;
    InventoryViewModel inventoryViewModel;
    RecyclerView itemsRv;
    AddItemsManuallyAdapter adapter;

    MaterialButton nextBtn;

    public AddItemstoInvoiceManuallyFragment() {

    }


    public static AddItemstoInvoiceManuallyFragment newInstance(List<String> codesList) {
        AddItemstoInvoiceManuallyFragment fragment = new AddItemstoInvoiceManuallyFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_INVOICE_CODES_LIST, (ArrayList<String>) codesList);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            codesList = getArguments().getStringArrayList(ARG_INVOICE_CODES_LIST);

        }
        itemList = new ArrayList<>();

        invoicesViewModel = new ViewModelProvider(this).get(InvoicesViewModel.class);
        inventoryViewModel = new ViewModelProvider(this).get(InventoryViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View v= inflater.inflate(R.layout.fragment_add_itemsto_invoice_manually, container, false);
        itemsRv = v.findViewById(R.id.items_rv);
        adapter = new AddItemsManuallyAdapter(itemList,this::addBtnClicked,this::removeBtnClicked);
        nextBtn = v.findViewById(R.id.next_btn);

        itemsRv.setAdapter(adapter);
        itemsRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        inventoryViewModel.getNonBarcodeandInStockItemsinInventory().observe(getViewLifecycleOwner(), new Observer<List<InventoryItem>>() {
            @Override
            public void onChanged(List<InventoryItem> inventoryItems) {
                adapter.update(inventoryItems);
            }
        });

        nextBtn.setOnClickListener(view -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .addToBackStack(CODE_INVOICE_MANUAL_FRAGMENT)
                    .replace(R.id.frame,AddCustomertoInvoiceFragment.newInstance(codesList))
                    .commit();
        });


    return v;
    }

    @Override
    public void addBtnClicked(InventoryItem item) {
        if(invoicesViewModel.checkifItemisinStock(item.getSKU()))
        {


            codesList.add(item.getSKU());
        }
        else {
            Toast.makeText(getActivity(),"Item is not in stock",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void removeBtnClicked(InventoryItem item) {
        if(codesList.contains(item.getSKU())) {
            codesList.remove(item.getSKU());
        }
    }
}