package com.example.shopmanager.customersfragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shopmanager.R;
import com.example.shopmanager.models.Customer;
import com.example.shopmanager.viewmodels.CustomersViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AddCustomerFragment extends Fragment {

//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    TextInputEditText nameEditText;
    TextInputEditText contactNumEditText;
    TextInputEditText emailEditText;
    MaterialButton addCustomersButton;
    CustomersViewModel viewModel;



    public AddCustomerFragment() {
        // Required empty public constructor
    }


    public static AddCustomerFragment newInstance() {
        AddCustomerFragment fragment = new AddCustomerFragment();
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
        viewModel = new ViewModelProvider(this).get(CustomersViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View v=  inflater.inflate(R.layout.fragment_add_customer, container, false);
        nameEditText = v.findViewById(R.id.add_name_edit_text);
        contactNumEditText = v.findViewById(R.id.add_contact_edit_text);
        emailEditText = v.findViewById(R.id.add_email_edit_text);

        addCustomersButton = v.findViewById(R.id.add_customer_btn);

        addCustomersButton.setOnClickListener(view -> {
            Customer customer = createCustomer();
            viewModel.addCustomer(customer);
            getParentFragmentManager().popBackStack("customers_frag_tag", FragmentManager.POP_BACK_STACK_INCLUSIVE);

        });



   return v;
    }

    private Customer createCustomer() {
        Customer customer = new Customer();
        if(nameEditText.getText().toString().trim().isEmpty()){
            Toast.makeText(getActivity(),"Item name cannot be empty",Toast.LENGTH_LONG);
        }
        else if(contactNumEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(getActivity(),"Purchase Price cannot be empty",Toast.LENGTH_LONG);
        }

        else {
            customer.setName(nameEditText.getText().toString());
            customer.setContactNum(contactNumEditText.getText().toString());
            customer.setEmail(emailEditText.getText().toString());
        }
        return customer;
    }
}