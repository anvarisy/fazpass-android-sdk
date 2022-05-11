package com.fazpass.otp


/**
 * Created by Anvarisy on 5/11/2022.
 * fazpass
 * anvarisy@fazpass.com
 */
class Fazpass {
    companion object{
        fun initialize(key:String):Merchant{
            val m = Merchant()
            m.merchantKey = key
            return m
        }
    }

}