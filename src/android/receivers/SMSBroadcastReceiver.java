package com.ramkumar.cordovaplugins.sms.receivers;

import org.apache.cordova.PluginResult;

import com.ramkumar.cordovaplugins.sms.CordovaSMS;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import org.json.JSONObject;

public class SMSBroadcastReceiver extends BroadcastReceiver {
    public SMSBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for(Object currentObj : pdusObj) {
                    
                    try {
                        Bundle bundle = intent.getExtras();
                        int slot = -1;
                        if (bundle != null) {
                            Set<String> keySet = bundle.keySet();
                            for(String key:keySet){
                              switch (key){
                                case "slot":slot = bundle.getInt("slot", -1);
                                break;
                                case "simId":slot = bundle.getInt("simId", -1);
                                break;
                                case "simSlot":slot = bundle.getInt("simSlot", -1);
                                break;
                                case "slot_id":slot = bundle.getInt("slot_id", -1);
                                break;
                                case "simnum":slot = bundle.getInt("simnum", -1);
                                break;
                                case "slotId":slot = bundle.getInt("slotId", -1);
                                break;
                                case "slotIdx":slot = bundle.getInt("slotIdx", -1);
                                break;
                                default:
                                  if(key.toLowerCase().contains("slot")|key.toLowerCase().contains("sim")){
                                   String value = bundle.getString(key, "-1");
                                   if(value.equals("0")|value.equals("1")|value.equals("2")){
                                     slot = bundle.getInt(key, -1);
                                   }
                                }
                              }
                            }
                            Log.d("slot", "slot=>"+slot);
                        }
                    } catch (Exception e){
                        Log.d(TAG, "Exception=>"+e);
                    }
    
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) currentObj);
                    JSONObject sms = new JSONObject();
                    sms.put("message",currentMessage.getDisplayMessageBody());
                    sms.put("sender",currentMessage.getDisplayOriginatingAddress());
                    /*sms.put("sim",currentMessage.getIndexOnIcc());*/
                    sms.put("sim",slot); 
                    sms.put("service_center",currentMessage.getServiceCenterAddress());
                    
                    CordovaSMS.sendSMSPayload( sms.toString() );
                }
            }
        } catch (Exception e) {
            Log.e("SMS", "Exception: " + e);
            e.printStackTrace();
        }
        
        
    }
}
