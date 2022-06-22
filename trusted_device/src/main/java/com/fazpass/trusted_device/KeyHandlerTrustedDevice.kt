@file:Suppress("DEPRECATION")

package com.fazpass.trusted_device

import android.content.Context
import android.os.Build
import android.security.KeyPairGeneratorSpec
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import java.math.BigInteger
import java.security.*
import java.util.*
import javax.security.auth.x500.X500Principal
import kotlin.math.abs

internal class KeyHandlerTrustedDevice {

    private fun getKeyStoreAlias():String{
        return ""
    }

    private fun getPrivateKey(): PrivateKey? {
        try {
            val keyStore = KeyStore.getInstance(ConsTrustedDevice.KEY_STORE_ALIAS)
            keyStore.load(null)
            return keyStore.getKey(getKeyStoreAlias(), null) as PrivateKey
        } catch (e: Exception) {
            Log.e("get_private_key",e.message.toString())
        }
        return null
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun generateKeyPair(context: Context):String?{
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val keyGenParameterSpec = KeyGenParameterSpec.Builder(getKeyStoreAlias(), KeyProperties.PURPOSE_SIGN)
                    .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                    .build()

                val keyPairGenerator: KeyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_EC, ConsTrustedDevice.KEY_STORE_ALIAS)
                keyPairGenerator.initialize(keyGenParameterSpec)
                val keyPair = keyPairGenerator.generateKeyPair()
                return Base64.encodeToString(keyPair.public.encoded, Base64.DEFAULT)
            }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                val keyPairGenerator: KeyPairGenerator = KeyPairGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_RSA, ConsTrustedDevice.KEY_STORE_ALIAS)
                val start = Calendar.getInstance()
                val end = Calendar.getInstance()
                end.add(Calendar.YEAR, 5)

                val specs = KeyPairGeneratorSpec.Builder(context)
                    .setAlias(getKeyStoreAlias())
                    .setSubject(X500Principal("CN=" + getKeyStoreAlias())) // TODO: Find out what this does
                    .setSerialNumber(
                        BigInteger.valueOf(
                            abs(getKeyStoreAlias().hashCode()).toLong()
                        )
                    ) // TODO: Find out what this does
                    .setStartDate(start.time)
                    .setEndDate(end.time)
                    .build()

                keyPairGenerator.initialize(specs)
                val keyPair = keyPairGenerator.generateKeyPair()
                return Base64.encodeToString(keyPair.public.encoded, Base64.DEFAULT)
            }else{
                Log.e("generate_key_pair", "API Level lower than 18 not supported for Trusted Devices")
            }

        }catch (e: Exception){
            Log.e("generate_key_pair", e.message.toString())
        }
        return null
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getPublicKey(ctx: Context): String? {
        try {
            val keyStore = KeyStore.getInstance(ConsTrustedDevice.KEY_STORE_ALIAS)
            keyStore.load(null)
            val publicKey =
                keyStore.getCertificate(getKeyStoreAlias()).publicKey
            return Base64.encodeToString(publicKey.encoded, Base64.DEFAULT)
        } catch (e: NullPointerException) {
            return generateKeyPair(ctx)
        } catch (e: NoSuchAlgorithmException) {
            return generateKeyPair(ctx)
        } catch (e: KeyStoreException) {
            return generateKeyPair(ctx)
        } catch (e: java.lang.Exception) {
            Log.e("get_public_key", e.message.toString())
        }
        return null
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getAlgorithm(ctx: Context): String? {
        try {
            getPublicKey(ctx)
            val keyStore = KeyStore.getInstance(ConsTrustedDevice.KEY_STORE_ALIAS)
            keyStore.load(null)
            val publicKey = keyStore.getCertificate(getKeyStoreAlias()).publicKey
            return publicKey.algorithm
        } catch (e: Exception) {
            Log.e("get_algorithm", e.message.toString())
        }
        return null
    }

    fun sign(stringToSign: String): String? {
        try {
            val s: Signature = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Signature.getInstance(ConsTrustedDevice.SIGNATURE_ALGO)
            } else {
                Signature.getInstance(ConsTrustedDevice.SIGNATURE_ALGO_RSA)
            }
            val privateKey: PrivateKey = getPrivateKey()!!
            s.initSign(privateKey)
            s.update(stringToSign.toByteArray())
            val signature = s.sign()
            Log.d("Message", stringToSign)
            Log.d("Signature", Base64.encodeToString(signature, Base64.DEFAULT))
            return Base64.encodeToString(signature, Base64.DEFAULT)
        } catch (e: java.lang.Exception) {
            Log.e("sign_key_handler", e.message.toString())
        }
        return null
    }
}