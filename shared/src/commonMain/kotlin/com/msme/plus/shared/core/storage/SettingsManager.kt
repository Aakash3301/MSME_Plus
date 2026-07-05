package com.msme.plus.shared.core.storage

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set

class SettingsManager {
    private val settings: Settings by lazy { Settings() }

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_MSME_PROFILE = "msme_profile"
    }

    fun saveToken(token: String) {
        settings[KEY_ACCESS_TOKEN] = token
    }

    fun getToken(): String? {
        return settings.getStringOrNull(KEY_ACCESS_TOKEN)
    }

    fun saveMsmeProfile(profileJson: String) {
        settings[KEY_MSME_PROFILE] = profileJson
    }

    fun getMsmeProfile(): String? {
        return settings.getStringOrNull(KEY_MSME_PROFILE)
    }

    fun clearToken() {
        settings.remove(KEY_ACCESS_TOKEN)
        settings.remove(KEY_MSME_PROFILE)
    }
}
