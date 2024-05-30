package com.example.shopmanager.invoicefragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shopmanager.R;
import com.example.shopmanager.adapters.InvoiceItemsAdapter;
import com.example.shopmanager.models.Customer;
import com.example.shopmanager.viewmodels.CustomersViewModel;
import com.example.shopmanager.viewmodels.InvoicesViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AddCustomertoInvoiceFragment extends Fragment {


    private static final String ARG_INVOICE_CODES_LIST = "invoice_bar_codes";
    public static final String ARG_ADD_CUSTOMER_TO_INVOICE_FRAG = "add_customer_to_invoice";

    TextInputEditText nameEditText;
    TextInputEditText contactNumEditText;
    TextInputEditText emailEditText;
    MaterialButton generateInvoiceBtn;

    List<String> codesList;
    CustomersViewModel customersViewModel;
    InvoicesViewModel invoicesViewModel;

    public AddCustomertoInvoiceFragment() {
        // Required empty public constructor
    }


    public static AddCustomertoInvoiceFragment newInstance(List<String> codesList) {
        AddCustomertoInvoiceFragment fragment = new AddCustomertoInvoiceFragment();
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
        //customersViewModel = new ViewModelProvider(this).get(CustomersViewModel.class);
        invoicesViewModel = new ViewModelProvider(this).get(InvoicesViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
     View v= inflater.inflate(R.layout.fragment_add_customerto_invoice, container, false);
        nameEditText = v.findViewById(R.id.add_name_edit_text);
        contactNumEditText = v.findViewById(R.id.add_contact_edit_text);
        emailEditText = v.findViewById(R.id.add_email_edit_text);
       // reviewItemsRv = v.findViewById(R.id.review_items_rv);
        String phoneNum="";

        Handler handler = new Handler(Looper.getMainLooper());
        Runnable autofilll = new Runnable() {
            @Override
            public void run() {
                if(!phoneNum.isEmpty()) {
                    Customer customer = customersViewModel.getCustomerfromContactNum(phoneNum);
                    Log.d("customerautofill",customer.getName());
                    nameEditText.setText(customer.getName());
                    emailEditText.setText(customer.getEmail());
                } else if (phoneNum.isEmpty()) {
                    nameEditText.setText("");
                    emailEditText.setText("");

                }
            }
        };

        contactNumEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            handler.removeCallbacks(autofilll);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
            String phoneNum = editable.toString();
            Log.d("phoneNum",phoneNum);
             handler.postDelayed(autofilll,2000);
            }
        });


        generateInvoiceBtn = v.findViewById(R.id.generate_invoice_btn);

        generateInvoiceBtn.setOnClickListener(view -> {


            Log.d("CustomerInfo",nameEditText.getText().toString());
            Log.d("CustomerInfo",contactNumEditText.getText().toString());
            Log.d("CustomerInfo",emailEditText.getText().toString());
            for (String code: codesList) {
                Log.d("invoiceBarcodeadded",code);
            }
           int id=  generateInvoice();
            if(id!=-1){
                getParentFragmentManager().
                        beginTransaction().
                        addToBackStack(ARG_ADD_CUSTOMER_TO_INVOICE_FRAG).
                        replace(R.id.frame,
                                InvoiceDetailsFragment.newInstance(invoicesViewModel.getInvoicefromId(id))).
                        commit();
            }


        });

    return v;
    }

    private int generateInvoice() {

        int id = -1;
        if (nameEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(getActivity(), "Customer Name cannot be empty", Toast.LENGTH_LONG);
        } else if (contactNumEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(getActivity(), "Contact number cannot be empty", Toast.LENGTH_LONG);
        } else {
             id= invoicesViewModel.generateInvoiceData(nameEditText.getText().toString(), contactNumEditText.getText().toString(), emailEditText.getText().toString(), codesList);

        }

        return id;
    }




}