package com.example.shopmanager.inventoryfragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.shopmanager.R;
import com.example.shopmanager.models.InventoryItem;
import com.example.shopmanager.models.ItemBarcode;
import com.example.shopmanager.viewmodels.InventoryViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class AddItemtoInventorybyQRFragment extends Fragment {

    private static final String ARG_ITEM = "item";


    RoundedImageView itemImage;
    TextInputEditText nameEditText;
    TextInputEditText descEditText;
    TextInputEditText sellingPriceEditText;
    TextInputEditText purchasePriceEditText;
    TextInputEditText availableStockEditText;
    TextInputEditText lowStockAlertLevelEditText;
    TextInputEditText skuEditText;
    AutoCompleteTextView categorySpinner;
    AutoCompleteTextView unitSpinner;
    MaterialButton addItemButton;
    FloatingActionButton addImagesButton;
    MaterialButton quickAddButton;

    InventoryViewModel viewModel;


    InventoryItem scanned_item;

    public AddItemtoInventorybyQRFragment() {
        // Required empty public constructor
    }


    public static AddItemtoInventorybyQRFragment newInstance(InventoryItem item) {
        AddItemtoInventorybyQRFragment fragment = new AddItemtoInventorybyQRFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ITEM,item);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            scanned_item = (InventoryItem) getArguments().getSerializable(ARG_ITEM);

        }

        viewModel = new ViewModelProvider(this).get(InventoryViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View v= inflater.inflate(R.layout.fragment_add_itemto_inventoryby_q_r, container, false);

        itemImage = v.findViewById(R.id.item_images);
        nameEditText = v.findViewById(R.id.add_name_edit_text);
        availableStockEditText = v.findViewById(R.id.add_available_stock_edit_text);
        purchasePriceEditText = v.findViewById(R.id.add_purchase_price_edit_text);
        sellingPriceEditText = v.findViewById(R.id.add_selling_price_edit_text);
        //skuEditText = v.findViewById(R.id.add_sku_edit_text);
        lowStockAlertLevelEditText = v.findViewById(R.id.add_low_stock_level_edit_text);
        descEditText = v.findViewById(R.id.add_desc_edit_text);
        categorySpinner = v.findViewById(R.id.category_spinner);
        unitSpinner = v.findViewById(R.id.unit_spinner);

        addItemButton = v.findViewById(R.id.add_item_btn);

        addImagesButton = v.findViewById(R.id.add_images_btn);

        unitSpinner.setText("items");
        Log.d("iteminfo",scanned_item.getName());
        Log.d("iteminfo", String.valueOf(scanned_item.getPurchasePrice()));
        Log.d("iteminfo", String.valueOf(scanned_item.getSellingPrice()));
        Log.d("iteminfo", String.valueOf(scanned_item.getDesc()));
        Log.d("iteminfo", String.valueOf(scanned_item.getSKU()));



        nameEditText.setText(scanned_item.getName());
        purchasePriceEditText.setText(String.valueOf(scanned_item.getPurchasePrice()));
       sellingPriceEditText.setText(String.valueOf(scanned_item.getSellingPrice()));
       categorySpinner.setText(scanned_item.getCategory());
       availableStockEditText.setText(String.valueOf(1));

        descEditText.setText(scanned_item.getDesc());




        addItemButton.setOnClickListener(view -> {
            Log.d("add","clicked");
            InventoryItem item = createItem();
            viewModel.addItemtoInventory(item);
            getParentFragmentManager().popBackStack();
        });


    return v;

    }


    private InventoryItem createItem() {
        InventoryItem item = new InventoryItem();
        if(nameEditText.getText().toString().trim().isEmpty()){
            Toast.makeText(getActivity(),"Item name cannot be empty",Toast.LENGTH_LONG);
        }
        else if(purchasePriceEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(getActivity(),"Purchase Price cannot be empty",Toast.LENGTH_LONG);
        }
        else if(sellingPriceEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(getActivity(),"Purchase Price cannot be empty",Toast.LENGTH_LONG);
        }
        else {
            item.setName(nameEditText.getText().toString());
            item.setAvailableStock(Integer.parseInt(availableStockEditText.getText().toString()));
            item.setIsBarcode(false);

            item.setPurchasePrice(Float.parseFloat(purchasePriceEditText.getText().toString()));
            item.setSellingPrice(Float.parseFloat(sellingPriceEditText.getText().toString()));
            item.setSKU(scanned_item.getSKU());
            item.setLowStockLevel(lowStockAlertLevelEditText.getText().toString().isEmpty()?0:Integer.parseInt(lowStockAlertLevelEditText.getText().toString()));
            item.setDesc(descEditText.getText().toString());
            item.setCategory(categorySpinner.getText().toString());
            item.setAvailableStock(Integer.parseInt(availableStockEditText.getText().toString()));
            item.setUnit("items");
            item.setTotalCost(Integer.parseInt(availableStockEditText.getText().toString())
                    * Float.parseFloat(purchasePriceEditText.getText().toString()));
            item.setTotalValue(Integer.parseInt(availableStockEditText.getText().toString())
                    * Float.parseFloat(sellingPriceEditText.getText().toString()));
//            if(imageURI!=null){
//                item.setImage(imageURI);
//            }
//            else{
//                item.setImage(null);
//            }

        }
        return item;
    }
}