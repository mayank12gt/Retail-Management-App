package com.example.shopmanager.upialertsfragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsReceiver extends BroadcastReceiver {
    TextToSpeech textToSpeech;
    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the SMS message from the intent
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus != null) {
                for (Object pdu : pdus) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                    String sender = smsMessage.getOriginatingAddress();

                    String message = smsMessage.getMessageBody();

                    Intent serviceIntent = new Intent(context, SmsService.class);
                    serviceIntent.putExtra("sender", sender);

                    serviceIntent.putExtra("message", message);

                    boolean isValidTransaction = message.contains("credited") || message.contains("received") || message.contains("debited") || message.contains("withdrawn");
                    if(sender.equalsIgnoreCase("HDFC BANK")){

                    }
                    Log.d("message",message);

                    if (isValidTransaction) {
                        Log.d("valid","true");
                       
                            
                            try {
                                Pattern regEx = Pattern.compile("(?i)Rs\\.\\s?([0-9]+(\\.\\d{1,2})?)");

                              
                                Matcher matcher = regEx.matcher(message);

                                if (matcher.find()) {
                                    
                                    String amount = matcher.group(1);
                                    System.out.println("Matched amount: " + amount);
                                    serviceIntent.putExtra("amount", amount);
                                } else {
                                    System.out.println("No match found.");
                                }
                            }
                            catch (Exception e){
                                Log.d("err " , e.getLocalizedMessage());
                            }

                        } else {
                            Log.d("Matched amount: " , "no match");
                        }

                    Pattern VPARegex = Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b");

                    // Create a Matcher object
                    Matcher matcher = VPARegex.matcher(message);
                    if (matcher.find()) {

                        String amount = matcher.group(1);
                        System.out.println("Matched VPA: " + amount);
                        serviceIntent.putExtra("amount", amount);
                    } else {
                        System.out.println("No match found.");
                    }

                        context.startService(serviceIntent);
                    }








                }
            }
        }
    }



