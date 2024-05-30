package com.example.shopmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText storeEmail, storepassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        storeEmail = findViewById(R.id.email_edit_text);
        storepassword = findViewById(R.id.password_edit_text);
        MaterialButton logInBtn = findViewById(R.id.logIn_btn);
        TextView signUp = findViewById(R.id.signUp_tv);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        logInBtn.setOnClickListener(view -> {
            check(sharedPreferences);
        });
        signUp.setOnClickListener(view -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
            finish();
        });
    }
    void  check(SharedPreferences preferences){
        if(storeEmail.getText().toString().isEmpty()||storepassword.getText().toString().isEmpty()){
            Toast.makeText(this, "Fill all fields",Toast.LENGTH_LONG).show();
        }
        else{
            String email = preferences.getString("store_email","");
            String password = preferences.getString("store_password","");
            if(email.equals(storeEmail.getText().toString()) && password.equals(storepassword.getText().toString())){
                SharedPreferences.Editor editor = preferences.edit();

                editor.putBoolean("isLoggedIn",true);
                editor.apply();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            else{

                Snackbar.make(this.getCurrentFocus(),"Email or Password is incorrect. Please try again",Snackbar.LENGTH_LONG).show();
            }
        }
    }
}