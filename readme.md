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
Maven
```
<dependency>
  <groupId>com.fazpass</groupId>
  <artifactId>otp</artifactId>
  <version>1.0.1</version>
</dependency>
```


## Usage
```kotlin
import com.fazpass.otp.Fazpass

// initialize an object
 val m = Fazpass.initialize(MERCHANT_KEY)

//Generate your otp
 m.setGateway(GATEWAY_KEY)
 m.generateOtp(PHONE_NUMBER/EMAIL) { response->
    //@TODO            
 }

//Request your otp
m.setGateway(GATEWAY_KEY)
m.requestOtp(PHONE_NUMBER/EMAIL) { response->
    //@TODO            
}

//Send your otp
m.setGateway(GATEWAY_KEY)
m.sendOtp(PHONE_NUMBER/EMAIL, OTP) { response->
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
If you feel lazy to create your validation page, we also have our validation page.
Only need one line and let us handle the verification.
```kotlin
 val m = Fazpass.initialize(MERCHANT_KEY)

 m.setGateway(GATEWAY_KEY)
 m.generateOtp(PHONE_NUMBER/EMAIL) { response->
     Fazpass.verificationPage(this, response) { status ->

     }          
 }
```
Note: this function need minimum build API MARSHMALLOW

It looks like this
<img src="https://raw.githubusercontent.com/fazpass/fazpass-android-sdk/main/.github/workflows/sample_verification.jpeg" width="50%"/>


## Conclusion
We will try to always make it simple and secure
<img src="https://github.githubassets.com/images/icons/emoji/unicode/2665.png?v8" width="20" height="20"/>



## License
[MIT](https://github.com/fazpass/fazpass-android-sdk/blob/main/LICENSE)