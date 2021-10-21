package io.github.takusan23.countdownwidgetlist.fragment

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import io.github.takusan23.countdownwidgetlist.R
import io.github.takusan23.countdownwidgetlist.widget.updateAppWidget

/**
 * 設定画面
 * */
class PreferenceFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        setPreferencesFromResource(R.xml.preference, rootKey)

        findPreference<Preference>("widget_update")?.setOnPreferenceClickListener {
            updateAppWidget(requireContext())
            true
        }
    }
}