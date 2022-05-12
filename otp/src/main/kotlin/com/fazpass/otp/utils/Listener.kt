package com.fazpass.otp.utils

import com.fazpass.otp.model.GenerateOtpResponse


/**
 * Created by Anvarisy on 5/11/2022.
 * fazpass
 * anvarisy@fazpass.com
 */
interface Listener {
    fun onSuccess():GenerateOtpResponse
    fun onError():Error
}