package ch.goodsolutions.swissqwiss.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import ch.goodsolutions.swissqwiss.R;

public class PrefsActivity extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
}
