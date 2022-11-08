package com.fazpass.otp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.chaos.view.PinView
import com.fazpass.otp.FazpassOtp.Companion.registerDialog
import com.fazpass.otp.FazpassOtp.Companion.unRegisterDialog
import com.fazpass.otp.HelperOtp.Companion.makeLinks
import com.fazpass.otp.model.Data
import com.fazpass.otp.model.Response
import com.google.android.material.button.MaterialButton
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

internal class VerificationPageOtp(otpResponse: Response, onComplete: OnCompleteOtp<Unit>) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireActivity(), theme) {
            override fun onBackPressed() {
              dismiss()
            }
        }
    }

    val complete = onComplete
    private var response = otpResponse
    private lateinit var digitContainer: LinearLayout
    private lateinit var btnVerify: MaterialButton
    private lateinit var imgLogo: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvTarget: TextView
    private lateinit var tvDetail: TextView
    private lateinit var tvResend: TextView
    private lateinit var pinView:PinView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        isCancelable = false
        return inflater.inflate(R.layout.fazpass_verification, container, false)
    }
    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var otpLength = 0
        try{
            otpLength = response.data?.otp_length.toString().toInt()
        }catch (e:java.lang.Exception){
        }

        initId(view, otpLength)

        btnVerify.setOnClickListener {
            val otp = pinView.text.toString()
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
                LoadingDialogOtp.showLoadingDialog(view.context, false)
                val m = Otp()
                when (response.target) {
                    "generate" -> {
                        response.target?.let { target -> m.generateOtp(target, object: OnCompleteOtp<Response> {

                            override fun onSuccess(result: Response) {
                                response = result
                                response.target = target
                                Otp.response = response
                                LoadingDialogOtp.hideLoading()
                            }

                            override fun onFailure(err: Throwable) {
                                response = Response(false, "", "generate", "", target, null)
                                response.error = err.message
                                LoadingDialogOtp.hideLoading()
                            }

                        })
                        }
                    }
                    "send" -> {
                        response.target?.let { target ->
                            response.data?.otp?.let { otp ->
                                m.sendOtp(target, otp, object: OnCompleteOtp<Response> {

                                    override fun onSuccess(result: Response) {
                                        response = result
                                        response.target = target
                                        Otp.response = response
                                        LoadingDialogOtp.hideLoading()
                                    }

                                    override fun onFailure(err: Throwable) {
                                        response = Response(false, "", "send", "", target, Data("", otp, "", "", "", "", ""))
                                        response.error = err.message
                                        LoadingDialogOtp.hideLoading()
                                    }
                                })
                            }
                        }
                    }
                    "request" -> {
                        response.target?.let { target ->
                            m.requestOtp(target, object : OnCompleteOtp<Response> {

                                override fun onSuccess(result: Response) {
                                    response = result
                                    response.target = target
                                    Otp.response = response
                                    LoadingDialogOtp.hideLoading()
                                }

                                override fun onFailure(err: Throwable) {
                                    response = Response(false, "", "request", "", target, null)
                                    response.error = err.message
                                    LoadingDialogOtp.hideLoading()
                                }
                            })
                        }
                    }
                }

            }

        }))

    }


    private fun verify(otp: String, context: Context){
        removeKeyboard()
        if(FazpassOtp.isOnline(context)){
            LoadingDialogOtp.showLoadingDialog(context, false)
            val m = Otp()
            val otpId = response.data?.id
            if (otpId != null) {
                m.verifyOtp(otpId, otp, object: OnCompleteOtp<Boolean> {

                    override fun onSuccess(result: Boolean) {
                        LoadingDialogOtp.hideLoading()
                        complete.onSuccess(Unit)
                    }

                    override fun onFailure(err: Throwable) {
                        LoadingDialogOtp.hideLoading()
                        complete.onFailure(err)
                    }
                })
            }else{
                LoadingDialogOtp.hideLoading()
                complete.onFailure(Throwable("Otp Id is null"))
            }
        }else{
            Toast.makeText(context,"Verification failed cause you are in offline mode", Toast.LENGTH_LONG).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initId(view:View, otpLength: Int){

        digitContainer = view.findViewById(R.id.llValidationOtpDigit)
        imgLogo = view.findViewById(R.id.imgValidationLogo)
        tvTitle = view.findViewById(R.id.tvValidationTitle)
        tvTarget = view.findViewById(R.id.tvValidationMobile)
        tvDetail = view.findViewById(R.id.tvPleaseInsert)
        var x = ""
        for (index in 0 until otpLength) {
            x+="X"
        }

        val drawableId : Int
        val titleId : Int
        val targetString : String
        val detailString : String
        when (response.data?.channel.toString().uppercase()) {
            "SMS" -> {
                drawableId = R.drawable.sms
                titleId = R.string.we_send_verification_code_to_your_sms
                targetString = response.target?.dropLast(4).plus("XXXX")
                detailString = getString(R.string.please_insert_your_verification_code)
            }
            "MISSCALL" -> {
                drawableId = R.drawable.call
                titleId = R.string.we_send_verification_code_as_a_missed_call
                targetString = response.data?.prefix?.plus(x) ?: ""
                detailString = getString(R.string.please_insert_your_miscall_last_number, otpLength)
            }
            "EMAIL" -> {
                drawableId = R.drawable.email
                titleId = R.string.we_send_verification_code_to_your_email
                targetString = response.target?.replaceRange(3,8,"xxxxx") ?: ""
                detailString = getString(R.string.please_insert_your_verification_code)
            }
            else -> {
                drawableId = R.drawable.whatsapp
                titleId = R.string.we_send_verification_code_to_your_whatsapp
                targetString = response.target?.dropLast(4).plus("XXXX")
                detailString = getString(R.string.please_insert_your_verification_code)
            }
        }
        imgLogo.setImageResource(drawableId)
        tvTitle.setText(titleId)
        tvTarget.text = targetString
        tvDetail.text = detailString

        pinView = view.findViewById(R.id.otpPin)
        pinView.itemCount= otpLength
        pinView.setHideLineWhenFilled(false)
        pinView.itemHeight= 42 *3
        pinView.itemRadius= 4 * 3
        pinView.itemSpacing= 4 * 3
        pinView.lineWidth= 3
        pinView.itemWidth= 35 * 3
        pinView.setLineColor(view.context.getColor(R.color.grey))
        btnVerify = view.findViewById(R.id.button)
    }

    private fun removeKeyboard(){
        val imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = requireActivity().currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onOtpRead(otp: String){
        Log.e("READ OTP",otp)
        pinView.setText(otp)
    }

    override fun onStart() {
        super.onStart()
        registerDialog()
    }

    override fun onStop() {
        super.onStop()
        unRegisterDialog()
    }
}

