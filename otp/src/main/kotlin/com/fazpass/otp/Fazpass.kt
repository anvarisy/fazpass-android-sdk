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
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.fazpass.otp.model.Response
import com.fazpass.otp.views.FazpassVerificationPage

class Fazpass {

    companion object{
        fun initialize(key:String):Merchant{
            Merchant.merchantKey = key
            return Merchant()
        }

        fun verificationPage(activity: AppCompatActivity, it:Response, onComplete:(Boolean)->Unit){
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            var view = activity.currentFocus
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0);
            val dialogFragment = FazpassVerificationPage(onComplete, it)
            dialogFragment.show(activity.supportFragmentManager, "fazpass_verification")
        }

        fun dismissPage(activity: AppCompatActivity){
            val prev: Fragment? = activity.supportFragmentManager.findFragmentByTag("fazpass_verification")
            if (prev != null) {
                val df: DialogFragment = prev as DialogFragment
                df.dismiss()
            }
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