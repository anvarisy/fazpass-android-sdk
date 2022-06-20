package com.fazpass.sample
import android.app.Activity
import android.content.Intent
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


private var gatewayKey = ""
private lateinit var btnGenerate: AppCompatButton
private lateinit var btnRequest: AppCompatButton
private const val BASE_URL = "http://34.101.82.250:3002"
private const val MERCHANT_KEY_STAGING= "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZGVudGlmaWVyIjoxM30.SbTzA7ftEfUtkx0Rdt_eoXrafx1X9kf2SHccS_G5jS8"
//private val MERCHANT_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZGVudGlmaWVyIjo3fQ._BtZy1Zp3UAPz4fbgaXeAhA8rYgV2c2Y-its_WIJi9I"
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

        val spinnerGateway = findViewById<Spinner>(R.id.spnGateway)
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
                Fazpass.initialize(BASE_URL, MERCHANT_KEY_STAGING)
                startForResult.launch(Fazpass.generatePage(this, gatewayKey))
            }
        }

        btnRequest = findViewById(R.id.btnRequest)
        btnRequest.setOnClickListener {
            if (formValid()) {
                Fazpass.initialize(BASE_URL, MERCHANT_KEY_STAGING)
                startForResult.launch(Fazpass.requestPage(this, gatewayKey))
            }

        }
    }

    private fun formValid():Boolean{
        return if(gatewayKey == ""){
            Toast.makeText(this,"Select gateway that will you use", Toast.LENGTH_LONG).show()
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



