<img src="http://fazpass.com/wp-content/uploads/2022/03/banner.png" />

[![Actions Status](https://github.com/fazpass/fazpass-android-sdk/actions/workflows/android.yml/badge.svg)](https://github.com/fazpass/fazpass-android-sdk/actions)
[![Kotlin Version](https://img.shields.io/badge/kotlin-1.7.0-blue.svg)](http://kotlinlang.org/)
[![Gradle](https://img.shields.io/badge/Gradle-7.0-blue)](https://gradle.org)
[![License](https://img.shields.io/badge/License-Mit%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

# Fazpass-Android

This is the Official Android wrapper/library for Fazpass OTP API, that is compatible with Gradle.
Visit https://fazpass.com for more information about the product and see documentation at http://docs.fazpass.com for more technical details.

## Installation
Please add this line into your gradle
```
 implementation 'https://github.com/fazpass/fazpass-android-sdk'
```

##Usage
```kotlin
import com.fazpass.otp.Fazpass

// initialize an object
 val m = Fazpass.initialize(MERCHANT_KEY)

//Generate your otp
 m.setGateway(GATEWAY_KEY)
 m.generateOtp(PHONE_NUMBER) { response->
    //@TODO            
 }
```

###Response Attribute
When generating otp was success, the response is an object with this structure
```kotlin
data class GenerateOtpResponse(
    var status:Boolean,
    var message:String,
    var error:String?,
    var phone:String?,
    var data: GenerateOtpData?
)

data class GenerateOtpData(
    var id: String?,
    var otp: String?,
    var otp_length: String?,
    var channel: String?,
    var provider: String?,
    var purpose: String?
)
```
We already serve it.

##Validation
```kotlin
 m.verifyOtp(OTP_ID, OTP){ status->
    Log.v("Status: ","$status")
    }
```

##Template
If you feel lazy to create your validation page, we also have our validation page.
Only need one line and let us handle the verification
```kotlin
 val m = Fazpass.initialize(MERCHANT_KEY)

 m.setGateway(GATEWAY_KEY)
 m.generateOtp(PHONE_NUMBER) { response->
     Fazpass.LoginPage(this, response)           
 }
```
It looks like this
<img src="https://raw.githubusercontent.com/fazpass/fazpass-android-sdk/main/.github/workflows/fazpass_verification.jpeg" />


##Conclusion
We will try to always make it simple and secure :hearth:


## License
[MIT](https://choosealicense.com/licenses/mit/)