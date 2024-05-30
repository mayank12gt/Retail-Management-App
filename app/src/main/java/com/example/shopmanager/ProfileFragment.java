package com.example.shopmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shopmanager.utils.GetStoreData;
import com.google.android.material.textfield.TextInputEditText;

public class ProfileFragment extends Fragment {

    TextInputEditText storeName, storeContact, storeEmail,storeAddr,storeUPIId,storePassword;

    GetStoreData getStoreData;
    public ProfileFragment() {
        // Required empty public constructor
    }


    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      View v= inflater.inflate(R.layout.fragment_profile, container, false);
        storeName = v.findViewById(R.id.store_name_edit_text);
        storeContact = v.findViewById(R.id.contact_num_edit_text);
        storeEmail = v.findViewById(R.id.email_edit_text);
        storeAddr = v.findViewById(R.id.address_edit_text);
        storeUPIId = v.findViewById(R.id.upi_id_edit_text);
        storePassword = v.findViewById(R.id.password_edit_text);
         getStoreData = new GetStoreData(getActivity());

        storeName.setText(getStoreData.getStoreName());
        storeEmail.setText(getStoreData.getStoreEmail());
        storeContact.setText(getStoreData.getStoreContact());
        storeAddr.setText(getStoreData.getStoreAddr());
        storeUPIId.setText(getStoreData.getStoreUPIId());
        storePassword.setText(getStoreData.getStorePassword());
        saveData();

    return v;
    }

    private void saveData() {
        if(storeName.getText().toString().isEmpty()||storeEmail.getText().toString().isEmpty() ||storeAddr.getText().toString().isEmpty()
                ||storeContact.getText().toString().isEmpty()|| storeUPIId.getText().toString().isEmpty()||storePassword.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), "Fill all fields",Toast.LENGTH_LONG).show();
        }
        else{
           getStoreData.setStoreName(storeName.getText().toString());
            getStoreData.setStoreEmail(storeEmail.getText().toString());
            getStoreData.setStoreContact(storeContact.getText().toString());
           getStoreData.setStoreAddr(storeAddr.getText().toString());
            getStoreData.setStoreUPIId(storeUPIId.getText().toString());
            getStoreData.setStorePassword(storePassword.getText().toString());




        }

    }
}