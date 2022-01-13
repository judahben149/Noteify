package com.judahben149.note.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.judahben149.note.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val libraries = arrayOf("ROOM", "Pretty Time", "Navigation component", "Material Components")
        findPreference<Preference>(R.string.librariesKey)?.setOnPreferenceClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.libraries)
                .setItems(libraries) { _, which ->
                    when (which) {
                        0 -> openLink("https://developer.android.com/jetpack/androidx/releases/room")
                        1 -> openLink("https://github.com/ocpsoft/prettytime")
                        2 -> openLink("https://developer.android.com/jetpack/androidx/releases/navigation")
                        3 -> openLink("https://github.com/material-components/material-components-android")
                    }
                }
                .setNegativeButton(R.string.cancel, null)
                .show()
            return@setOnPreferenceClickListener true
        }
    }

    private fun openLink(link: String) {
        val uri = Uri.parse(link)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun <T> findPreference(id: Int): T? = findPreference(getString(id))
}