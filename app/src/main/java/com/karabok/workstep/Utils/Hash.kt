package com.karabok.workstep.Utils

import com.karabok.workstep.Loguru.Luna
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class Hash {
    companion object {
        private val hexArray = "0123456789ABCDEF".toCharArray()

        fun sha256(password: String): String {
            try {
                val digest = MessageDigest.getInstance("SHA-256")
                val passwordBytes = password.toByteArray()
                val hashedBytes = digest.digest(passwordBytes)
                val hashedPassword = bytesToHex(hashedBytes)
                return hashedPassword
            } catch (e: NoSuchAlgorithmException) {
                Luna.error(e.toString())
                return ""
            }
        }

        // Вспомогательная функция для преобразования байтов в шестнадцатеричный формат
        private fun bytesToHex(bytes: ByteArray): String {
            val hexChars = CharArray(bytes.size * 2)
            for (i in bytes.indices) {
                val v = bytes[i].toInt() and 0xFF
                hexChars[i * 2] = hexArray[v ushr 4]
                hexChars[i * 2 + 1] = hexArray[v and 0x0F]
            }
            return String(hexChars)
        }
    }
}
