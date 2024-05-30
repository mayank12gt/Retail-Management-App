package com.example.shopmanager.inventoryfragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shopmanager.R;
import com.example.shopmanager.models.CategoryItem;
import com.example.shopmanager.models.InventoryItem;
import com.example.shopmanager.models.ItemBarcode;
import com.example.shopmanager.viewmodels.InventoryViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;


public class AddItemManuallyFragment extends Fragment {

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




    CategoryItem selectedCategory;
    String imageURI;

    public AddItemManuallyFragment() {
        // Required empty public constructor
    }


    public static AddItemManuallyFragment newInstance() {
        AddItemManuallyFragment fragment = new AddItemManuallyFragment();
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

        }

        viewModel = new ViewModelProvider(this).get(InventoryViewModel.class);

        getParentFragmentManager().setFragmentResultListener("imagerequestkey", this,
                (requestKey, result) -> {
                    Log.d("onresult","here");
                    //Toast.makeText(getActivity(),"I am here",Toast.LENGTH_LONG).show();
                    Log.d("imageuri",result.getString("ImageURI"));
                    imageURI = result.getString("ImageURI");
                    setItemImage();

                });
    }
    private void loadImage() {
        if(imageURI!=null) {
            Glide.with(getActivity())
                    .load(imageURI)
                    .into(itemImage);
        }
    }
    private void setItemImage() {
        loadImage();
//        Dexter.withContext(getActivity())
//                .withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
//                .withListener(new PermissionListener() {
//                    @Override
//                    public void onPermissionGranted(PermissionGrantedResponse response) {
//                        loadImage();
//                    }
//
//                    @Override
//                    public void onPermissionDenied(PermissionDeniedResponse response) {
//
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
//
//                    }
//                })
//                .check();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View v= inflater.inflate(R.layout.fragment_add_item_manually, container, false);

        itemImage = v.findViewById(R.id.item_images);
        nameEditText = v.findViewById(R.id.add_name_edit_text);
        availableStockEditText = v.findViewById(R.id.add_available_stock_edit_text);
        purchasePriceEditText = v.findViewById(R.id.add_purchase_price_edit_text);
        sellingPriceEditText = v.findViewById(R.id.add_selling_price_edit_text);
        skuEditText = v.findViewById(R.id.add_sku_edit_text);
        lowStockAlertLevelEditText = v.findViewById(R.id.add_low_stock_level_edit_text);
        descEditText = v.findViewById(R.id.add_desc_edit_text);
        categorySpinner = v.findViewById(R.id.category_spinner);
        unitSpinner = v.findViewById(R.id.unit_spinner);

        addItemButton = v.findViewById(R.id.add_item_btn);

        addImagesButton = v.findViewById(R.id.add_images_btn);

        unitSpinner.setText("items");


        List<String> categoryItems = new ArrayList<>();

//        categoryItems.add(new CategoryItem("All",false));
//        categoryItems.add(new CategoryItem("Beverage",false));
//        categoryItems.add(new CategoryItem("Snacks",false));
//        categoryItems.add(new CategoryItem("Add Category",true));

        categoryItems.add("All");
        categoryItems.add("Beverage");
        categoryItems.add("Grocery");
        categoryItems.add("Add");



        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item,categoryItems);

        categorySpinner.setAdapter(adapter);


        categorySpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//

            }
        });




        //skuEditText.setText(String.valueOf(codesList.size()));
        //skuEditText.setInputType(InputType.TYPE_NULL);

        //skuEditText.setEnabled(false);






        addImagesButton.setOnClickListener(view -> {
            getParentFragmentManager().beginTransaction().addToBackStack("add_item_frag_tag").replace(R.id.frame,ImageCaptureFragment.newInstance()).commit();


        });

        addItemButton.setOnClickListener(view -> {
            InventoryItem item =createItem();
            viewModel.addItemtoInventory(item);

            getParentFragmentManager().popBackStack("inventory_frag_tag", FragmentManager.POP_BACK_STACK_INCLUSIVE);
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
            item.setSKU(skuEditText.getText().toString());
            item.setLowStockLevel(lowStockAlertLevelEditText.getText().toString().isEmpty()?0:Integer.parseInt(lowStockAlertLevelEditText.getText().toString()));
            item.setDesc(descEditText.getText().toString());
            item.setCategory(categorySpinner.getText().toString());
            item.setUnit("items");
            item.setTotalCost(Integer.parseInt(availableStockEditText.getText().toString())
                    * Float.parseFloat(purchasePriceEditText.getText().toString()));
            item.setTotalValue(Integer.parseInt(availableStockEditText.getText().toString())
                    * Float.parseFloat(sellingPriceEditText.getText().toString()));
            if(imageURI!=null){
                item.setImage(imageURI);
            }
            else{
                item.setImage(null);
            }

        }
        return item;
    }
}