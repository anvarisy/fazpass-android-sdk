package com.fazpass.header_enrichment

interface OnComplete<R> {
    fun onSuccess(result: R)
    fun onFailure(err: Throwable)
}