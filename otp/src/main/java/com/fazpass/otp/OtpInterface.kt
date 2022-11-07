package com.fazpass.otp

import com.fazpass.otp.model.Response

sealed interface OtpInterface {
    fun generateOtp(target: String, onComplete: OnCompleteOtp<Response>)
    fun verifyOtp(otpId: String, otp: String, onComplete: OnCompleteOtp<Boolean>)
    fun sendOtp(target: String, otp: String, onComplete: OnCompleteOtp<Response>)
    fun requestOtp(target: String, onComplete: OnCompleteOtp<Response>)
}