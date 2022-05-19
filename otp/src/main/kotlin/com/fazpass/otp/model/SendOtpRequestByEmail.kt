package com.fazpass.otp.model

data class SendOtpRequestByEmail(
    val gateway_key:String,
    val email: String,
    val otp:String,
)