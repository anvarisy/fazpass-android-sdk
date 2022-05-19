package com.fazpass.otp.model

data class VerifyOtpRequest(
    val otp_id:String,
    val otp: String
)
