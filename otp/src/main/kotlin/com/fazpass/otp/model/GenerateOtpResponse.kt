package com.fazpass.otp.model


/**
 * Created by Anvarisy on 5/12/2022.
 * fazpass
 * anvarisy@fazpass.com
 */
data class GenerateOtpResponse(
    val status:Boolean,
    val message:String,
    val data: GenerateOtpData?
)
data class GenerateOtpData(
    val id: String?,
    val otp: String?,
    val otp_length: String?,
    val channel: String?,
    val provider: String?,
    val purpose: String?
)
