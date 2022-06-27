package com.fazpass.otp

interface ServiceListenerOtp {
    fun onSmsReceived(otp: String?)
    fun onCallReceived(otp: String?)
}
