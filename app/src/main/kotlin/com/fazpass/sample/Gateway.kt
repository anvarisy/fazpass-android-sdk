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
           Gateway("SMS","98b5d429-b081-4332-ab33-ae1daa746f03", "sms"),
           Gateway("WHATSAPP","2c7aafdc-7d6c-49c5-95b8-f8476996865c", "whatsapp"),
           Gateway("EMAIL","11475737-ae16-43de-8253-2a2c0241415f", "email"),
           Gateway("MISCALL","9bbd5a07-fc1c-402e-8424-86cb970d0bf7","call"),
           Gateway("LONG WA","e954f844-f644-46ec-917f-0677fc609cee", "whatsapp"),
       )
    }
}