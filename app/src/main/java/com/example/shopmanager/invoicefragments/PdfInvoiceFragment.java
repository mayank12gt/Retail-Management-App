package com.example.shopmanager.invoicefragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shopmanager.R;
import com.example.shopmanager.viewmodels.InvoicesViewModel;


public class PdfInvoiceFragment extends Fragment {

//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
   private static final String ARG_INVOICE_ID = "invoice_id";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
    private int id;
    InvoicesViewModel viewModel;
//    private String mParam2;

    public PdfInvoiceFragment() {
        // Required empty public constructor
    }


    public static PdfInvoiceFragment newInstance(int invoiceId) {
        PdfInvoiceFragment fragment = new PdfInvoiceFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_INVOICE_ID, invoiceId);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
          id = getArguments().getInt(ARG_INVOICE_ID);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        viewModel = new ViewModelProvider(this).get(InvoicesViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v=  inflater.inflate(R.layout.fragment_pdf_invoice, container, false);
        String path = viewModel.getPdfPathfromId(id);


    return v;
    }
}