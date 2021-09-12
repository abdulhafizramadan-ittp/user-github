package com.ahr.usergithub.preferences

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.ahr.usergithub.MainActivity
import com.ahr.usergithub.R

class SettingPreferences : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var dailyReminder: String
    private lateinit var dailyPreference: SwitchPreference
    private lateinit var sharePreferences: SharedPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)

        dailyReminder = getString(R.string.key_daily)
        dailyPreference = findPreference<SwitchPreference>(dailyReminder) as SwitchPreference
        sharePreferences = preferenceManager.sharedPreferences

        dailyPreference.isChecked = sharePreferences.getBoolean(dailyReminder, false)
        dailyPreference.setOnPreferenceChangeListener { preference, newValue ->
            if (newValue as Boolean) {
                Toast.makeText(activity, "active", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "un active", Toast.LENGTH_SHORT).show()
            }
            true
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        if (key == dailyReminder) {
            dailyPreference.isChecked = sharedPreferences.getBoolean(dailyReminder, false)
        }
    }
}