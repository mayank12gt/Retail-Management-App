package com.example.shopmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.shopmanager.customersfragments.CustomersFragment;
import com.example.shopmanager.dashboardfragments.DashboardFragment;
import com.example.shopmanager.inventoryfragments.InventoryFragment;
import com.example.shopmanager.invoicefragments.InvoicesFragment;
import com.example.shopmanager.upialertsfragments.UpiAlertsFragment;
import com.example.shopmanager.webfragment.AlertsFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_nav_bar);
        toolbar  = findViewById(R.id.topAppBar);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               if(item.getItemId()==R.id.dashboard){
                   getSupportFragmentManager().beginTransaction().replace(R.id.frame, DashboardFragment.newInstance()).commit();
               } else if (item.getItemId()==R.id.invoices) {
                   getSupportFragmentManager().beginTransaction().replace(R.id.frame, InvoicesFragment.newInstance()).commit();
               } else if (item.getItemId()==R.id.inventory) {
                   getSupportFragmentManager().beginTransaction().replace(R.id.frame, InventoryFragment.newInstance()).commit();
               }else if(item.getItemId()==R.id.customers){
                   getSupportFragmentManager().beginTransaction().replace(R.id.frame, CustomersFragment.newInstance()).commit();
               } else if(item.getItemId()==R.id.Profile){
                   getSupportFragmentManager().beginTransaction().replace(R.id.frame, ProfileFragment.newInstance()).commit();
               }

                return  true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.inventory);

        toolbar.setTitle(getSharedPreferences("MyPreferences", Context.MODE_PRIVATE).getString("store_name","Demo Store"));
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
               if(item.getItemId()==R.id.alerts){
                   getSupportFragmentManager().beginTransaction().replace(R.id.frame, AlertsFragment.newInstance()).commit();
               } else if (item.getItemId()==R.id.web) {
                   
               }
               else if (item.getItemId()==R.id.UPIalerts) {
                   getSupportFragmentManager().beginTransaction().replace(R.id.frame, UpiAlertsFragment.newInstance()).commit();
               }

                return true;
            }
        });

    }
}