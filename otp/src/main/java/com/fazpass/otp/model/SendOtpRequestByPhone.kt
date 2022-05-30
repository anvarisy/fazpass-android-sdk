package com.fazpass.otp.model

data class SendOtpRequestByPhone(
    val gateway_key:String,
    val phone: String,
    val otp:String,
)
