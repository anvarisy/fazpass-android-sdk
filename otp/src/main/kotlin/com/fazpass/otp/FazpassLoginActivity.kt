package com.fazpass.otp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.fazpass.otp.model.GenerateOtpResponse


/**
 * Created by Anvarisy on 5/12/2022.
 * fazpass
 * anvarisy@fazpass.com
 */
internal class FazpassLoginActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.fazpass_login)
        val bundle: Bundle? = intent.extras
        val tvPhone: TextView = findViewById(R.id.tvValidationMobile)
        bundle?.apply {
            //Serializable Data
            val it = getSerializable("it") as GenerateOtpResponse?
            if (it != null) {
                var phone:String = it.phone.toString()
                tvPhone.text =phone.dropLast(4)+"xxxx"

            }
        }
    }
}