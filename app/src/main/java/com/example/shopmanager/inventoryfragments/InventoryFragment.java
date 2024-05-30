package com.example.shopmanager.inventoryfragments;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shopmanager.R;
import com.example.shopmanager.adapters.StockItemAdapter;
import com.example.shopmanager.models.InventoryItem;
import com.example.shopmanager.repo.Repo;
import com.example.shopmanager.viewmodels.InventoryViewModel;
import com.example.shopmanager.webfragment.SyncData;
import com.example.shopmanager.webfragment.WebPageItem;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;
import java.util.List;


public class InventoryFragment extends Fragment implements OnInventoryItemClicked {

//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    RecyclerView stockItemsRv;
    CircularProgressIndicator loadingIndicator;
    MaterialButton addItemBtn;
    FloatingActionButton filterItemsBtn;
    SearchView searchItemsView;
    StockItemAdapter adapter;
    List<InventoryItem> inventoryItems;

    InventoryViewModel viewModel;


    public InventoryFragment() {
        // Required empty public constructor
    }


    public static InventoryFragment newInstance() {
        InventoryFragment fragment = new InventoryFragment();
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
        inventoryItems = new ArrayList<>();
        viewModel = new ViewModelProvider(this).get(InventoryViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      View v= inflater.inflate(R.layout.fragment_inventory, container, false);
      loadingIndicator = v.findViewById(R.id.loading_items);
      addItemBtn = v.findViewById(R.id.add_item_btn);
      stockItemsRv = v.findViewById(R.id.items_rv);

        setupRecyclerView();
        setupAddBtn();

        SyncData syncData = new SyncData(new Repo(getActivity().getApplication()));
        viewModel.getAllItemsinInventory().observe(getViewLifecycleOwner(), new Observer<List<InventoryItem>>() {
            @Override
            public void onChanged(List<InventoryItem> inventoryItems) {
                List<InventoryItem> items = new ArrayList<>();
                for (InventoryItem it:
                     inventoryItems) {
                    if(it.getWebpage_id()==null){
                        items.add(it);
                    }
                }
                if(items.size()>0) {
                    syncData.addItemstoWebpage(items);
                }
            }
        });






      return v;
    }

    private void setupAddBtn() {

        addItemBtn.setOnClickListener(view -> {
            AddMethodSelectBottomSheet addMethodSelectBottomSheet = new AddMethodSelectBottomSheet();
            addMethodSelectBottomSheet.show(getParentFragmentManager(),addMethodSelectBottomSheet.getTag());

        });
    }

    private void setupRecyclerView() {
        adapter = new StockItemAdapter(inventoryItems,this::onInventoryItemClicked);
        stockItemsRv.setLayoutManager(new LinearLayoutManager(getContext()));
        stockItemsRv.setAdapter(adapter);
        getAllInventoryItems();
    }

    private void getAllInventoryItems() {
        setLoadingStatus();
        viewModel.getAllItemsinInventory().observe(getViewLifecycleOwner(), new Observer<List<InventoryItem>>() {
            @Override
            public void onChanged(List<InventoryItem> inventoryItems) {
                adapter.update(inventoryItems);
            }
        });

    }

    private void setLoadingStatus() {

    }

    @Override
    public void onInventoryItemClicked(InventoryItem item) {
        getParentFragmentManager().beginTransaction().addToBackStack("inventory_frag_tag").replace(R.id.frame,ItemDetailsFragment.newInstance(item)).commit();
    }
}