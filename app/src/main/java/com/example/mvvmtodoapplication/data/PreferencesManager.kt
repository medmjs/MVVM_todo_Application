package com.example.mvvmtodoapplication.data


import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton


enum class SortOrder { BY_NAME, BY_DATE }

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext val context: Context) {

    //Create DataStore Preferances
    private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "task_preferences")


    //create Keys
    val sortKey = stringPreferencesKey("sort_oreder")
    val hideKey = booleanPreferencesKey("hide_completed")


    // Insert Value to dataStore
    suspend fun updateSortOrder(sortOrder: SortOrder) =
        context.datastore.edit { preferences ->
            preferences[sortKey] = sortOrder.name ?: SortOrder.BY_DATE.name
        }

    suspend fun updateHideCompleted(hide: Boolean) =
        context.datastore.edit { preferences ->
            preferences[hideKey] = hide
        }


    //get data from datastore
    val preferencesFlow = context.datastore.data
        .catch { ex ->
            if (ex is IOException) {
                Log.d("medmjs dataStore ", ex.message.toString())
            } else {
                throw ex
            }
        }
        .map { preference ->
            val pref_Sort = SortOrder.valueOf(preference[sortKey] ?: SortOrder.BY_DATE.name)
            val pref_hide = preference[hideKey] ?: false

            FilterPreferances(pref_Sort, pref_hide)
        }


}

data class FilterPreferances(
    val sortOrder: SortOrder,
    val hide: Boolean
)