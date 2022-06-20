package com.fazpass.otp.model

internal data class VerifyOtpRequest(
    val otp_id:String,
    val otp: String
)
