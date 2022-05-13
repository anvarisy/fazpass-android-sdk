package com.fazpass.sample
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.fazpass.otp.Fazpass


private lateinit var btnGenerate: AppCompatButton
private lateinit var btnVerification: AppCompatButton
private lateinit var myPhone: EditText
private lateinit var myOtp: EditText
var otp: String = ""
var otpId: String = ""
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnGenerate = findViewById(R.id.btnGenerate)
        myPhone = findViewById(R.id.edtPhone)
        myOtp = findViewById(R.id.edtOtp)
        btnVerification = findViewById(R.id.btnVerification)
        val m = Fazpass.initialize("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZGVudGlmaWVyIjoxM30.SbTzA7ftEfUtkx0Rdt_eoXrafx1X9kf2SHccS_G5jS8")

        btnGenerate.setOnClickListener {
            //Action perform when the user clicks on the button.
            var phone: String = myPhone.text.toString()
            m.setGateway("98b5d429-b081-4332-ab33-ae1daa746f03")
            m.generateOtp(phone) { response->
                Fazpass.LoginPage(this, response){status->
                    Log.v("STATUS","$status")
                }

            }

        }
    }
}

/*
        val m = Fazpass.initialize("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZGVudGlmaWVyIjoxM30.SbTzA7ftEfUtkx0Rdt_eoXrafx1X9kf2SHccS_G5jS8")

 */

/*      m.setGateway("9bbd5a07-fc1c-402e-8424-86cb970d0bf")
        m.generateOtp(phone) { response->
                *//*Fazpass.LoginPage(this, response)*//*
                response.data?.otp?.let { otp -> Log.v("Otp", otp) }
            }*/

/*
        m.verifyOtp("",""){status->
             if(status){
                Log.v("Status","Verification complete !")
              }
            }
*/
