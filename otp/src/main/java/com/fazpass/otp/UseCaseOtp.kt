package com.fazpass.otp

import com.fazpass.otp.model.*
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

internal interface UseCaseOtp {
    @POST("generate") fun generateOtpByPhone(@Header("Authorization") token:String, @Body requestBody: RequestOtpByPhone): Observable<Response>
    @POST("generate") fun generateOtpByEmail(@Header("Authorization") token:String, @Body requestBody: RequestOtpByEmail): Observable<Response>
    @POST("verify") fun verifyOtp(@Header("Authorization") token:String, @Body requestBody: VerifyOtpRequest): Observable<Boolean>
    @POST("request") fun requestOtpByPhone(@Header("Authorization") token:String, @Body requestBody: RequestOtpByPhone): Observable<Response>
    @POST("request") fun requestOtpByEmail(@Header("Authorization") token:String, @Body requestBody: RequestOtpByEmail):Observable<Response>
    @POST("send") fun sendOtpByPhone(@Header("Authorization") token:String, @Body requestBody: SendOtpRequestByPhone): Observable<Response>
    @POST("send") fun sendOtpByEmail(@Header("Authorization") token:String, @Body requestBody: SendOtpRequestByEmail):Observable<Response>

    companion object{
        fun start(): UseCaseOtp {
            val clientBuilder = OkHttpClient.Builder()
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            clientBuilder.addInterceptor(loggingInterceptor)
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Otp.baseUrl)
                .client(clientBuilder.build())
                .build()

            return retrofit.create(UseCaseOtp::class.java)
        }
    }
}