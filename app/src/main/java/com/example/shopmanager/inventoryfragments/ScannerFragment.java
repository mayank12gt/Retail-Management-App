package com.example.shopmanager.inventoryfragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.shopmanager.MainActivity;
import com.example.shopmanager.R;
import com.example.shopmanager.utils.JsonParser;
import com.example.shopmanager.viewmodels.InventoryViewModel;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ScannerFragment extends Fragment {


   //private static final String ARG_REQUESTCODE = "requestcode";
    //   private static final String ARG_PARAM2 = "param2";




    private CodeScanner mCodeScanner;
    CodeScannerView scannerView;
    InventoryViewModel viewModel;

    public ScannerFragment() {
        // Required empty public constructor
    }


    public static ScannerFragment newInstance() {
        ScannerFragment fragment = new ScannerFragment();
        Bundle args = new Bundle();

//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        viewModel = new ViewModelProvider(this).get(InventoryViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

    View v= inflater.inflate(R.layout.fragment_scanner, container, false);
        scannerView = v.findViewById(R.id.barcode_scanner);
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
        List<String> codes = new ArrayList<>();

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // You can customize the format as needed
//
//
//                        Date date = new Date(result.getTimestamp());
//
//                        String formattedTime = sdf.format(date);
//                        Toast.makeText(getActivity(), formattedTime, Toast.LENGTH_SHORT).show();
//                        Log.d("result",String.valueOf(result.getNumBits()));
//                        Log.d("result", String.valueOf(result.getBarcodeFormat()));
//                        Log.d("result",String.valueOf(result.getRawBytes()));
//                        Log.d("result",String.valueOf(result.getResultPoints()));
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
        List<String> codesList = new ArrayList<>();

            if (viewModel.checkIfBarcodeAlreadyExists(result.getText())) {
                Log.d("barcode", "already exists");
                Snackbar.make(scannerView, "Barcode already exists in inventory. Add a new barcode", BaseTransientBottomBar.LENGTH_INDEFINITE)
                        .setAction("Ok", view -> {
                            mCodeScanner.startPreview();
                        }).setAnchorView(R.id.bottom_nav_bar).show();
            } else {

                codesList.add(result.getText());
                getParentFragmentManager()
                        .beginTransaction()
                        .addToBackStack("scanner_frag_tag")
                        .replace(R.id.frame, AddItemFragment.newInstance(codesList))
                        .commit();
            }
        }



}