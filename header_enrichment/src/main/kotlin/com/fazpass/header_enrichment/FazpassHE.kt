package com.fazpass.header_enrichment

import android.app.Activity
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.fazpass.header_enrichment.model.request.AuthRequest
import com.fazpass.header_enrichment.model.request.CheckResultRequest
import com.fazpass.header_enrichment.model.response.AuthResponse
import com.fazpass.header_enrichment.model.response.BaseResponse
import com.fazpass.header_enrichment.model.response.CheckResultResponse
import com.fazpass.header_enrichment.model.response.RedirectAuthResponse
import com.google.zxing.integration.android.IntentIntegrator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

open class FazpassHE {

    companion object {
        private val he = HE()
        internal var baseUrl : String = ""
        internal var merchantKey : String = ""

        fun initialize(baseUrl: String, merchantKey: String) {
            FazpassHE.baseUrl = baseUrl
            FazpassHE.merchantKey = merchantKey
        }

        fun authenticateWithUser(username: String, password: String, onComplete: OnComplete<Unit?>) {
            he.auth(username, password, object: OnComplete<BaseResponse<AuthResponse>> {

                override fun onSuccess(result: BaseResponse<AuthResponse>) {
                    result.data?.redirectUrl?.let { authenticate(it, onComplete) }
                }

                override fun onFailure(err: Throwable) {
                    onComplete.onFailure(err)
                }
            })
        }

        fun authenticateWithQR(activity: AppCompatActivity, onComplete: OnComplete<Unit?>) {
            val intentIntegrator = IntentIntegrator(activity)
            intentIntegrator.setDesiredBarcodeFormats(listOf(IntentIntegrator.QR_CODE))
            val startForResult = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val intentResult = IntentIntegrator.parseActivityResult(result.resultCode, result.data)
                    val url = intentResult.contents

                    authenticate(url, onComplete)
                }

                onComplete.onFailure(Error("Canceled."))
            }
            startForResult.launch(intentIntegrator.createScanIntent())
        }

        private fun authenticate(url: String, onComplete: OnComplete<Unit?>) {
            he.redirectAuth(url, object: OnComplete<BaseResponse<RedirectAuthResponse>> {

                override fun onSuccess(result: BaseResponse<RedirectAuthResponse>) {
                    he.checkResult(object: OnComplete<BaseResponse<CheckResultResponse>> {

                        override fun onSuccess(result: BaseResponse<CheckResultResponse>) {
                            onComplete.onSuccess(null)
                        }

                        override fun onFailure(err: Throwable) {
                            onComplete.onFailure(err)
                        }
                    })
                }

                override fun onFailure(err: Throwable) {
                    onComplete.onFailure(err)
                }
            })
        }
    }

    private class HE : BaseHE {
        override fun auth(
            username: String,
            password: String,
            onComplete: OnComplete<BaseResponse<AuthResponse>>
        ) {
            val fazpass by lazy { UseCase.start() }
            val request = AuthRequest(username, password)
            fazpass.auth("Bearer $merchantKey", request)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(onComplete::onSuccess, onComplete::onFailure)
        }

        override fun redirectAuth(
            url: String,
            onComplete: OnComplete<BaseResponse<RedirectAuthResponse>>
        ) {
            val fazpass by lazy { UseCase.start(url) }
            fazpass.redirectAuth()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(onComplete::onSuccess, onComplete::onFailure)
        }

        override fun checkResult(onComplete: OnComplete<BaseResponse<CheckResultResponse>>) {
            val fazpass by lazy { UseCase.start() }
            val request = CheckResultRequest(null)
            fazpass.redirectCheckResult("Bearer $merchantKey", request)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(onComplete::onSuccess, onComplete::onFailure)
        }
    }
}