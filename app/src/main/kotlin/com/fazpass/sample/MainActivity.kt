package com.fazpass.sample
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.fazpass.otp.Fazpass
import com.fazpass.otp.Loading


private lateinit var spinnerGateway:Spinner
private var gatewayKey = ""
private lateinit var btnGenerate: AppCompatButton
private lateinit var btnRequest: AppCompatButton

class MainActivity : AppCompatActivity() {
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            startHomeScreen()
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinnerGateway = findViewById(R.id.spnGateway)
        val spinnerAdapter = GatewayAdapter(this, MyGateway.gateways)
        spinnerGateway.adapter = spinnerAdapter
        spinnerGateway.onItemSelectedListener = object : AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                gatewayKey = MyGateway.gateways[p2].gatewayKey
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
            if (formValid()) {
                Fazpass.initialize("http://34.101.82.250:3002",
                    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZGVudGlmaWVyIjoxM30.SbTzA7ftEfUtkx0Rdt_eoXrafx1X9kf2SHccS_G5jS8")
                startForResult.launch(Fazpass.generatePage(this, gatewayKey))
            }
        }

        btnRequest = findViewById(R.id.btnRequest)
        btnRequest.setOnClickListener {
            if (formValid()) {
                Fazpass.initialize("http://34.101.82.250:3002",
                    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZGVudGlmaWVyIjoxM30.SbTzA7ftEfUtkx0Rdt_eoXrafx1X9kf2SHccS_G5jS8")
                startForResult.launch(Fazpass.requestPage(this, gatewayKey))
            }

        }
    }

    private fun formValid():Boolean{
        return if(gatewayKey == ""){
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



