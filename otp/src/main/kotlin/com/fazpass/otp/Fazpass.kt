package com.fazpass.otp

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.fazpass.otp.model.GenerateOtpResponse


/**
 * Created by Anvarisy on 5/11/2022.
 * fazpass
 * anvarisy@fazpass.com
 */
class Fazpass {

    companion object{
        fun initialize(key:String):Merchant{
            val m = Merchant()
            m.merchantKey = key
            Merchant.merchantKey = key
            return m
        }

        fun LoginPage(activity: AppCompatActivity, it:GenerateOtpResponse, onComplete:(Boolean)->Unit){
          /*  val intent = Intent(context, FazpassLoginActivity::class.java).apply {
                putExtra("it",it as java.io.Serializable)
            }
            context.startActivity(intent)*/
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            var view = activity.currentFocus
            if (view == null) {
                view = View(activity)
            }

            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            val dialogFragment = FazpassLoginPage(onComplete, it)
            dialogFragment.show(activity.supportFragmentManager, "signature")
            /*if(it.data==null){
                val builder = AlertDialog.Builder(activity)
                builder.setTitle("Alert !")
                builder.setMessage("Generate Otp Failed")
                builder.setPositiveButton("Close") { dialog, _ ->
                    dialog.dismiss()
                }
                builder.show()
            }else{
                val dialogFragment = FazpassLoginPage(onComplete, it)
                dialogFragment.show(activity.supportFragmentManager, "signature")
            }*/

        }

    }


}