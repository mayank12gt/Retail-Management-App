package com.example.shopmanager.upialertsfragments;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.example.shopmanager.models.Payment;
import com.example.shopmanager.repo.Repo;
import com.example.shopmanager.viewmodels.InventoryViewModel;

import java.util.Locale;

public class SmsService extends Service  implements TextToSpeech.OnInitListener {
    TextToSpeech textToSpeech;
    String msg;
    String message;
    String sender;
    String amount;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("service","started");

        amount = intent.getStringExtra("amount");
       sender = intent.getStringExtra("sender");
       msg = intent.getStringExtra("message");

      message = "Received Rupees "+amount+" in bank account";
      Log.d("message2",message);

      Payment payment = new Payment(Double.parseDouble(amount),System.currentTimeMillis());

      Repo repo = new Repo(getApplicationContext());
      repo.addPayment(payment);



        textToSpeech = new TextToSpeech(this, this);


        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onInit(int i) {

        if (TextToSpeech.SUCCESS==0) {
            int result = textToSpeech.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {

            } else {

                speak();
            }
        } else {
           Log.d("error","err");
        }

    }

    private void speak() {

        textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);




    }
}
