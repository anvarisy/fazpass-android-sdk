package com.fazpass.otp

import android.util.Log
import com.fazpass.otp.model.GenerateOtpRequest
import com.fazpass.otp.model.GenerateOtpRequestByEmail
import com.fazpass.otp.model.GenerateOtpResponse
import com.fazpass.otp.model.VerifyOtpRequest
import com.fazpass.otp.usecase.MerchantUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Anvarisy on 5/11/2022.
 * fazpass
 * anvarisy@fazpass.com
 */
open class Merchant {
    internal lateinit var merchantKey:String
    private lateinit var gatewayKey:String

    companion object{
        internal  lateinit var merchantKey: String
        internal lateinit var gatewayKey: String
    }
    fun setGateway(gatewayKey:String){
        this.gatewayKey = gatewayKey
        Merchant.gatewayKey = gatewayKey
    }

/*    fun generateOtp(phone: String, gatewayKey:String,onComplete:(GenerateOtpResponse)->Unit){
        startGenerate(phone, gatewayKey,onComplete)
    }*/

    fun generateOtp(phone:String, onComplete:(GenerateOtpResponse)->Unit){
       startGenerate(phone, Merchant.gatewayKey,onComplete)
    }

    private fun startGenerate(phone: String, gatewayKey: String, onComplete:(GenerateOtpResponse)->Unit){
        val fazpass by lazy { MerchantUseCase.start() }
        var response = GenerateOtpResponse(false,"",null, phone,null)
        if(android.util.Patterns.EMAIL_ADDRESS.matcher(phone).matches()){
            fazpass.generateOtpByEmail("Bearer ${Merchant.merchantKey}", GenerateOtpRequestByEmail( gatewayKey, phone)).
            subscribeOn(Schedulers.io()).
            observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        response = result
                        response.phone = phone
                        onComplete(response)
                    },
                    { error ->
                        response.error = error.message
                        onComplete(response)
                    }
                )
        }else{
            fazpass.generateOtp("Bearer ${Merchant.merchantKey}",GenerateOtpRequest( gatewayKey, phone)).
            subscribeOn(Schedulers.io()).
            observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        response = result
                        response.phone = phone
                        onComplete(response)
                    },
                    { error ->
                        response.error = error.message
                        onComplete(response)
                    }
                )
        }


    }

    fun verifyOtp(otpId:String, otp:String, onComplete: (Boolean) -> Unit){
        val fazpass by lazy { MerchantUseCase.start() }
        fazpass.verifyOtp("Bearer ${Merchant.merchantKey}",VerifyOtpRequest( otpId, otp)).
        subscribeOn(Schedulers.io()).
        observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    onComplete(true)
                },
                {
                    onComplete(false)
                }
            )
    }
}