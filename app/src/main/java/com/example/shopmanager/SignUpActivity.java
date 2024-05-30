package com.example.shopmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class SignUpActivity extends AppCompatActivity {

    TextInputEditText storeName, storeContact, storeEmail,storeAddr,storeUPIId,storePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        storeName = findViewById(R.id.store_name_edit_text);
        storeContact = findViewById(R.id.contact_num_edit_text);
        storeEmail = findViewById(R.id.email_edit_text);
        storeAddr = findViewById(R.id.address_edit_text);
        storeUPIId = findViewById(R.id.upi_id_edit_text);
        storePassword = findViewById(R.id.password_edit_text);
        MaterialButton signUpBtn = findViewById(R.id.signUp_btn);
        TextView logIn = findViewById(R.id.logIn_tv);



        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        if(sharedPreferences.getBoolean("isLoggedIn",false)==true){
            Log.d("isLoggedIn","true");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        logIn.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });


        SharedPreferences.Editor editor = sharedPreferences.edit();

        signUpBtn.setOnClickListener(view -> {
            saveData(editor);
        });



    }

    private void saveData(SharedPreferences.Editor editor) {
        if(storeName.getText().toString().isEmpty()||storeEmail.getText().toString().isEmpty() ||storeAddr.getText().toString().isEmpty()
        ||storeContact.getText().toString().isEmpty()|| storeUPIId.getText().toString().isEmpty()||storePassword.getText().toString().isEmpty()){
            Toast.makeText(this, "Fill all fields",Toast.LENGTH_LONG).show();
        }
        else{
            editor.putString("store_name", storeName.getText().toString());
            editor.putString("store_email", storeEmail.getText().toString());
            editor.putString("store_contact", storeContact.getText().toString());
            editor.putString("store_addr", storeAddr.getText().toString());
            editor.putString("store_upi_id", storeUPIId.getText().toString());
            editor.putString("store_password", storePassword.getText().toString());



            editor.apply();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }
}