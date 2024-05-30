package com.example.shopmanager.inventoryfragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.shopmanager.R;
import com.example.shopmanager.models.InventoryItem;
import com.example.shopmanager.utils.JsonParser;
import com.example.shopmanager.viewmodels.InventoryViewModel;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;

public class AddItembyQRFragment extends Fragment {

    private CodeScanner mCodeScanner;
    CodeScannerView scannerView;
    InventoryViewModel viewModel;

    public AddItembyQRFragment() {
        // Required empty public constructor
    }

// TODO: Rename and change types and number of parameters
    public static AddItembyQRFragment newInstance() {
        AddItembyQRFragment fragment = new AddItembyQRFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        viewModel = new ViewModelProvider(this).get(InventoryViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      View v=  inflater.inflate(R.layout.fragment_add_itemby_q_r, container, false);
        scannerView = v.findViewById(R.id.qrcode_scanner);
        mCodeScanner = new CodeScanner(getActivity(), scannerView);


        Dexter.withContext(getActivity())
                .withPermission(android.Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        scanQR();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    private void scanQR() {
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        addItem(result);

                    }
                });
            }
        });


        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();

            }
        });
    }

    private void addItem(Result result) {
        Log.d("json", result.getText());
        InventoryItem item = JsonParser.parseJson(result.getText());

        if (viewModel.checkIfItemAlreadyExists(item.getSKU())) {
            InventoryItem item1 = viewModel.getItemfromSKU(item.getSKU());
            getParentFragmentManager()
                    .beginTransaction()
                    .addToBackStack("scanner_frag_tag")
                    .replace(R.id.frame, ItemDetailsFragment.newInstance(item1))
                    .commit();

        } else {
            item.setAvailableStock(1);

            getParentFragmentManager()
                    .beginTransaction()
                    .addToBackStack("scanner_frag_tag")
                    .replace(R.id.frame, AddItemtoInventorybyQRFragment.newInstance(item))
                    .commit();

        }
    }
}