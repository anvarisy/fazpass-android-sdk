package com.fazpass.header_enrichment.model.response

data class BaseResponse<D> (
    var status:Boolean,
    var message:String,
    var type: String?,
    var error:String?,
    var target:String?,
    var data: D?)