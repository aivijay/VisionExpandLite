package com.bcsoft.estx.visionexpand;

import im.delight.apprater.AppRater;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class MainActivity extends FragmentActivity {
	private AdView adView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*
		 * Set the title for the page
		 */
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.activity_main);

		/*
		 * Set the custom title for this page
		 */
		// getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
		// R.layout.standard_window_title);

		// TextView tv = (TextView) findViewById(R.id.windowTitleText);
		// tv.setText(getResources().getString(R.string.app_name));

		/*
		 * // Create the adView adView = new AdView(this, AdSize.BANNER,
		 * Constants.VISION_EXPAND_ADMOB_AD_UNIT_ID);
		 * 
		 * // Lookup your LinearLayout assuming it's been given // the attribute
		 * android:id="@+id/mainLayout" LinearLayout layout =
		 * (LinearLayout)findViewById(R.id.mainLayout);
		 * 
		 * // Add the adView to it layout.addView(adView);
		 * 
		 * // Initiate a generic request to load it with an ad adView.loadAd(new
		 * AdRequest());
		 */

		// Look up the AdView as a resource and load a request.
		AdView adView = (AdView) this.findViewById(R.id.adViewSmart);
		adView.loadAd(new AdRequest());

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		//
		// Show app rater to rate the app
		//

		// This will keep a track of when the app was first used and whether to
		// show a prompt
		// It should be the default implementation of AppRater
		// AppRater.app_launched(this);

		AppRater appRater = new AppRater(this, "com.bcsoft.estx.visionexpand");
		appRater.setDaysBeforePrompt(3);
		appRater.setLaunchesBeforePrompt(7);
		appRater.setPhrases(R.string.rate_title, R.string.rate_explanation,
				R.string.rate_now, R.string.rate_later, R.string.rate_never);

		appRater.setTargetUri("https://play.google.com/store/apps/details?id=%1$s");
		appRater.setPreferenceKeys("app_rater", "flag_dont_show",
				"launch_count", "first_launch_time");

		AlertDialog mAlertDialog = appRater.show();

	}

	/*
	 * public void onClickTraining(View view) { Intent i = new Intent(this,
	 * TrainingSelectActivity.class); startActivity(i); }
	 */

	public void onClickTraining(View view) {
		Intent i = new Intent(this, Training1MainActivity.class);
		startActivity(i);
	}

	public void onClickTraining2(View view) {
		Intent i = new Intent(this, Training2MainActivity.class);
		startActivity(i);
	}

	/*
	 * public void onClickTraining(View view) { Intent i = new Intent(this,
	 * VisionExpandMainActivity.class); startActivity(i); }
	 * 
	 * public void onClickTraining2(View view) { Intent i = new Intent(this,
	 * VisionExpandTwoMainActivity.class); startActivity(i); }
	 * 
	 * public void onClickDrills(View view) { Intent i = new Intent(this,
	 * DrillsActivity.class); startActivity(i); }
	 */

	public void onClickSettings(View view) {
		Intent i = new Intent(this, UserSettingActivity.class);
		startActivity(i);
	}

	public void onClickHelp(View view) {
		Intent i = new Intent(this, HelpActivity.class);
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.menu_settings:
			Intent i = new Intent(this, UserSettingActivity.class);
			startActivityForResult(i, Constants.RESULT_SETTINGS);
			break;

		}

		return true;
	}

}
