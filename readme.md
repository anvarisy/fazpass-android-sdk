<img src="http://fazpass.com/wp-content/uploads/2022/03/banner.png" />

[![Actions Status](https://github.com/fazpass/fazpass-android-sdk/actions/workflows/android.yml/badge.svg)](https://github.com/fazpass/fazpass-android-sdk/actions)
[![Kotlin Version](https://img.shields.io/badge/kotlin-1.7.0-blue.svg)](http://kotlinlang.org/)
[![Gradle](https://img.shields.io/badge/Gradle-7.0-blue)](https://gradle.org)
[![License](https://img.shields.io/badge/License-Mit%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

# Fazpass-Android

This is the Official Android wrapper/library for Fazpass OTP API, that is compatible with Gradle.
Visit https://fazpass.com for more information about the product and see documentation at http://docs.fazpass.com for more technical details.

## Installation
Gradle
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

```
 implementation 'com.github.fazpass:fazpass-android-sdk:TAG'
```


## Usage
```kotlin
import com.fazpass.otp.Fazpass

// initialize an object
 val m = Fazpass.initialize(MERCHANT_KEY)

//Request your otp
m.setGateway(GATEWAY_KEY)
m.requestOtp(PHONE_NUMBER/EMAIL) { response->
    //@TODO            
}

```

### Response Attribute
When generating otp was success, the response is an object with this structure
```kotlin
data class Response(
    var status:Boolean,
    var message:String,
    var type: String?,
    var error:String?,
    var target:String?,
    var data: Data?
)
data class Data(
    var id: String?,
    var otp: String?,
    var prefix: String?,
    var otp_length: String?,
    var channel: String?,
    var provider: String?,
    var purpose: String?
)
```
We already serve it.

## Validation
```kotlin
 m.verifyOtp(OTP_ID, OTP){ status->
    Log.v("Status: ","$status")
    }
```

## Template
If you feel lazy to create your generate OTP page, You can call our activity from an activity result 
```kotlin
private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
    if (result.resultCode == Activity.RESULT_OK) {
        startHomeScreen()
    }
}
@RequiresApi(Build.VERSION_CODES.M)
override fun onCreate(savedInstanceState: Bundle?) {
    startForResult.launch(Fazpass.requestPage(this, GATEWAY_KEY))
    }
```
Or maybe you just need verification page, We also have default dialog verification page
```kotlin
m.setGateway(GATEWAY_KEY)
m.requestOtp(PHONE_NUMBER/EMAIL) { response->
    Fazpass.verificationPage(this, response) { status ->

    }
}
```
Note: this function need minimum build API MARSHMALLOW

It is some sample of our template

<img src="https://firebasestorage.googleapis.com/v0/b/anvarisy-tech.appspot.com/o/ss%20request%20page.jpeg?alt=media&token=d58ef9f4-9c14-4c49-8ad2-25cb2484ab2c" width="27%"/>
<img src="https://firebasestorage.googleapis.com/v0/b/anvarisy-tech.appspot.com/o/sample_verification.jpeg?alt=media&token=2b62a77f-d05c-4609-84c5-08a749f44a8c" width="27%"/>
<img src="https://firebasestorage.googleapis.com/v0/b/anvarisy-tech.appspot.com/o/saple_missedcall.jpeg?alt=media&token=3e68cf7c-5133-41c1-a813-85ea74f3d97f" width="27%"/>

<img src="https://firebasestorage.googleapis.com/v0/b/anvarisy-tech.appspot.com/o/sample_sms.jpeg?alt=media&token=7a2a7ec9-4dfa-47cc-bcf9-26e78de8796a" width="27%"/>
<img src="https://firebasestorage.googleapis.com/v0/b/anvarisy-tech.appspot.com/o/sample_email.jpeg?alt=media&token=e46f13e3-462a-4183-bb3b-a6d8217cd1c0" width="27%"/>

## Conclusion
We will try to always make it simple and secure
<img src="https://github.githubassets.com/images/icons/emoji/unicode/2665.png?v8" width="20" height="20"/>



## License
[MIT](https://github.com/fazpass/fazpass-android-sdk/blob/main/LICENSE)