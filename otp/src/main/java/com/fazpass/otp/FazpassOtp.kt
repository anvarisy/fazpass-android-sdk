package com.fazpass.otp

import android.Manifest
import android.app.Activity
import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.telecom.TelecomManager
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
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
            imm.hideSoftInputFromWindow(view.windowToken, 0);
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

        @RequiresApi(Build.VERSION_CODES.M)
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
                Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS),ConsOtp.BASE_PERMISSION)
        }

        fun Activity.enableService(){
            enablePermissions()
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                try {
                    val roleManager = getSystemService(RoleManager::class.java)

                    val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING)
                    this.startActivityForResult(intent,ConsOtp.BASE_ROLE_MANAGER)
                }catch (e:Exception){
                    Log.e("ERROR", e.message.toString())
                }
            }else{
                val telecomManager = getSystemService(AppCompatActivity.TELECOM_SERVICE) as TelecomManager
                if (packageName != telecomManager.defaultDialerPackage) {
                    val intent = Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
                        .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName)
                    this.startActivity(intent)
                }else{
                    Log.e("ERROR","This device don't have dialer support")
                }
            }

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