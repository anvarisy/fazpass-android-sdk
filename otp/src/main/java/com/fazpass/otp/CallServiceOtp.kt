package com.fazpass.otp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log
import org.greenrobot.eventbus.EventBus


class CallServiceOtp: BroadcastReceiver() {
    override fun onReceive(arg0: Context?, arg1: Intent?) {
        if (arg1 != null) {
            if (arg1.action.equals("android.intent.action.PHONE_STATE")) {

                val state: String? = arg1.getStringExtra(TelephonyManager.EXTRA_STATE)
                if (state == TelephonyManager.EXTRA_STATE_RINGING) {
                    val phoneNumber: String? =
                        arg1.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
                   if(phoneNumber!= null){
                       val phoneLength = phoneNumber.length
                       val otp = phoneNumber.substring(phoneLength- (Otp.response.data?.otp_length?.toInt() ?: 6))
                       Log.d("INCOMING OTP", otp)
                       EventBus.getDefault().post(otp)
                   }
                }
            }
        }
    }
}