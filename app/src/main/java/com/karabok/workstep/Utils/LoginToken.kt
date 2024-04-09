package com.karabok.workstep.Utils

import android.content.SharedPreferences

object LoginToken {

    // Ключ для сохранения токена доступа в SharedPreferences
    private const val TOKEN_KEY = "token_key"

    public fun saveToken(token: String, sharedPreferences: SharedPreferences?) {
        // Сохраняем токен доступа в SharedPreferences
        val editor = sharedPreferences?.edit()
        editor?.putString(TOKEN_KEY, token)
        editor?.apply()
    }

    public fun getToken(sharedPreferences: SharedPreferences?): String? {
        return sharedPreferences?.getString(TOKEN_KEY, null)
    }

    public fun isLogged(sharedPreferences: SharedPreferences?): Boolean {
        // Проверяем, сохранен ли токен доступа в SharedPreferences
        return sharedPreferences?.getString(TOKEN_KEY, null) != null
    }
}