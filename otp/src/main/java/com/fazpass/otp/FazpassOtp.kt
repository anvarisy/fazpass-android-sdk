package com.fazpass.otp

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.fazpass.otp.model.Response
import org.greenrobot.eventbus.EventBus


class FazpassOtp {

    companion object{

        fun initialize(url: String, key:String):Otp{
            Otp.baseUrl = url
            Otp.merchantKey = key
            return Otp()
        }

        fun verificationPage(activity: AppCompatActivity, it:Response, onComplete:(Boolean)->Unit){
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            var view = activity.currentFocus
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            val dialogFragment = VerificationPageOtp(onComplete, it)
            dialogFragment.show(activity.supportFragmentManager, "fazpass_verification")
        }

        fun dismissPage(activity: AppCompatActivity){
            val prev: Fragment? = activity.supportFragmentManager.findFragmentByTag("fazpass_verification")
            if (prev != null) {
                val df: DialogFragment = prev as DialogFragment
                df.dismiss()
            }
        }

        fun isOnline(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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
            return false
        }

        fun requestPage(activity: AppCompatActivity, gatewayKey: String): Intent{
            return startPage(activity,gatewayKey,"Request")
        }

        fun generatePage(activity: AppCompatActivity, gatewayKey: String): Intent{
           return startPage(activity,gatewayKey,"Generate")
        }

        private fun startPage(activity: AppCompatActivity, gatewayKey: String, type: String):Intent{
            return (Intent(activity, RequestPageOtp::class.java).
            putExtra("fazpass_request_type",type).
            putExtra("fazpass_request_gateway",gatewayKey))

        }

        fun Activity.enablePermissions(){
            requestPermissions(arrayOf(Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS),ConsOtp.BASE_PERMISSION)
        }

        fun Activity.registerActivity(){
            EventBus.getDefault().register(this)
        }

        fun Activity.unRegisterActivity(){
            EventBus.getDefault().unregister(this)
        }

        fun DialogFragment.registerDialog(){
            EventBus.getDefault().register(this)
        }

        fun DialogFragment.unRegisterDialog(){
            EventBus.getDefault().unregister(this)
        }

        fun Fragment.registerFragment(){
            EventBus.getDefault().register(this)
        }

        fun Fragment.unRegisterFragment(){
            EventBus.getDefault().unregister(this)
        }

    }

}