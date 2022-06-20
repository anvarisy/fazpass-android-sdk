package com.fazpass.otp.model

internal data class RequestOtpByPhone(
    val gateway_key:String,
    val phone: String
)


