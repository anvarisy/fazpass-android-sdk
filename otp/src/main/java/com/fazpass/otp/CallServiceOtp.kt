package com.fazpass.otp

import android.net.Uri
import android.os.Build
import android.telecom.Call
import android.telecom.CallScreeningService
import android.util.Log
import androidx.annotation.RequiresApi
import org.greenrobot.eventbus.EventBus

@RequiresApi(Build.VERSION_CODES.N)
class CallServiceOtp: CallScreeningService(){
    private var callback: ServiceListenerOtp? = null

    fun setOnOtpReceived(callback: ServiceListenerOtp?) {
        this.callback = callback
    }

    override fun onScreenCall(callDetails: Call.Details) {
        val phoneNumber = getPhoneNumber(callDetails)
        Log.d("INCOMING NUMBER", phoneNumber)
        val phoneLength = phoneNumber.length
        val otp = phoneNumber.substring(phoneLength- (Otp.response.data?.otp_length?.toInt() ?: 6))
        callback?.onCallReceived(otp)
        Log.d("INCOMING OTP", otp)
        EventBus.getDefault().post(otp)
    }

    private fun getPhoneNumber(callDetails: Call.Details): String {
        return callDetails.handle.toString().removeTelPrefix().parseCountryCode()
    }

    private fun String.removeTelPrefix() = this.replace(":tel", "")

    private fun String.parseCountryCode(): String = Uri.decode(this)
}