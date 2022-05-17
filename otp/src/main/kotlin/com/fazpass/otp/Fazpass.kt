package com.fazpass.otp

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
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

        fun loginPage(activity: AppCompatActivity, it:GenerateOtpResponse, onComplete:(Boolean)->Unit){
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

        @RequiresApi(Build.VERSION_CODES.M)
        fun isOnline(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivityManager != null) {
                val capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                        return true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                        return true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                        return true
                    }
                }
            }
            return false
        }

    }


}