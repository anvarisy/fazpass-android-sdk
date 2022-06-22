package com.fazpass.trusted_device

internal class ConsTrustedDevice {
    companion object{
        var BASE_URL: String = "${TrustedDevice.baseUrl}/v1/otp/"
        const val KEY_STORE_ALIAS = "FAZPASS_TRUSTED_DEVICE_SYSTEM"
        const val SIGNATURE_ALGO = "SHA256withECDSA"
        const val SIGNATURE_ALGO_RSA = "SHA256withRSA"
    }
}