package com.fazpass.otp

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.fazpass.otp.model.GenerateOtpResponse


/**
 * Created by Anvarisy on 5/11/2022.
 * fazpass
 * anvarisy@fazpass.com
 */
class Fazpass {
    companion object{
        fun initialize(key:String):Merchant{
            val m = Merchant()
            m.merchantKey = key
            return m
        }

        fun LoginPage(context:Context, it:GenerateOtpResponse){
            val intent = Intent(context, FazpassLoginActivity::class.java).apply {
                putExtra("it",it)
            }

            context.startActivity(intent)
        }
    }

}