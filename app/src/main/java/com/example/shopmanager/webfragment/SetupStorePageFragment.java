package com.example.shopmanager.webfragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.shopmanager.R;


public class SetupStorePageFragment extends Fragment {


    public SetupStorePageFragment() {

    }


    public static SetupStorePageFragment newInstance() {
        SetupStorePageFragment fragment = new SetupStorePageFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_setup_store_page, container, false);



         return v;
    }
}