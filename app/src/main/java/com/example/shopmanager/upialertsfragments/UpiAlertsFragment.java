package com.example.shopmanager.upialertsfragments;

import android.Manifest;
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
import com.example.shopmanager.adapters.PaymentsAdapter;
import com.example.shopmanager.models.Payment;
import com.example.shopmanager.viewmodels.InvoicesViewModel;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;


public class UpiAlertsFragment extends Fragment {


    RecyclerView rv;
    InvoicesViewModel vm;
    public UpiAlertsFragment() {
        // Required empty public constructor
    }


    public static UpiAlertsFragment newInstance() {
        UpiAlertsFragment fragment = new UpiAlertsFragment();
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
        vm = new ViewModelProvider(this).get(InvoicesViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      View v= inflater.inflate(R.layout.fragment_upi_alerts, container, false);
      rv  =v.findViewById(R.id.payments_rv);
      List<Payment> payments = new ArrayList<>();
        payments.add(new Payment(125,1700022100));
        payments.add(new Payment(200,1700022300));
        PaymentsAdapter adapter = new PaymentsAdapter(payments);

        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(adapter);

//        vm.getPayments().observe(getViewLifecycleOwner(), new Observer<List<Payment>>() {
//            @Override
//            public void onChanged(List<Payment> payments) {
//                adapter.update(payments);
//            }
//        });


        Dexter.withContext(getActivity())
                .withPermissions(
                        Manifest.permission.READ_SMS,
                        Manifest.permission.RECEIVE_SMS

                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        Log.d("permission","granted");
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                    }
                }).check();
      return v;
    }
}