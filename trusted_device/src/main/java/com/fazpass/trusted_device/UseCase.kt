package com.fazpass.trusted_device

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

internal class TrustedDeviceUseCase {

    companion object{
        fun start(): TrustedDeviceUseCase {
            val clientBuilder = OkHttpClient.Builder()
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            clientBuilder.addInterceptor(loggingInterceptor)
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Cons.BASE_URL)
                .client(clientBuilder.build())
                .build()

            return retrofit.create(TrustedDeviceUseCase::class.java)
        }
    }
}