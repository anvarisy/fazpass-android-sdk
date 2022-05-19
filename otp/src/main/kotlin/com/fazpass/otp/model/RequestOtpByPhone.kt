package com.fazpass.otp.model

data class RequestOtpByPhone(
    val gateway_key:String,
    val phone: String
)


