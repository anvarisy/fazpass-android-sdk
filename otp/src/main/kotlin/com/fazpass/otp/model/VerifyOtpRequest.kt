package com.fazpass.otp.model


/**
 * Created by Anvarisy on 5/12/2022.
 * fazpass
 * anvarisy@fazpass.com
 */
data class VerifyOtpRequest(
    val otp_id:String,
    val otp: String
)
