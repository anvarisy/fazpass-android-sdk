package com.fazpass.otp

import com.fazpass.otp.model.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

open class Otp : OtpInterface {

    companion object {
        internal var merchantKey: String = ""
        internal var gatewayKey: String = ""
        internal var baseUrl: String = ""
        internal var senderId: String = ""
        internal var response: Response = Response(false, "", "", "", "", null)
    }

    fun setGateway(gateway: String) {
        gatewayKey = gateway
    }

    override fun generateOtp(target: String, onComplete: OnCompleteOtp<Response>) {
        val fazpass by lazy { UseCaseOtp.start() }
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()) {
            fazpass.generateOtpByEmail(
                "Bearer $merchantKey",
                RequestOtpByEmail(gatewayKey, target)
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(onComplete::onSuccess, onComplete::onFailure)
        } else {
            fazpass.generateOtpByPhone("Bearer $merchantKey", RequestOtpByPhone(gatewayKey, target))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(onComplete::onSuccess, onComplete::onFailure)
        }
    }

    override fun verifyOtp(otpId: String, otp: String, onComplete: OnCompleteOtp<Boolean>) {
        val fazpass by lazy { UseCaseOtp.start() }
        fazpass.verifyOtp("Bearer $merchantKey", VerifyOtpRequest(otpId, otp))
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(onComplete::onSuccess, onComplete::onFailure)
    }

    override fun sendOtp(target: String, otp: String, onComplete: OnCompleteOtp<Response>) {
        val fazpass by lazy { UseCaseOtp.start() }
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()) {
            fazpass.sendOtpByEmail(
                "Bearer $merchantKey",
                SendOtpRequestByEmail(gatewayKey, target, otp)
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(onComplete::onSuccess, onComplete::onFailure)
        } else {
            fazpass.sendOtpByPhone(
                "Bearer $merchantKey",
                SendOtpRequestByPhone(gatewayKey, target, otp)
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(onComplete::onSuccess, onComplete::onFailure)
        }
    }

    override fun requestOtp(target: String, onComplete: OnCompleteOtp<Response>) {
        val fazpass by lazy { UseCaseOtp.start() }
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()) {
            fazpass.requestOtpByEmail(
                "Bearer $merchantKey",
                RequestOtpByEmail(gatewayKey, target)
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(onComplete::onSuccess, onComplete::onFailure)
        } else {
            fazpass.requestOtpByPhone("Bearer $merchantKey", RequestOtpByPhone(gatewayKey, target))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(onComplete::onSuccess, onComplete::onFailure)
        }
    }
}