package com.duma.dicodingevent.ui.setting

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingPreferences private constructor(private var dataStore: DataStore<Preferences>){

    private val themeKey = booleanPreferencesKey("theme_setting")

    fun getThemeSetting(): kotlinx.coroutines.flow.Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[themeKey] ?: false
        }
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[themeKey] = isDarkModeActive
        }
    }

    private val dailyRemainderKey = booleanPreferencesKey("daily_reminder_setting")

    fun getDailyReminderSetting(): kotlinx.coroutines.flow.Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[dailyRemainderKey] ?: false
        }
    }

    suspend fun saveDailyReminderSetting(isEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[dailyRemainderKey] = isEnabled
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}