package com.fazpass.otp

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.fazpass.otp.model.Response


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
            if (edtFazpassTarget.text.toString() != "") {
                when (requestType) {
                    "Generate" -> generate()
                    "Request" -> request()
                }
            } else {
                edtFazpassTarget.requestFocus()
                Toast.makeText(this, "Target required", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun generate() {
        val target = edtFazpassTarget.text.toString()
        val m = Otp()
        m.setGateway(gatewayKey)
        LoadingDialogOtp.showLoadingDialog(this, false)
        m.generateOtp(target, object : OnCompleteOtp<Response> {

            override fun onSuccess(result: Response) {
                result.target = target
                Otp.response = result
                toVerificationPage(result)
            }

            override fun onFailure(err: Throwable) {
                val response = Response(false, "", "generate", "", target, null)
                response.error = err.message
                toVerificationPage(response)
            }
        })
    }

    private fun request() {
        val target = edtFazpassTarget.text.toString()
        val m = Otp()
        m.setGateway(gatewayKey)
        m.requestOtp(edtFazpassTarget.text.toString(), object: OnCompleteOtp<Response> {

            override fun onSuccess(result: Response) {
                result.target = target
                Otp.response = result
                toVerificationPage(result)
            }

            override fun onFailure(err: Throwable) {
                val response = Response(false, "", "request", "", target, null)
                response.error = err.message
                toVerificationPage(response)
            }
        })
    }

    private fun toVerificationPage(response: Response) {
        FazpassOtp.verificationPage(this@RequestPageOtp, response, object: OnCompleteOtp<Unit> {
            override fun onSuccess(result: Unit) {
                val resultIntent = Intent()
                resultIntent.putExtra("status", true)
                setResult(RESULT_OK, resultIntent)
                finish()
            }

            override fun onFailure(err: Throwable) {
                println(err.message)
            }
        })
    }
}