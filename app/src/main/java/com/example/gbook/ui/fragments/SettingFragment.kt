package com.example.gbook.ui.fragments

import android.annotation.TargetApi
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreference
import androidx.preference.SwitchPreferenceCompat
import com.example.gbook.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_book_list.*
import java.util.*


class SettingFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PreferenceManager.getDefaultSharedPreferences(context)
            .registerOnSharedPreferenceChangeListener(this)


    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting, rootKey)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == "dark_mode") {
            val prefs = sharedPreferences?.getString(key, "1")

            when (prefs?.toInt()) {
                1 -> {
                    AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_NO
                    )
                }

                2 -> {
                    AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_YES
                    )
                }

                3 -> {
                    AppCompatDelegate.setDefaultNightMode(
                        AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
                    )
                }
            }
        }

        if (key == "language") {

            val prefs = sharedPreferences?.getString(key, "1")
            when (prefs?.toInt()) {
                1 -> {
                    var locale = Locale("en")
                    Locale.setDefault(locale)
                    var config = Configuration()
                    config.setLocale(locale)
                    context?.getResources()?.updateConfiguration(config, null)
                }
                2 -> {

                    var locale = Locale("ar")
                    Locale.setDefault(locale)
                    var config = Configuration()
                    config.setLocale(locale)
                    context?.getResources()?.updateConfiguration(config, null)

                }
            }
        }

        if (key == "background") {
            val prefs = sharedPreferences?.getBoolean(key, true)
            when (prefs!!) {
                true -> {
                    main_activity?.setBackgroundColor(Color.WHITE)
                    list?.setBackgroundColor(Color.WHITE)
                }
                false -> {
                    list?.setBackgroundColor(Color.DKGRAY)
                    main_activity?.setBackgroundColor(Color.DKGRAY)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(context)
            .unregisterOnSharedPreferenceChangeListener(this)
    }

}