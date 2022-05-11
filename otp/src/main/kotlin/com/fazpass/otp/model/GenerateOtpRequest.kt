package com.fazpass.otp.model


/**
 * Created by Anvarisy on 5/11/2022.
 * fazpass
 * anvarisy@fazpass.com
 */
data class GenerateOtpRequest(
    val gateway_key:String,
    val phone: String
)
