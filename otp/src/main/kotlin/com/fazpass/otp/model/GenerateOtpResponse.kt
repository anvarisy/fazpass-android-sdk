package com.fazpass.otp.model

/**
 * Created by Anvarisy on 5/12/2022.
 * fazpass
 * anvarisy@fazpass.com
 */

data class GenerateOtpResponse(
    var status:Boolean,
    var message:String,
    var error:String?,
    var phone:String?,
    var data: GenerateOtpData?
):java.io.Serializable
data class GenerateOtpData(
    var id: String?,
    var otp: String?,
    var prefix: String?,
    var otp_length: String?,
    var channel: String?,
    var provider: String?,
    var purpose: String?
)
