package com.example.shopmanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.shopmanager.R;
import com.example.shopmanager.models.CategoryItem;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<CategoryItem> {

List<CategoryItem> categoryItems;
    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull List<CategoryItem> objects) {
        super(context, resource, objects);
        categoryItems = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_item,parent,false);

        }

        TextView spinnerText = convertView.findViewById(R.id.spinner_item);
        ImageView addBtn = convertView.findViewById(R.id.add_category_btn);

        CategoryItem categoryItem = categoryItems.get(position);

        if(categoryItem!=null){
            spinnerText.setText(categoryItem.getCategory());
            if(categoryItem.getAddItem()==true){
                addBtn.setVisibility(View.VISIBLE);

            }
            else{
                addBtn.setVisibility(View.GONE);
            }
        }





        return convertView;
    }


}
