package com.fazpass.otp.model

internal data class RequestOtpByEmail(
    val gateway_key:String,
    val email: String
)
