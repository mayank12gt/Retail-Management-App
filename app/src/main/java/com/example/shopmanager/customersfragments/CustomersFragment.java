package com.example.shopmanager.customersfragments;

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
import com.example.shopmanager.adapters.CustomersAdapter;
import com.example.shopmanager.models.Customer;
import com.example.shopmanager.viewmodels.CustomersViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;
import java.util.List;


public class CustomersFragment extends Fragment implements OnCustomerItemClicked{

//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    RecyclerView customerItemsRv;
    CircularProgressIndicator loadingCustomers;
    MaterialButton addCustomerBtn;
    FloatingActionButton filterCustomersBtn;
    SearchView searchCustomersView;
    CustomersViewModel viewModel;
    List<Customer> customerList;
    CustomersAdapter adapter;

    public CustomersFragment() {
        // Required empty public constructor
    }


    public static CustomersFragment newInstance() {
        CustomersFragment fragment = new CustomersFragment();
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
        customerList = new ArrayList<>();
        viewModel = new ViewModelProvider(this).get(CustomersViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_customers, container, false);
        customerItemsRv = v.findViewById(R.id.customers_rv);
        addCustomerBtn = v.findViewById(R.id.add_customer_btn);

        setupRecyclerView();

        addCustomerBtn.setOnClickListener(view -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .addToBackStack("customers_frag_tag")
                    .replace(R.id.frame, AddCustomerFragment.newInstance())
                    .commit();
        });

        return v;
    }

    private void setupRecyclerView() {
        customerItemsRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CustomersAdapter(customerList, this::onCustomerItemClicked);
        customerItemsRv.setAdapter(adapter);

        getCustomersList();


    }

    private void getCustomersList() {
        viewModel.getAllCustomers().observe(getViewLifecycleOwner(), new Observer<List<Customer>>() {
            @Override
            public void onChanged(List<Customer> customers) {
                adapter.update(customers);
            }
        });
    }

    @Override
    public void onCustomerItemClicked(Customer customer) {
        getParentFragmentManager()
                .beginTransaction()
                .addToBackStack("customers_frag_tag")
                .replace(R.id.frame,CustomerDetailsFragment.newInstance(customer)).commit();

    }
}