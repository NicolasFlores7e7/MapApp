package com.example.mapsapps.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPrefs(private val context: Context){

    companion object{
        val STORE_EMAIL = stringPreferencesKey("store_email")
        val STORE_PASSWORD = stringPreferencesKey("store_password")
        val STORE_DIDWELOGOUT = booleanPreferencesKey("store_didWeLogout")
    }


    val getUserData: Flow<List<String>> = context.dataStore.data.map { prefs ->
        listOf(
            prefs[STORE_EMAIL] ?: "",
            prefs[STORE_PASSWORD] ?: "",

        )
    }

    val getUserStatus: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[STORE_DIDWELOGOUT] ?: false
    }

    suspend fun saveUserData(email: String, password: String){
        context.dataStore.edit { prefs ->
            prefs[STORE_EMAIL] = email
            prefs[STORE_PASSWORD] = password
        }
    }

    suspend fun saveDidWeLogout(didWeLogout: Boolean){
        context.dataStore.edit { prefs ->
            prefs[STORE_DIDWELOGOUT] = didWeLogout
        }
    }

}