package com.fazpass.header_enrichment

import com.fazpass.header_enrichment.model.response.*

internal interface BaseHE {
    fun auth(username: String, password: String, onComplete: OnComplete<BaseResponse<AuthResponse>>)
    fun redirectAuth(url: String, onComplete: OnComplete<BaseResponse<RedirectAuthResponse>>)
    fun checkResult(onComplete: OnComplete<BaseResponse<CheckResultResponse>>)
}
