package com.example.shopmanager.inventoryfragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shopmanager.R;
import com.example.shopmanager.models.InventoryItem;
import com.example.shopmanager.viewmodels.InventoryViewModel;
import com.example.shopmanager.viewmodels.InvoicesViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;


public class ItemDetailsFragment extends Fragment {

    private static final String ARG_ITEM = "param1";
    public static final String ITEM_DETAILS_FRAG_TAG = "item_details_frag_tag";

private InventoryItem item;
    RoundedImageView itemImage;
    TextView itemName;
    TextView availableStock;
    TextView lowStockAlert;
    TextView purchasePrice;

    TextView totalCost;
    ExtendedFloatingActionButton editBtn;


    TextView sellingPrice;
    TextView totalValue;
    TextView descHeading;
    TextView desc;

    MaterialButton addMoreItemsBtn;

    List<String> codesList;
    InventoryViewModel viewModel;
    ImageView addUnitBtn,removeUnitBtn;



    public ItemDetailsFragment() {
        // Required empty public constructor
    }


    public static ItemDetailsFragment newInstance(InventoryItem item) {
        ItemDetailsFragment fragment = new ItemDetailsFragment();
        Bundle args = new Bundle();
       args.putSerializable(ARG_ITEM, item);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
          item = (InventoryItem) getArguments().getSerializable(ARG_ITEM);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        codesList = new ArrayList<>();
        viewModel = new ViewModelProvider(this).get(InventoryViewModel.class);


        getParentFragmentManager().setFragmentResultListener("requestKey", this,
                (requestKey, result) -> {
                    Log.d("onresult","here");
                    //Toast.makeText(getActivity(),"I am here",Toast.LENGTH_LONG).show();
                    codesList =result.getStringArrayList("codes_list");

                    viewModel.addBarcodestoItem(codesList, item.getId());
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v= inflater.inflate(R.layout.fragment_item_details, container, false);
       itemImage = v.findViewById(R.id.item_images);
       itemName = v.findViewById(R.id.item_name);
       availableStock  =v.findViewById(R.id.item_available_stock);
       lowStockAlert = v.findViewById(R.id.item_low_stock_alert);
       purchasePrice = v.findViewById(R.id.item_purchase_price);
       totalCost = v.findViewById(R.id.item_total_cost);
        sellingPrice = v.findViewById(R.id.item_selling_price);
        totalValue = v.findViewById(R.id.item_total_value);
        descHeading = v.findViewById(R.id.item_desc_heading);
        desc = v.findViewById(R.id.item_desc);
        addMoreItemsBtn = v.findViewById(R.id.add_more_items_btn);
        editBtn  = v.findViewById(R.id.edit_btn);

        addUnitBtn = v.findViewById(R.id.add_item);
        removeUnitBtn = v.findViewById(R.id.remove_item);

        editBtn.setOnClickListener(view -> {

        });


       setupView();

        addMoreItemsBtn.setOnClickListener(view -> {

                getParentFragmentManager().beginTransaction().addToBackStack(ITEM_DETAILS_FRAG_TAG).
                        replace(R.id.frame,MoreItemsScannerFragment.newInstance(codesList)).commit();

        });


        addUnitBtn.setOnClickListener(view -> {
            item.setAvailableStock(item.getAvailableStock()+1);

            viewModel.updateIteminInventory(item);
            item.setTotalCost((item.getAvailableStock() )*item.getPurchasePrice());
            item.setTotalValue((item.getAvailableStock() )*item.getSellingPrice());
            viewModel.updateIteminInventory(item);
            availableStock.setText(String.valueOf(item.getAvailableStock()));
        });

        removeUnitBtn.setOnClickListener(view -> {
            if(item.getAvailableStock()>0) {
                item.setAvailableStock(item.getAvailableStock() - 1);

                viewModel.updateIteminInventory(item);
                item.setTotalCost((item.getAvailableStock() )*item.getPurchasePrice());
                item.setTotalValue((item.getAvailableStock() )*item.getSellingPrice());
                viewModel.updateIteminInventory(item);
                availableStock.setText(String.valueOf(item.getAvailableStock()));
            }
        });






       return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupView();
    }

    private void setupView() {
        if(item.getIsBarcode()==false){
            addMoreItemsBtn.setVisibility(View.GONE);
            addUnitBtn.setVisibility(View.VISIBLE);
            removeUnitBtn.setVisibility(View.VISIBLE);


        }
        if(item.getImage()!=null){
            Glide.with(this).load(item.getImage()).into(itemImage);
        }
        itemName.setText(item.getName());
        availableStock.setText(String.valueOf(item.getAvailableStock())+" "+item.getUnit());
        lowStockAlert.setText(String.valueOf(item.getLowStockLevel())+" "+item.getUnit());
        purchasePrice.setText("₹ "+String.valueOf(item.getPurchasePrice()));
        totalCost.setText("₹ "+String.valueOf(item.getTotalCost()));
        sellingPrice.setText("₹ "+String.valueOf(item.getSellingPrice()));
        totalValue.setText("₹ "+String.valueOf(item.getTotalValue()));

        if(item.getDesc()!=null&&!item.getDesc().isEmpty()){
            descHeading.setVisibility(View.VISIBLE);
            desc.setVisibility(View.VISIBLE);
            desc.setText(item.getDesc());
        }
    }
}