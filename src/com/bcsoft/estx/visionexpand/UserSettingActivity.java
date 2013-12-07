package com.bcsoft.estx.visionexpand;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class UserSettingActivity extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		/*
		 * Set the title for the page
		 */
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		
		super.onCreate(savedInstanceState);
		
		/*
		 * Set the custom title for this page
		 */
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
		//		R.layout.standard_window_title);		
		
//		TextView tv = (TextView)findViewById(R.id.windowTitleText);
//		tv.setText(getResources().getString(R.string.app_name));		
		
		
		PreferenceManager prefMgr = getPreferenceManager();
		prefMgr.setSharedPreferencesName("visionExpandPreferences");

		addPreferencesFromResource(R.xml.settings);
		
		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

	}
}
