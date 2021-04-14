package com.example.cheerup2120.Utils

import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class Crypto {
    companion object {
        fun getSHA256(info: String): String? {
            var hexString: StringBuilder? = null
            try {
                val data = info.toByteArray(charset("UTF-8"))
                val messageDigest = MessageDigest.getInstance("SHA-256")
                val digest = messageDigest.digest(data)
                hexString = StringBuilder()
                for (aMessageDigest in digest) {
                    var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
                    while (h.length < 2) h = "0$h"
                    hexString.append(h)
                }
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }
            return hexString.toString()
        }
    }
}