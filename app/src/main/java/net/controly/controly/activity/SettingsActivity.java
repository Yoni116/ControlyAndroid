package net.controly.controly.activity;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.widget.Toast;

import net.controly.controly.R;

/**
 * This is the settings activity of the application.
 */
public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        Preference editProfileButton = findPreference("pref_edit_profile");
        editProfileButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(SettingsActivity.this, "Will open the edit profile activity", Toast.LENGTH_SHORT)
                        .show();

                return false;
            }
        });
    }
}
