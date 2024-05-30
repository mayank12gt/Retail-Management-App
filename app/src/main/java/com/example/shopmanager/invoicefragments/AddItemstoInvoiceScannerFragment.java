package com.example.shopmanager.invoicefragments;

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
import com.example.shopmanager.utils.JsonParser;
import com.example.shopmanager.viewmodels.InventoryViewModel;
import com.example.shopmanager.viewmodels.InvoicesViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;

public class AddItemstoInvoiceScannerFragment extends Fragment {

    public static final String CODE_INVOICE_SCANNER_FRAGMENT="invoice_scanner_fragment";

    private CodeScanner mCodeScanner;
    CodeScannerView scannerView;
    InvoicesViewModel viewModel;
    MaterialButton done_btn,addItemsManuallyBtn;
    List<String> codesList;

    public AddItemstoInvoiceScannerFragment() {

    }


    public static AddItemstoInvoiceScannerFragment newInstance() {
        AddItemstoInvoiceScannerFragment fragment = new AddItemstoInvoiceScannerFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        codesList = new ArrayList<>();
        viewModel = new ViewModelProvider(this).get(InvoicesViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      View v= inflater.inflate(R.layout.fragment_add_itemsto_invoice_scanner, container, false);
        scannerView = v.findViewById(R.id.barcode_scanner);
        mCodeScanner = new CodeScanner(getActivity(), scannerView);
      //  done_btn = v.findViewById(R.id.done_btn);
        addItemsManuallyBtn = v.findViewById(R.id.add_manually_btn);


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


//        done_btn.setOnClickListener(view -> {
//
//        });

        addItemsManuallyBtn.setOnClickListener(view -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .addToBackStack(CODE_INVOICE_SCANNER_FRAGMENT)
                    .replace(R.id.frame,AddItemstoInvoiceManuallyFragment.newInstance(codesList))
                    .commit();
        });



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
        List<String> codes = new ArrayList<>();

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Log.d("codeval",result.getText());
                        Log.d("codeval",result.getText());
                        addBarcode(result);

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

    private void addBarcode(Result result) {
        if (result.getBarcodeFormat().equals(BarcodeFormat.QR_CODE)) {



            String sku = JsonParser.parseJson(result.getText()).getSKU();
            if(viewModel.checkifItemisinStock(sku)){
                codesList.add(sku);
            }
            else{
                Snackbar.make(scannerView, "Item is either out of stock or not in inventory", BaseTransientBottomBar.LENGTH_INDEFINITE)
                        .setAction("Ok", view -> {
                            mCodeScanner.startPreview();
                        }).setAnchorView(R.id.add_manually_btn).show();
            }

        } else {
            if (viewModel.checkIfBarcodeAlreadyExists(result.getText())) {
                if (!codesList.contains(result.getText())) {
                    codesList.add(result.getText());
                    Log.d("invoicebarcodeadded", result.getText());
                    Snackbar.make(scannerView, "Barcode added to invoice", BaseTransientBottomBar.LENGTH_INDEFINITE)
                            .setAction("Ok", view -> {
                                mCodeScanner.startPreview();
                            }).setAnchorView(R.id.add_manually_btn).show();
                } else {
                    Log.d("invoicebarcodeadded", "already added");
                    Snackbar.make(scannerView, "Barcode already added. Add a different barcode", BaseTransientBottomBar.LENGTH_INDEFINITE)
                            .setAction("Ok", view -> {
                                mCodeScanner.startPreview();
                            }).setAnchorView(R.id.add_manually_btn).show();
                }
            } else {
                Log.d("invoicebarcodeadded", "doesn't exists");
                Snackbar.make(scannerView, "Barcode doesn't exists. Add a different barcode", BaseTransientBottomBar.LENGTH_INDEFINITE)
                        .setAction("Ok", view -> {
                            mCodeScanner.startPreview();
                        }).setAnchorView(R.id.add_manually_btn).show();
            }

        }
    }
    }
