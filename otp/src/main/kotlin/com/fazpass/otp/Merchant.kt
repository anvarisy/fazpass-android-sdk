package com.fazpass.otp

import android.util.Log
import com.fazpass.otp.model.GenerateOtpRequest
import com.fazpass.otp.usecase.MerchantUsecase
import com.fazpass.otp.utils.Listener

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.RequestBody
import org.json.JSONObject

/**
 * Created by Anvarisy on 5/11/2022.
 * fazpass
 * anvarisy@fazpass.com
 */
open class Merchant {
    internal lateinit var merchantKey:String
    private lateinit var gatewayKey:String

    fun setGateway(gatewayKey:String){
        this.gatewayKey = gatewayKey
    }

    fun generateOtp(phone:String){
        startGenerate(phone, this.gatewayKey)
    }

    fun generateOtp(phone:String, gateway: String){
       startGenerate(phone, gatewayKey)
    }

    private fun startGenerate(phone: String, gatewayKey: String){
        val fazpass by lazy { MerchantUsecase.start() }
        fazpass.generateOtp("Bearer $merchantKey",GenerateOtpRequest( gatewayKey, phone)).
        subscribeOn(Schedulers.io()).
        observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    Log.i("RESULT", result.toString())
                },
                { error ->
                    error.message?.let { Log.e("ERRORS", it) }
                }
            )
    }


}