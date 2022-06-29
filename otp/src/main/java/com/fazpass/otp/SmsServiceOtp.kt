package com.fazpass.otp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsMessage
import android.util.Log
import org.greenrobot.eventbus.EventBus


class SmsServiceOtp:BroadcastReceiver() {


    override fun onReceive(ctx: Context?, intent: Intent?) {
        val bundle = intent!!.extras
        try{
            val pdusObj = (bundle!!["pdus"] as Array<*>?)?.filterIsInstance<Any>()
            var otp = ""
            for (i in pdusObj!!.indices) {
                val currentMessage = getIncomingMessage(pdusObj[i], bundle)
                val phoneNumber = currentMessage!!.displayOriginatingAddress
                Log.d("SENDER",phoneNumber)
                if (Otp.senderId!=""&&phoneNumber!=Otp.senderId){
                   otp = ""
                }
                var message = currentMessage.displayMessageBody
                val re = Regex("[^A-Za-z0-9]")
                message = re.replace(message, " ")
                val messageArr = message.replace("(?m)^[ \t]*\r?\n".toRegex()," ").split(" ")
                for(item in messageArr){
                   if(!isLetters(item)){
                       otp = item.replace(" ","")
                   }
                }
                Log.d("INCOMING OTP", otp)
                EventBus.getDefault().post(otp)
            }
        }catch (e: Exception){

        }
    }

    private fun getIncomingMessage(aObject: Any, bundle: Bundle): SmsMessage? {
        val format = bundle.getString("format")
        return SmsMessage.createFromPdu(aObject as ByteArray, format)
    }

    private fun isLetters(string: String): Boolean {
        return string.filter { it in 'A'..'Z' || it in 'a'..'z' }.length == string.length
    }
}