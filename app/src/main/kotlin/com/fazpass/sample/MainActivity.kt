package com.fazpass.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast

import androidx.appcompat.widget.AppCompatButton
import com.fazpass.otp.Fazpass

private lateinit var myButton: AppCompatButton
    private lateinit var myPhone: EditText
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myButton = findViewById(R.id.btnGenerate)
        myPhone = findViewById(R.id.edtPhone)
        //set listener
        myButton.setOnClickListener {
            //Action perform when the user clicks on the button.
            var phone: String = myPhone.text.toString()
            val m = Fazpass.initialize("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZGVudGlmaWVyIjoxM30.SbTzA7ftEfUtkx0Rdt_eoXrafx1X9kf2SHccS_G5jS8")
            m.setGateway("9bbd5a07-fc1c-402e-8424-86cb970d0bf7")
            m.generateOtp(phone)

        }
    }
}