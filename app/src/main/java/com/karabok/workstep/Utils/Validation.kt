package com.karabok.workstep.Utils

class Validation{
    companion object{
        fun String.isUpperCases(): Boolean {
            for (char in this) {
                if (char.isUpperCase()) {
                    return true
                }
            }
            return false
        }

        fun String.isDigits(): Boolean {
            for (char in this) {
                if (char.isDigit()) {
                    return true
                }
            }
            return false
        }

        fun String.isSpecialChars(): Boolean {
            val specialChars = setOf('!', '@', '#', '$', '%', '^', '&', '*')

            for (char in this) {
                if (specialChars.contains(char)) {
                    return true
                }
            }
            return false
        }

        fun String.isValidEmail(): Boolean {
            val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\$")
            return matches(emailRegex)
        }
    }
}