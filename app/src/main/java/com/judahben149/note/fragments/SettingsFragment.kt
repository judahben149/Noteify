package com.judahben149.note.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.judahben149.note.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}