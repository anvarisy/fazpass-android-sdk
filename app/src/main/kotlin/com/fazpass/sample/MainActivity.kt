package com.fazpass.sample
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.fazpass.otp.Fazpass
import com.fazpass.otp.views.Loading


private lateinit var spinnerGateway:Spinner
private var gatewayKey = ""
private lateinit var btnGenerate: AppCompatButton
private lateinit var btnRequest: AppCompatButton
private lateinit var btnSend: AppCompatButton
private lateinit var edtTarget:EditText
private lateinit var edtOtp:EditText

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edtTarget = findViewById(R.id.edtTarget)
        edtOtp = findViewById(R.id.edtOtp)

        spinnerGateway = findViewById(R.id.spnGateway)
        val spinnerAdapter = GatewayAdapter(this, MyGateway.gateways)
        spinnerGateway.adapter = spinnerAdapter
        spinnerGateway.onItemSelectedListener = object : AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                gatewayKey = MyGateway.gateways.get(p2).gatewayKey
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                TODO("Not yet implemented")
            }
        }

        btnGenerate = findViewById(R.id.btnGenerate)
        btnGenerate.setOnClickListener {
            if(formValid()){
                Loading.showDialog(this, false)
                generateOtp(edtTarget.text.toString())
            }
        }

        btnRequest = findViewById(R.id.btnRequest)
        btnRequest.setOnClickListener {
            if (formValid()) {
                Loading.showDialog(this, false)
                requestOtp(edtTarget.text.toString())
            }
        }

        btnSend = findViewById(R.id.btnSend)
        btnSend.setOnClickListener {
            if (formValid() ) {
                if(edtOtp.text.toString() != ""){
                    Loading.showDialog(this, false)
                    sendOtp(edtTarget.text.toString(), edtOtp.text.toString())
                }else{
                    Toast.makeText(this,"Field otp can't be empty", Toast.LENGTH_LONG).show()
                }

            }
        }


    }

    private fun generateOtp(target: String){
        var m = Fazpass.initialize("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZGVudGlmaWVyIjoxM30.SbTzA7ftEfUtkx0Rdt_eoXrafx1X9kf2SHccS_G5jS8")
        m.setGateway(gatewayKey)
        m.generateOtp(target) { response ->
            Loading.hideLoading()
            if (!response.status){
                Toast.makeText(this, "Error happen cause ${response.error}", Toast.LENGTH_LONG).show()
            }else{
                Fazpass.verificationPage(this, response) { status ->
                    if(status){
                        Fazpass.dismissPage(this)
                        startHomeScreen()
                    }
                    Toast.makeText(this,"Verification status: $status",Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun requestOtp(target:String){
        var m = Fazpass.initialize("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZGVudGlmaWVyIjoxM30.SbTzA7ftEfUtkx0Rdt_eoXrafx1X9kf2SHccS_G5jS8")
        m.setGateway(gatewayKey)
        m.requestOtp(target) { response ->
            Loading.hideLoading()
            if (!response.status){
                Toast.makeText(this, "Error happen cause ${response.error}", Toast.LENGTH_LONG).show()
            }else{
                Fazpass.verificationPage(this, response) { status ->
                    if(status){
                        Fazpass.dismissPage(this)
                        startHomeScreen()
                    }
                    Toast.makeText(this,"Verification status: $status",Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun sendOtp(target:String, otp: String){
        var m = Fazpass.initialize("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZGVudGlmaWVyIjoxM30.SbTzA7ftEfUtkx0Rdt_eoXrafx1X9kf2SHccS_G5jS8")
        m.setGateway(gatewayKey)
        m.sendOtp(target, otp) { response ->
            Loading.hideLoading()
            if (!response.status){
                Toast.makeText(this, "Error happen cause ${response.error}", Toast.LENGTH_LONG).show()
            }else{
                Fazpass.verificationPage(this, response) { status ->
                    if(status){
                        Fazpass.dismissPage(this)
                        startHomeScreen()
                    }
                    Toast.makeText(this,"Verification status: $status",Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun formValid():Boolean{
        return if (edtTarget.text.toString() == "") {
            Toast.makeText(this,"Field email / phone can't be empty", Toast.LENGTH_LONG).show()
            edtTarget.requestFocus()
            false
        }else if(gatewayKey == ""){
            Toast.makeText(this,"Select gateway that will you use", Toast.LENGTH_LONG).show()
            spinnerGateway.requestFocus()
            false
        }else{
            true
        }
    }

    private fun startHomeScreen(){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}





/*

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
        var m = Fazpass.initialize("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZGVudGlmaWVyIjoxM30.SbTzA7ftEfUtkx0Rdt_eoXrafx1X9kf2SHccS_G5jS8")
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
        var m = Fazpass.initialize("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZGVudGlmaWVyIjoxM30.SbTzA7ftEfUtkx0Rdt_eoXrafx1X9kf2SHccS_G5jS8")
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
*/
