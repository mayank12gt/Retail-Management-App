package com.example.shopmanager.webfragment;

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

import com.example.shopmanager.R;
import com.example.shopmanager.adapters.AlertAdapter;
import com.example.shopmanager.inventoryfragments.ItemDetailsFragment;
import com.example.shopmanager.inventoryfragments.OnInventoryItemClicked;
import com.example.shopmanager.models.Alert;
import com.example.shopmanager.models.InventoryItem;
import com.example.shopmanager.viewmodels.InventoryViewModel;

import java.util.ArrayList;
import java.util.List;


public class AlertsFragment extends Fragment implements OnInventoryItemClicked {


    RecyclerView alertRV;
    InventoryViewModel viewModel;


    public static AlertsFragment newInstance() {
        AlertsFragment fragment = new AlertsFragment();
        Bundle args = new Bundle();

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
        viewModel = new ViewModelProvider(this).get(InventoryViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View v= inflater.inflate(R.layout.fragment_alerts, container, false);

        alertRV = v.findViewById(R.id.alerts_rv);
        List<Alert> alerts = new ArrayList<>();
        AlertAdapter adapter = new AlertAdapter(alerts,this::onInventoryItemClicked);

        alertRV.setAdapter(adapter);
        alertRV.setLayoutManager(new LinearLayoutManager(getActivity()));

        viewModel.getOutofStockItems().observe(getViewLifecycleOwner(), new Observer<List<InventoryItem>>() {
            @Override
            public void onChanged(List<InventoryItem> inventoryItems) {
                for (InventoryItem it:
                     inventoryItems) {
                    Log.d("alert",it.getName());
                    if(it.getAvailableStock()<1) {
                        alerts.add(new Alert(it, it.getName() + " is out of stock"));
                    }
                    else{
                        alerts.add(new Alert(it, it.getName() + " is about to go out of stock"));
                    }
                }
                adapter.update(alerts);
            }
        });

    return v;
    }

    @Override
    public void onInventoryItemClicked(InventoryItem item) {
        getParentFragmentManager().beginTransaction().addToBackStack("alerts_frag_tag").replace(R.id.frame, ItemDetailsFragment.newInstance(item)).commit();
    }
}