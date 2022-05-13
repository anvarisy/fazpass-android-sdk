package com.fazpass.otp

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.fazpass.otp.model.GenerateOtpResponse


/**
 * Created by Anvarisy on 5/12/2022.
 * fazpass
 * anvarisy@fazpass.com
 */

internal class FazpassLoginPage(onComplete:(Boolean)->Unit, otpResponse: GenerateOtpResponse) : DialogFragment() {
    val complete = onComplete
    private var response = otpResponse
    private lateinit var digitContainer: LinearLayout
    private lateinit var digit: EditText
    private lateinit var btnVerify: Button
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
    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var otpLength = 12
        try{
            otpLength = response.data?.otp_length.toString().toInt()
        }catch (e:java.lang.Exception){
        }

        digitContainer = view.findViewById(R.id.llValidationOtpDigit)
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
            digit.setBackgroundResource(R.drawable.rectangle_grey_border)
            digit.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    val digitFocus = view.findViewById<EditText>(idCollection[0])
                    digitFocus.requestFocus()
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    try {
                        val digitFocus = view.findViewById<EditText>(idCollection[index + 1])
                        digitFocus.requestFocus()
                    } catch (e: Exception) {

                    }
                }

                override fun afterTextChanged(editable: Editable) {}
            })
            digitContainer.addView(digit)
        }
        btnVerify = view.findViewById(R.id.button)
        btnVerify.setOnClickListener {
            var otp = ""
            for (index in 0 until otpLength) {
                val digit = view.findViewById<EditText>(idCollection[index])
                otp+=digit.text.toString()
            }
            verify(otp)
        }

    }

    fun verify(otp: String){
        val m = Merchant()
        val otpId = response.data?.id
        if (otpId != null) {
            m.verifyOtp(otpId, otp){
                complete(it)
            }
        }
    }
}