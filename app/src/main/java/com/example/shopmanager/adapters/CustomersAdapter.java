package com.example.shopmanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.RoomDatabase;

import com.example.shopmanager.R;
import com.example.shopmanager.customersfragments.OnCustomerItemClicked;
import com.example.shopmanager.models.Customer;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class CustomersAdapter extends RecyclerView.Adapter<CustomersAdapter.viewholder> {
    List<Customer> customers;
    OnCustomerItemClicked onCustomerItemClicked;

    public CustomersAdapter(List<Customer> customers,OnCustomerItemClicked onCustomerItemClicked) {
        this.customers = customers;
        this.onCustomerItemClicked = onCustomerItemClicked;
    }

    @NonNull
    @Override
    public CustomersAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomersAdapter.viewholder holder, int position) {
Customer customer = customers.get(position);
holder.customerName.setText(customer.getName());
holder.customerContactNum.setText(customer.getContactNum());
holder.customerEmail.setText(customer.getEmail());
holder.itemView.setOnClickListener(view -> {
onCustomerItemClicked.onCustomerItemClicked(customer);
});
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    public  void update(List<Customer> customers){
        this.customers = customers;
        notifyDataSetChanged();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        RoundedImageView customerImage;
        TextView customerName;
        TextView customerContactNum;
        TextView customerEmail;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            customerImage = itemView.findViewById(R.id.customer_Image);
            customerName = itemView.findViewById(R.id.customer_name);
            customerContactNum = itemView.findViewById(R.id.customer_contact_num);
            customerEmail = itemView.findViewById(R.id.customer_email);
        }
    }
}
