package com.fazpass.sample



data class Gateway (
    var name:String,
    var gatewayKey:String,
    var url:String
)

class MyGateway{
    companion object{
       var gateways = arrayListOf(
           Gateway("Choose your gateway","", "fazpass"),
           Gateway("SMS","", "sms"),
           Gateway("WHATSAPP","", "whatsapp"),
           Gateway("EMAIL","", "email"),
           Gateway("MISCALL","","call"),
           Gateway("LONG WA","", "whatsapp"),
       )
    }
}