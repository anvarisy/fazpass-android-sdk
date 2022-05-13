package com.fazpass.sample
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.fazpass.otp.Fazpass



private lateinit var myCountry: EditText
private lateinit var myPhone: EditText
private lateinit var myEmail: EditText
private lateinit var btnSms: AppCompatButton
private lateinit var btnMissCall: AppCompatButton
private lateinit var btnWhatsapp: AppCompatButton
private lateinit var btnLongWhatsapp: AppCompatButton
private lateinit var btnEmail: AppCompatButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myCountry = findViewById(R.id.edtCountryCode)
        myCountry.setText("+62")
        myPhone = findViewById(R.id.edtPhone)
        myPhone.requestFocus()
        myEmail = findViewById(R.id.edtEmail)
        btnSms = findViewById(R.id.btnSms)
        btnSms.setOnClickListener {
            if(myPhone.text.toString() == ""){
                Toast.makeText(this,"Phone should be filled",Toast.LENGTH_LONG).show()
            }else{
                Generate(Gateway.SMS)
            }

        }

        btnMissCall = findViewById(R.id.btnMissCall)
        btnMissCall.setOnClickListener {
            if(myPhone.text.toString() == ""){
                Toast.makeText(this,"Phone should be filled",Toast.LENGTH_LONG).show()
            }else {
                Generate(Gateway.MISCALL)
            }
        }

        btnWhatsapp = findViewById(R.id.btnWhatsapp)
        btnWhatsapp.setOnClickListener {
            if(myPhone.text.toString() == ""){
                Toast.makeText(this,"Phone should be filled",Toast.LENGTH_LONG).show()
            }else {
                Generate(Gateway.WHATSAPP)
            }
        }
        btnLongWhatsapp = findViewById(R.id.btnWaLong)
        btnLongWhatsapp.setOnClickListener {
            if(myPhone.text.toString() == ""){
                Toast.makeText(this,"Phone should be filled",Toast.LENGTH_LONG).show()
            }else {
                Generate(Gateway.WA_LONG)
            }
        }

        btnEmail = findViewById(R.id.btnEmail)
        btnEmail.setOnClickListener {
            if(myPhone.text.toString() == ""){
                Toast.makeText(this,"Email should be filled",Toast.LENGTH_LONG).show()
            }else {
                Generate(Gateway.EMAIL)
            }
        }

    }

    fun Generate(gateway:String) {
        val m = Fazpass.initialize("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZGVudGlmaWVyIjoxM30.SbTzA7ftEfUtkx0Rdt_eoXrafx1X9kf2SHccS_G5jS8")
        var phone = myCountry.text.toString()+ myPhone.text.toString()
        if(phone.length<12){
            Toast.makeText(this,"Phone not valid",Toast.LENGTH_LONG).show()
        }else{
            m.setGateway(gateway)
            m.generateOtp(phone) { response ->
                Fazpass.LoginPage(this, response) { status ->

                }
            }
        }

    }

}

