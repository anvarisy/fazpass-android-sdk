package com.fazpass.otp.model

data class RequestOtpByEmail(
    val gateway_key:String,
    val email: String
)
