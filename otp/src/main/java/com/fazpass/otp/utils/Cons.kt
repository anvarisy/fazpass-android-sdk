package com.fazpass.otp.utils

import com.fazpass.otp.Merchant

internal class Cons{
    companion object{
        var BASE_URL: String = "${Merchant.baseUrl}/v1/otp/"
    }
}

