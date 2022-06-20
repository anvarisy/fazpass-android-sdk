package com.fazpass.otp.model

internal data class SendOtpRequestByPhone(
    val gateway_key:String,
    val phone: String,
    val otp:String,
)
