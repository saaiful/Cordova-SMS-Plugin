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
                    int slot = -1;
                    slot = bundle.get("slot");
    
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
