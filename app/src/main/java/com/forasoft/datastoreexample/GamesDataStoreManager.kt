package com.forasoft.datastoreexample

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GamesDataStoreManager(private val context: Context) {
    private val dataStore
        get() = context.gamesDataStore

    val valueFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[GAME_KEY] ?: ""
    }

    companion object {
        val GAME_KEY = stringPreferencesKey("game_key")
    }

    suspend fun saveValue(value: String) {
        dataStore.edit { preferences ->
            preferences[GAME_KEY] = value
        }
    }
}