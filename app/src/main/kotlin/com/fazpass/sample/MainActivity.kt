package com.fazpass.sample
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
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
    @RequiresApi(Build.VERSION_CODES.M)
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
                if(Fazpass.isOnline(this)){
                    generate(Gateway.SMS)
                }else{
                    Toast.makeText(this,"Verification failed cause you are in offline mode", Toast.LENGTH_LONG).show()
                }

            }

        }

        btnMissCall = findViewById(R.id.btnMissCall)
        btnMissCall.setOnClickListener {
            if(myPhone.text.toString() == ""){
                Toast.makeText(this,"Phone should be filled",Toast.LENGTH_LONG).show()
            }else {
                if(Fazpass.isOnline(this)){
                    generate(Gateway.MISCALL)
                }else{
                    Toast.makeText(this,"Verification failed cause you are in offline mode", Toast.LENGTH_LONG).show()
                }

            }
        }

        btnWhatsapp = findViewById(R.id.btnWhatsapp)
        btnWhatsapp.setOnClickListener {
            if(myPhone.text.toString() == ""){
                Toast.makeText(this,"Phone should be filled",Toast.LENGTH_LONG).show()
            }else {
                if(Fazpass.isOnline(this)) {
                    generate(Gateway.WHATSAPP)
                }else{
                    Toast.makeText(this,"Verification failed cause you are in offline mode", Toast.LENGTH_LONG).show()
                }
            }
        }
        btnLongWhatsapp = findViewById(R.id.btnWaLong)
        btnLongWhatsapp.setOnClickListener {
            if(myPhone.text.toString() == ""){
                Toast.makeText(this,"Phone should be filled",Toast.LENGTH_LONG).show()
            }else {
                generate(Gateway.WA_LONG)
            }
        }

        btnEmail = findViewById(R.id.btnEmail)
        btnEmail.setOnClickListener {
            if(myEmail.text.toString() == ""){
                Toast.makeText(this,"Email should be filled",Toast.LENGTH_LONG).show()
            }else {
                if(android.util.Patterns.EMAIL_ADDRESS.matcher(myEmail.text.toString()).matches()){
                    var email = myEmail.text.toString()
                    generate(Gateway.EMAIL, email)
                }else{
                    Toast.makeText(this,"your text not an email !",Toast.LENGTH_LONG).show()
                }


            }
        }

    }

    private fun generate(gateway:String) {
        val m = Fazpass.initialize("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZGVudGlmaWVyIjoxM30.SbTzA7ftEfUtkx0Rdt_eoXrafx1X9kf2SHccS_G5jS8")
        var phone = myCountry.text.toString()+ myPhone.text.toString()
        if(phone.length<12){
            Toast.makeText(this,"Phone not valid",Toast.LENGTH_LONG).show()
        }else{
            m.setGateway(gateway)
            m.generateOtp(phone) { response ->
                 if (!response.status){
                     Toast.makeText(this, "Error happen cause ${response.error}", Toast.LENGTH_LONG).show()
                 }else{
                     Fazpass.loginPage(this, response) { status ->
                         //@TODO
                         // Do anything when verification complete
                         Toast.makeText(this,"Verification status: $status",Toast.LENGTH_LONG).show()
                     }
                 }
                }
            }
        }

    private fun generate(gateway:String, email:String) {
        val m = Fazpass.initialize("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZGVudGlmaWVyIjoxM30.SbTzA7ftEfUtkx0Rdt_eoXrafx1X9kf2SHccS_G5jS8")
        m.setGateway(gateway)
        m.generateOtp(email) { response ->
            if (!response.status){
                Toast.makeText(this, "Error happen cause ${response.error}", Toast.LENGTH_LONG).show()
            }else{
                Fazpass.loginPage(this, response) { status ->
                    //@TODO
                    // Do anything when verification complete
                    Toast.makeText(this,"Verification status: $status",Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}






