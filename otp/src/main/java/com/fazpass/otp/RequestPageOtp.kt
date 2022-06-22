package com.fazpass.otp

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton


internal class RequestPageOtp : AppCompatActivity() {
    private lateinit var btnRequest: AppCompatButton
    private lateinit var edtFazpassTarget: EditText
    var gatewayKey = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fazpass_request)
        val extras = intent.extras
        val requestType = extras?.getString("fazpass_request_type")
        gatewayKey = extras?.getString("fazpass_request_gateway").toString()
        edtFazpassTarget = findViewById(R.id.edtFazpassTarget)
        btnRequest = findViewById(R.id.btnFazpassRequest)
        btnRequest.text = requestType
        btnRequest.setOnClickListener {
            if(edtFazpassTarget.text.toString()!= ""){
                when(requestType){
                    "Generate"-> generate()
                    "Request"-> request()
                }
            }else{
                edtFazpassTarget.requestFocus()
                Toast.makeText(this, "Target required",Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun generate(){
        val m = Otp()
        m.setGateway(gatewayKey)
        LoadingDialogOtp.showLoadingDialog(this,false)
        m.generateOtp(edtFazpassTarget.text.toString()) { it ->
            FazpassOtp.verificationPage(this, it) { status ->
                if(status){
                    val resultIntent = Intent()
                    resultIntent.putExtra("status", status)
                    setResult(RESULT_OK, resultIntent)
                    finish()
                }
            }
        }
    }

    private fun request(){
        val m = Otp()
        m.setGateway(gatewayKey)
        m.requestOtp(edtFazpassTarget.text.toString()) { it ->
            FazpassOtp.verificationPage(this, it) { status ->
                if(status){
                    val resultIntent = Intent()
                    resultIntent.putExtra("status", status)
                    setResult(RESULT_OK, resultIntent)
                    finish()
                }
            }
        }
    }
}