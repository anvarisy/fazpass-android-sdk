package com.fazpass.otp.model

data class Response(
    var status:Boolean,
    var message:String,
    var type: String?,
    var error:String?,
    var target:String?,
    var data: Data?
):java.io.Serializable
data class Data(
    var id: String?,
    var otp: String?,
    var prefix: String?,
    var otp_length: String?,
    var channel: String?,
    var provider: String?,
    var purpose: String?
)
