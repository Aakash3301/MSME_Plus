package com.msme.plus.shared.core.storage

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set

class SettingsManager {
    private val settings: Settings by lazy { Settings() }

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
    }

    fun saveToken(token: String) {
        settings[KEY_ACCESS_TOKEN] = token
    }

    fun getToken(): String? {
        return settings.getStringOrNull(KEY_ACCESS_TOKEN)
    }

    fun clearToken() {
        settings.remove(KEY_ACCESS_TOKEN)
    }
}
