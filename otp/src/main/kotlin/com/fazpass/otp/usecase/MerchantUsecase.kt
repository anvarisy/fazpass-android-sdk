package com.fazpass.otp.usecase


import com.fazpass.otp.model.GenerateOtpRequest
import com.fazpass.otp.model.GenerateOtpResponse
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


/**
 * Created by Anvarisy on 5/10/2022.
 * fazpass
 * anvarisy@fazpass.com
 */
interface MerchantUsecase {
    @POST("generate") fun generateOtp(@Header("Authorization") token:String, @Body requestBody: GenerateOtpRequest): Observable<GenerateOtpResponse>

    companion object{
        fun start():MerchantUsecase{
            val clientBuilder = OkHttpClient.Builder()
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY;
            clientBuilder.addInterceptor(loggingInterceptor);
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://34.101.82.250:3002/v1/otp/")
                .client(clientBuilder.build())
                .build()

            return retrofit.create(MerchantUsecase::class.java)
        }
    }
}