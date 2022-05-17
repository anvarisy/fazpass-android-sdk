package com.fazpass.otp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.text.*
import android.text.InputFilter.LengthFilter
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.fazpass.otp.model.GenerateOtpResponse
import com.fazpass.otp.utils.Helper.Companion.makeLinks
import com.fazpass.otp.views.Loading
import com.google.android.material.button.MaterialButton


/**
 * Created by Anvarisy on 5/12/2022.
 * fazpass
 * anvarisy@fazpass.com
 */

internal class FazpassLoginPage(onComplete:(Boolean)->Unit, otpResponse: GenerateOtpResponse) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(activity!!, theme) {
            override fun onBackPressed() {
              dismiss()
            }
        }
    }

    val complete = onComplete
    private var response = otpResponse
    private lateinit var digitContainer: LinearLayout
    private lateinit var digit: EditText
    private lateinit var btnVerify: MaterialButton
    private lateinit var imgLogo: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvTarget: TextView
    private lateinit var tvDetail: TextView
    private lateinit var tvResend: TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        return inflater.inflate(R.layout.fazpass_login, container, false)
    }
    override fun getTheme(): Int {
        return R.style.DialogTheme
    }
    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var otpLength = 0
        try{
            otpLength = response.data?.otp_length.toString().toInt()
        }catch (e:java.lang.Exception){
        }

        digitContainer = view.findViewById(R.id.llValidationOtpDigit)
        imgLogo = view.findViewById(R.id.imgValidationLogo)
        tvTitle = view.findViewById(R.id.tvValidationTitle)
        tvTarget = view.findViewById(R.id.tvValidationMobile)
        tvDetail = view.findViewById(R.id.tvPleaseInsert)
        var x = ""
        for (index in 0 until otpLength) {
            x+="X"
        }
        if(response.data?.channel.toString().uppercase()=="SMS"){
            imgLogo.setImageResource(R.drawable.sms)
            tvTitle.setText(R.string.we_send_verification_code_to_your_sms)
            tvTarget.setText(response.phone?.dropLast(4).plus("XXXX"))
            tvDetail.setText(R.string.please_insert_your_verification_code)
        }else if (response.data?.channel.toString().uppercase()=="MISSCALL"){
            imgLogo.setImageResource(R.drawable.call)
            tvTitle.setText(R.string.we_send_verification_code_as_a_missed_call)
            tvTarget.setText(response.data?.prefix?.plus(x))
            tvDetail.setText("Please insert $otpLength digit of last number that missed call you")
        }else if (response.data?.channel.toString().uppercase()=="WHATSAPP"){
            imgLogo.setImageResource(R.drawable.whatsapp)
            tvTitle.setText(R.string.we_send_verification_code_to_your_whatsapp)
            tvTarget.setText(response.phone?.dropLast(4).plus("XXXX"))
            tvDetail.setText(R.string.please_insert_your_verification_code)
        }else if (response.data?.channel.toString().uppercase()=="WA_LONG_NUMBER"){
            imgLogo.setImageResource(R.drawable.whatsapp)
            tvTitle.setText(R.string.we_send_verification_code_to_your_whatsapp)
            tvTarget.setText(response.phone?.dropLast(4).plus("XXXX"))
            tvDetail.setText(R.string.please_insert_your_verification_code)
        }else if (response.data?.channel.toString().uppercase()=="EMAIL"){
            imgLogo.setImageResource(R.drawable.email)
            tvTitle.setText(R.string.we_send_verification_code_to_your_email)
            tvTarget.setText(response.phone?.replaceRange(3,8,"xxxxx"))
            tvDetail.setText(R.string.please_insert_your_verification_code)
        }

        val border = GradientDrawable()
        border.setColor(-0x1) //white background
        border.cornerRadius = 10f
        border.setStroke(2, -0x75757575)

        val idCollection = ArrayList<Int>()
        for (index in 0 until otpLength) {
            digit = EditText(context)
            idCollection.add(index)
            if (otpLength > 6) {
                digit.width = 60
            }
            digit.width = 85
            digit.gravity = Gravity.CENTER
            digit.id = index
            digit.setTextColor(R.color.black)
            digit.inputType = InputType.TYPE_CLASS_NUMBER
            digit.filters = arrayOf<InputFilter>(LengthFilter(1))
            digit.background = border
           /* digit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    val digitFocus = view.findViewById<EditText>(idCollection[0])
                    digitFocus.requestFocus()
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    try {
                        val digitFocus = view.findViewById<EditText>(idCollection[index + 1])
                        digitFocus.requestFocus()
                    } catch (e: Exception) {
                        digit.setFocusableInTouchMode(false);
                        digit.setFocusable(false);
                        digit.setFocusableInTouchMode(true);
                        digit.setFocusable(true);
                    }
                }

                override fun afterTextChanged(editable: Editable) {
                }
            })*/
            digitContainer.addView(digit)
        }
        btnVerify = view.findViewById(R.id.button)
        btnVerify.setOnClickListener {
            var otp = ""
            for (index in 0 until otpLength) {
                val digit = view.findViewById<EditText>(idCollection[index])
                otp+=digit.text.toString()
            }
            if(otp.length==otpLength){
                verify(otp, view.context)
            }else{
                Toast.makeText(view.context,"OTP has not been filled completely",Toast.LENGTH_LONG).show()
            }

        }

        tvResend = view.findViewById(R.id.tvResend)
        tvResend.setText(R.string.not_received_the_code_resend_here)
        tvResend.makeLinks( Pair("here", View.OnClickListener {
            if(otpLength<=0){
                dismiss()
            }else{
                Loading.displayLoadingWithText(view.context,false)
                val m = Merchant()
                response.phone?.let { phone -> m.generateOtp(phone) {
                    Loading.hideLoading()
                } }
            }

        }))

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun verify(otp: String, context: Context){
        removeKeyboard()
        if(Fazpass.isOnline(context)){
            Loading.displayLoadingWithText(context,false)
            val m = Merchant()
            val otpId = response.data?.id
            if (otpId != null) {
                m.verifyOtp(otpId, otp){
                    Loading.hideLoading()
                    complete(it)
                }
            }else{
                Loading.hideLoading()
                complete(false)
            }
        }else{
            Toast.makeText(context,"Verification failed cause you are in offline mode", Toast.LENGTH_LONG).show()
        }

    }

    private fun removeKeyboard(){
        val imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity!!.currentFocus
        if (view == null) {
            view = View(activity)
        }

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



}