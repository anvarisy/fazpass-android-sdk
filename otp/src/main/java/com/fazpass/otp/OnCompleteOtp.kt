package com.fazpass.otp

interface OnCompleteOtp<R> {
    fun onSuccess(result: R)
    fun onFailure(err: Throwable)
}