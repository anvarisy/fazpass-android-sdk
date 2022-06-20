package com.fazpass.otp.model

internal data class SendOtpRequestByEmail(
    val gateway_key:String,
    val email: String,
    val otp:String,
)