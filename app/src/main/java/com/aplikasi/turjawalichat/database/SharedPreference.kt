package com.aplikasi.turjawalichat.database

import android.content.Context

class SharedPreference(context: Context) {
    companion object {
        private const val PREFS_NAME = "turjawali_pref"
        private const val IS_LOGIN = "is_login"

    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val editor = preferences.edit()

    fun setLoggin(isLogin: Boolean) {
        editor?.putBoolean(IS_LOGIN, isLogin)
        editor?.commit()
    }

    fun isLogin(): Boolean {
        return preferences.getBoolean(IS_LOGIN, false)
    }
    fun removeData() {
        editor?.clear()
        editor?.commit()
    }
}